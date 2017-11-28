package com.lothrazar.cyclicmagic.component.beaconpotion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.gui.ITileSizeToggle;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBeaconPotion extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle, ITileSizeToggle {
  static final int MAX_POTION = 16000;
  private static final int POTION_TICKS = Const.TICKS_PER_SEC * 8;// 8 seconds
  private static final int MAX_RADIUS = 8;
  public static enum Fields {
    REDSTONE, TIMER, FUELMAX, ENTITYTYPE, RANGE;
  }
  public static enum EntityType {
    PLAYERS, NONPLAYER, ALL, MONSTER, CREATURE, AMBIENT, WATER; // ambient, monster, creature, water
  }
  static boolean doesConsumePotions;
  static List<String> blacklist;
  @SideOnly(Side.CLIENT)
  private long beamRenderCounter;
  @SideOnly(Side.CLIENT)
  private float beamRenderScale;
  private EntityType entityType = EntityType.PLAYERS;
  private final List<TileEntityBeaconPotion.BeamSegment> beamSegments = Lists.<TileEntityBeaconPotion.BeamSegment> newArrayList();
  private String customName;
  /** Primary potion effect given by this beacon. */
  @Nullable
  private List<PotionEffect> effects;
  private int needsRedstone;
  private int radius = MAX_RADIUS - 2;//just a mid tier default 
  public TileEntityBeaconPotion() {
    super(9);
    this.timer = 0;
  }
  @Override
  public void update() {
    if (!isRunning()) {
      return;
    }
    this.shiftAllUp(1);
    if (this.timer == 0) {
      //wipe out the current effects
      this.effects = new ArrayList<PotionEffect>();
      // and try to consume a potion
      ItemStack s = this.getStackInSlot(0);
      List<PotionEffect> newEffects = PotionUtils.getEffectsFromStack(s);
      if (newEffects != null && newEffects.size() > 0) {
        effects = new ArrayList<PotionEffect>();
        if (this.isPotionValid(newEffects)) {
          //first read all potins
          for (PotionEffect eff : newEffects) {
            //cannot set the duration time so we must copy it
            effects.add(new PotionEffect(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, false));
          }
          //then refil progress bar
          this.timer = MAX_POTION;
          //then consume the item, unless disabled
          if (doesConsumePotions) {
            this.setInventorySlotContents(0, ItemStack.EMPTY);
          }
        }
        // else at least one is not valid, do not eat the potoin
      }
    }
    else if (this.world.getTotalWorldTime() % 80L == 0L) {
      this.updateTimerIsZero();
      this.updateBeacon();
      world.addBlockEvent(this.pos, Blocks.BEACON, 1, 0);
    }
  }
  private boolean isPotionValid(List<PotionEffect> newEffects) {
    String id;
    for (PotionEffect eff : newEffects) {
      id = eff.getPotion().getRegistryName().toString();
      for (String match : blacklist) {
        if (id.equals(match)) {
          return false;
        }
      }
    }
    return true;
  }
  public void updateBeacon() {
    if (this.world != null) {
      this.updateSegmentColors();
      this.addEffectsToEntities();
    }
  }
  public String getFirstEffectName() {
    if (this.effects == null || this.effects.size() == 0) {
      return "";
    }
    return this.effects.get(0).getEffectName();
  }
  private void addEffectsToEntities() {
    if (this.effects == null || this.effects.size() == 0) {
      return;
    }
    int x = this.pos.getX();
    int y = this.pos.getY();
    int z = this.pos.getZ();
    int theRadius = ((int) Math.pow(2, this.radius));
    AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1))).grow(theRadius).expand(0.0D, (double) this.world.getHeight(), 0.0D);
    //get players, or non players, or both. but players extend living base too.
    boolean skipPlayers = (this.entityType == EntityType.NONPLAYER);
    boolean showParticles = (this.entityType == EntityType.PLAYERS);
    EnumCreatureType creatureType = this.getCreatureType();
    List<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
    if (this.entityType == EntityType.PLAYERS) {
      list.addAll(this.world.getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb));
    }
    else { // we apply other filters later
      list.addAll(this.world.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb));
    }
    for (EntityLivingBase entity : list) {
      if (skipPlayers && entity instanceof EntityPlayer) {
        continue;// filter says to skip players
      }
      if (creatureType != null && entity.isCreatureType(creatureType, false) == false) {
        continue;//creature type filter is enabled AND this one doesnt match, so skip
      }
      for (PotionEffect eff : this.effects) {
        if (entity.getActivePotionEffect(eff.getPotion()) != null) {
          //important to use combine for thing effects that apply attributes such as health
          entity.getActivePotionEffect(eff.getPotion()).combine(eff);
        }
        else {
          entity.addPotionEffect(new PotionEffect(eff.getPotion(), POTION_TICKS, eff.getAmplifier(), true, showParticles));
        }
      }
    }
  }
  @SuppressWarnings("incomplete-switch")
  private EnumCreatureType getCreatureType() {
    switch (this.entityType) {
      case AMBIENT:
        return EnumCreatureType.AMBIENT;
      case CREATURE:
        return EnumCreatureType.CREATURE;
      case MONSTER:
        return EnumCreatureType.MONSTER;
      case WATER:
        return EnumCreatureType.WATER_CREATURE;
    }
    return null;
  }
  private void updateSegmentColors() {
    int i = this.pos.getX();
    int j = this.pos.getY();
    int k = this.pos.getZ();
    // int l = 5;
    this.beamSegments.clear();
    BeamSegment tileentitybeacon$beamsegment = new BeamSegment(EnumDyeColor.WHITE.getColorComponentValues());
    this.beamSegments.add(tileentitybeacon$beamsegment);
    boolean flag = true;
    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
    for (int i1 = j + 1; i1 < 256; ++i1) {
      IBlockState iblockstate = this.world.getBlockState(blockpos$mutableblockpos.setPos(i, i1, k));
      float[] afloat;
      if (iblockstate.getBlock() == Blocks.STAINED_GLASS) {
        afloat = ((EnumDyeColor) iblockstate.getValue(BlockStainedGlass.COLOR)).getColorComponentValues();
      }
      else {
        if (iblockstate.getBlock() != Blocks.STAINED_GLASS_PANE) {
          if (iblockstate.getLightOpacity(world, blockpos$mutableblockpos) >= 15 && iblockstate.getBlock() != Blocks.BEDROCK) {
            this.beamSegments.clear();
            break;
          }
          float[] customColor = iblockstate.getBlock().getBeaconColorMultiplier(iblockstate, this.world, blockpos$mutableblockpos, getPos());
          if (customColor != null)
            afloat = customColor;
          else {
            tileentitybeacon$beamsegment.incrementHeight();
            continue;
          }
        }
        else
          afloat = ((EnumDyeColor) iblockstate.getValue(BlockStainedGlassPane.COLOR)).getColorComponentValues();
      }
      if (!flag) {
        afloat = new float[] { (tileentitybeacon$beamsegment.getColors()[0] + afloat[0]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[1] + afloat[1]) / 2.0F, (tileentitybeacon$beamsegment.getColors()[2] + afloat[2]) / 2.0F };
      }
      if (Arrays.equals(afloat, tileentitybeacon$beamsegment.getColors())) {
        tileentitybeacon$beamsegment.incrementHeight();
      }
      else {
        tileentitybeacon$beamsegment = new BeamSegment(afloat);
        this.beamSegments.add(tileentitybeacon$beamsegment);
      }
      flag = false;
    }
  }
  @SideOnly(Side.CLIENT)
  public List<TileEntityBeaconPotion.BeamSegment> getBeamSegments() {
    return this.beamSegments;
  }
  @SideOnly(Side.CLIENT)
  public float shouldBeamRender() {
    if (!this.isRunning()) { // if no redstone power, return zero to hide beam
      return 0;
    }
    int i = (int) (this.world.getTotalWorldTime() - this.beamRenderCounter);
    this.beamRenderCounter = this.world.getTotalWorldTime();
    if (i > 1) {
      this.beamRenderScale -= (float) i / 40.0F;
      if (this.beamRenderScale < 0.0F) {
        this.beamRenderScale = 0.0F;
      }
    }
    this.beamRenderScale += 0.025F;
    if (this.beamRenderScale > 1.0F) {
      this.beamRenderScale = 1.0F;
    }
    return this.beamRenderScale;
  }
  @Nullable
  public SPacketUpdateTileEntity getUpdatePacket() {
    return new SPacketUpdateTileEntity(this.pos, 3, this.getUpdateTag());
  }
  public NBTTagCompound getUpdateTag() {
    return this.writeToNBT(new NBTTagCompound());
  }
  @SideOnly(Side.CLIENT)
  public double getMaxRenderDistanceSquared() {
    return 65536.0D;
  }
  @Nullable
  private static Potion isBeaconEffect(int i) {
    return Potion.getPotionById(i);
  }
  /**
   * Returns true if this thing is named
   */
  public boolean hasCustomName() {
    return this.customName != null && !this.customName.isEmpty();
  }
  public void setName(String name) {
    this.customName = name;
  }
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() != null && stack.getItem() instanceof ItemPotion;
  }
  public boolean receiveClientEvent(int id, int type) {
    if (id == 1) {
      this.updateBeacon();
      return true;
    }
    else {
      return super.receiveClientEvent(id, type);
    }
  }
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
  }
  /**
   * Returns true if automation can insert the given item in the given slot from the given side.
   */
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return true;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case FUELMAX:
        return MAX_POTION;
      case ENTITYTYPE:
        return this.entityType.ordinal();
      case RANGE:
        return this.radius;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case FUELMAX:
      break;
      case ENTITYTYPE:
        if (value >= EntityType.values().length)
          value = 0;
        if (value < 0)
          value = EntityType.values().length - 1;
        this.entityType = EntityType.values()[value];
      break;
      case RANGE:
        this.radius = Math.min(value, MAX_RADIUS);
      break;
    }
  }
  @Override
  public void toggleNeedsRedstone() {
    this.setField(Fields.REDSTONE.ordinal(), (this.needsRedstone + 1) % 2);
  }
  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  /**
   * Returns true if automation can extract the given item in the given slot from the given side.
   */
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return false;
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.radius = tagCompound.getInteger("radius");
    this.needsRedstone = tagCompound.getInteger("red");
    int eType = tagCompound.getInteger("et");
    NBTTagList tagList = tagCompound.getTagList("potion_list", 10);
    this.effects = new ArrayList<PotionEffect>();
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      String potion = tag.getString("potion_effect");
      int strength = tag.getInteger("potion_strength");
      Potion p = Potion.getPotionFromResourceLocation(potion);
      if (p != null) {
        this.effects.add(new PotionEffect(p, POTION_TICKS, strength));
      }
    }
    if (eType >= 0 && eType < EntityType.values().length) {
      this.entityType = EntityType.values()[eType];
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("radius", radius);
    tagCompound.setInteger("et", entityType.ordinal());
    tagCompound.setInteger("red", this.needsRedstone);
    NBTTagList itemList = new NBTTagList();
    if (this.effects != null) {
      for (PotionEffect e : this.effects) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("potion_effect", e.getPotion().getRegistryName().toString());
        tag.setInteger("potion_strength", e.getAmplifier());
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag("potion_list", itemList);
    return super.writeToNBT(tagCompound);
  }
  public EntityType getEntityType() {
    int type = this.getField(Fields.ENTITYTYPE.ordinal());
    return EntityType.values()[type];
  }
  public static class BeamSegment {
    /** RGB (0 to 1.0) colors of this beam segment */
    private final float[] colors;
    private int height;
    public BeamSegment(float[] colorsIn) {
      this.colors = colorsIn;
      this.height = 1;
    }
    protected void incrementHeight() {
      ++this.height;
    }
    /**
     * Returns RGB (0 to 1.0) colors of this beam segment
     */
    public float[] getColors() {
      return this.colors;
    }
    @SideOnly(Side.CLIENT)
    public int getHeight() {
      return this.height;
    }
  }
  public int getRadiusCalc() {
    return (int) Math.pow(2, this.radius);
  }
  @Override
  public void toggleSizeShape() {
    int newRad = this.getField(Fields.RANGE.ordinal()) + 1;
    if (newRad > MAX_RADIUS) {
      newRad = 4;
    }
    this.setField(Fields.RANGE.ordinal(), newRad);
  }
}
