package com.lothrazar.cyclicmagic.component.beaconpotion;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBeaconPotion extends TileEntityBaseMachineInvo implements ITickable {
  @SideOnly(Side.CLIENT)
  private long beamRenderCounter;
  @SideOnly(Side.CLIENT)
  private float beamRenderScale;
  private final List<TileEntityBeaconPotion.BeamSegment> beamSegments = Lists.<TileEntityBeaconPotion.BeamSegment> newArrayList();
  private String customName;
  /** Primary potion effect given by this beacon. */
  @Nullable
  private Potion primaryEffect;
  public TileEntityBeaconPotion() {
    super(0);
  }
  @Override
  public void update() {
    if (this.world.getTotalWorldTime() % 80L == 0L) {
      this.updateBeacon();
      world.addBlockEvent(this.pos, Blocks.BEACON, 1, 0);
    }
  }
  public void updateBeacon() {
//    this.primaryEffect = PotionEffectRegistry.BOUNCE;
    this.primaryEffect = MobEffects.REGENERATION;
    if (this.world != null) {
      this.updateSegmentColors();
      this.addEffectsToPlayers();
    }
  }
  private void addEffectsToPlayers() {
    if (this.primaryEffect != null) {
      double radius = 64;
      int x = this.pos.getX();
      int y = this.pos.getY();
      int z = this.pos.getZ();
      AxisAlignedBB axisalignedbb = (new AxisAlignedBB((double) x, (double) y, (double) z, (double) (x + 1), (double) (y + 1), (double) (z + 1))).grow(radius).expand(0.0D, (double) this.world.getHeight(), 0.0D);
      List<EntityPlayer> list = this.world.<EntityPlayer> getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);
      for (EntityPlayer entityplayer : list) {
        entityplayer.addPotionEffect(new PotionEffect(this.primaryEffect, 800, 1, true, true));
      }
    }
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
    // if no redstone power, return zero;
    if (world.isBlockPowered(pos) == false) {
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
  /**
   * Returns true if automation is allowed to insert the given stack (ignoring
   * stack size) into the given slot. For guis use Slot.isItemValid
   */
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() != null && stack.getItem().isBeaconPayment(stack);
  }
  public String getGuiID() {
    return "minecraft:beacon";
  }
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    return new ContainerBeacon(playerInventory, this);
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
    return new int[0];
  }
  /**
   * Returns true if automation can insert the given item in the given slot from
   * the given side.
   */
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return false;
  }
  /**
   * Returns true if automation can extract the given item in the given slot
   * from the given side.
   */
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return false;
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
}
