package com.lothrazar.cyclicmagic.component.autouser;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.ITileSizeToggle;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class TileEntityUser extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITileSizeToggle, ITilePreviewToggle, ITickable {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBT_LR = "lr";
  private static final int MAX_SIZE = 4;//9x9 area 
  public static int maxHeight = 10;
  public static int TIMER_FULL = 120;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  private int[] hopperInputFuel = { 9 };// all slots for all faces
  //  final int RADIUS = 4;//center plus 4 in each direction = 9x9
  private int rightClickIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int needsRedstone = 1;
  private int renderParticles = 0;
  private int toolSlot = 0;
  private int size;
  public static enum Fields {
    TIMER, SPEED, REDSTONE, LEFTRIGHT, SIZE, RENDERPARTICLES, FUEL, FUELMAX;
  }
  public TileEntityUser() {
    super(10);
    timer = TIMER_FULL;
    this.setFuelSlot(9);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public void update() {
    if (!isRunning()) { return; }
    // this.shiftAllUp();
    this.updateFuelIsBurning();
    boolean triggered = this.updateTimerIsZero();
    if (triggered) {
      this.spawnParticlesAbove();
    }
    if (world instanceof WorldServer) {
      verifyUuid(world);
      if (fakePlayer == null) {
        fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) world, this.uuid);
        if (fakePlayer == null) {
          ModCyclic.logger.error("Fake player failed to init ");
          return;
        }
      }
      tryEquipItem();
      if (triggered) {
        timer = TIMER_FULL;
        try {
          BlockPos targetPos = this.getTargetPos();//this.getCurrentFacingPos();/
          if (rightClickIfZero == 0) {//right click entities and blocks
            this.rightClickBlock(targetPos);
          }
          interactEntities(targetPos);
        }
        catch (Exception e) {//exception could come from external third party block/mod/etc
          ModCyclic.logger.error("Automated User Error");
          ModCyclic.logger.error(e.getLocalizedMessage());
          e.printStackTrace();
        }
      } //timer == 0 block
      //      else {
      //        timer = 1;//allows it to run on a pulse
      //      }
    }
  }
  private void interactEntities(BlockPos targetPos) {
    BlockPos entityCenter = this.getPos().offset(this.getCurrentFacing(), 1);
    int vRange = 1;
    AxisAlignedBB entityRange = UtilEntity.makeBoundingBox(entityCenter, size + 1, vRange);
    List<? extends Entity> living = world.getEntitiesWithinAABB(EntityLivingBase.class, entityRange);
    List<? extends Entity> carts = world.getEntitiesWithinAABB(EntityMinecart.class, entityRange);
    List<Entity> all = new ArrayList<Entity>(living);
    all.addAll(carts);//works since  they share a base class but no overlap
    if (rightClickIfZero == 0) {//right click entities and blocks
      this.getWorld().markChunkDirty(targetPos, this);
      for (Entity ent : all) {//both living and minecarts
        // on the line below: NullPointerException  at com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser.func_73660_a(TileMachineUser.java:101)
        if (world.isRemote == false &&
            ent != null && ent.isDead == false
            && fakePlayer != null && fakePlayer.get() != null) {
          validateTool(); //recheck this at every step so we dont go negative
          if (EnumActionResult.FAIL != fakePlayer.get().interactOn(ent, EnumHand.MAIN_HAND)) {
            this.tryDumpFakePlayerInvo();
            break;//dont do every entity in teh whole batch
          }
        }
      }
    }
    else { // left click entities and blocks 
      ItemStack held = fakePlayer.get().getHeldItemMainhand();
      fakePlayer.get().onGround = true;
      for (Entity e : living) {// only living, not minecarts
        EntityLivingBase ent = (EntityLivingBase) e;
        if (e == null) {
          continue;
        } //wont happen eh
        fakePlayer.get().attackTargetEntityWithCurrentItem(ent);
        //THANKS TO FORUMS http://www.minecraftforge.net/forum/index.php?topic=43152.0
        IAttributeInstance damage = new AttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        if (!held.isEmpty()) {
          for (AttributeModifier modifier : held.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getName())) {
            damage.applyModifier(modifier);
          }
        }
        float dmgVal = (float) damage.getAttributeValue();
        float f1 = EnchantmentHelper.getModifierForCreature(held, (ent).getCreatureAttribute());
        ent.attackEntityFrom(DamageSource.causePlayerDamage(fakePlayer.get()), dmgVal + f1);
      }
    }
  }
  private void rightClickBlock(BlockPos targetPos) {
    if (Block.getBlockFromItem(fakePlayer.get().getHeldItemMainhand().getItem()) != Blocks.AIR) { return; }
    //dont ever place a block. they want to use it on an entity
    fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND, targetPos, EnumFacing.UP, .5F, .5F, .5F);
  }
  private void tryDumpFakePlayerInvo() {
    for (ItemStack s : fakePlayer.get().inventory.mainInventory) {
      if (!s.isEmpty() && !s.equals(fakePlayer.get().getHeldItemMainhand())) {
        EntityItem entityItem = UtilItemStack.dropItemStackInWorld(world, getCurrentFacingPos(), s.copy());
        if (entityItem != null) {
          entityItem.setVelocity(0, 0, 0);
        }
        s.setCount(0);
      }
    }
  }
  /**
   * detect if tool stack is empty or destroyed and reruns equip
   */
  private void validateTool() {
    ItemStack maybeTool = getStackInSlot(toolSlot);
    if (maybeTool != ItemStack.EMPTY && maybeTool.getCount() < 0) {
      maybeTool = ItemStack.EMPTY;
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      inv.set(toolSlot, ItemStack.EMPTY);
    }
  }
  private ItemStack tryEquipItem() {
    ItemStack maybeTool = getStackInSlot(toolSlot);
    if (maybeTool != ItemStack.EMPTY) {
      //do we need to make it null
      if (maybeTool.getCount() <= 0) {
        maybeTool = ItemStack.EMPTY;
      }
    }
    fakePlayer.get().setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());//seems to help interact() mob drops like milk
    fakePlayer.get().onUpdate();//trigger   ++this.ticksSinceLastSwing; among other things
    if (maybeTool == ItemStack.EMPTY) {//null for any reason
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
      inv.set(toolSlot, ItemStack.EMPTY);
    }
    else {
      //so its not null
      if (!maybeTool.equals(fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND))) {
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, maybeTool);
      }
    }
    return maybeTool;
  }
  private void verifyUuid(World world) {
    if (uuid == null) {
      uuid = UUID.randomUUID();
      IBlockState state = world.getBlockState(this.pos);
      world.notifyBlockUpdate(pos, state, state, 3);
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (uuid != null) {
      compound.setString(NBTPLAYERID, uuid.toString());
    }
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger(NBT_LR, rightClickIfZero);
    compound.setInteger(NBT_SIZE, size);
    compound.setInteger(NBT_RENDER, renderParticles);
    return super.writeToNBT(compound);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(compound.getString(NBTPLAYERID));
    }
    needsRedstone = compound.getInteger(NBT_REDST);
    rightClickIfZero = compound.getInteger(NBT_LR);
    size = compound.getInteger(NBT_SIZE);
    this.renderParticles = compound.getInteger(NBT_RENDER);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    if (side == EnumFacing.UP)
      return hopperInputFuel;
    return hopperInput;
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case SPEED:
        return getSpeed();
      case TIMER:
        return getTimer();
      case REDSTONE:
        return this.needsRedstone;
      case SIZE:
        return this.size;
      case LEFTRIGHT:
        return this.rightClickIfZero;
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
      case RENDERPARTICLES:
        return this.renderParticles;
      default:
      break;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case SPEED:
        this.setSpeed(value);
      break;
      case TIMER:
        if (value < 0) {
          value = 0;
        }
        timer = Math.min(value, TIMER_FULL);
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case LEFTRIGHT:
        this.rightClickIfZero = value;
      break;
      case SIZE:
        this.size = value;
      break;
      case FUEL:
        this.setFuelCurrent(value);
      break;
      case FUELMAX:
        this.setFuelMax(value);
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
      default:
      break;
    }
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    // Extracts data from a packet (S35PacketUpdateTileEntity) that was sent
    // from the server. Called on client only.
    this.readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }
  public int getTimer() {
    return timer;
  }
  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone == 1) ? 0 : 1;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }
  public void toggleLeftRight() {
    int val = (this.rightClickIfZero == 1) ? 0 : 1;
    this.setField(Fields.LEFTRIGHT.ordinal(), val);
  }
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
  @Override
  public void toggleSizeShape() {
    this.size++;
    if (this.size > MAX_SIZE) {
      this.size = 0;
    }
  }
  private BlockPos getTargetPos() {
    BlockPos targetPos = UtilWorld.getRandomPos(getWorld().rand, getTargetCenter(), this.size);
    if (world.isAirBlock(targetPos)) {
      targetPos = targetPos.down();
    }
    return targetPos;
  }
  public BlockPos getTargetCenter() {
    //move center over that much, not including exact horizontal
    return this.getPos().offset(this.getCurrentFacing(), this.size + 1);
  }
  @Override
  public void togglePreview() {
    this.renderParticles = (renderParticles + 1) % 2;
    //    
    //    List<BlockPos> allPos = UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
    //    for (BlockPos pos : allPos) {
    //      UtilParticle.spawnParticle(getWorld(), EnumParticleTypes.DRAGON_BREATH, pos);
    //    }
  }
  @Override
  public List<BlockPos> getShape() {
    return UtilShape.squareHorizontalHollow(getTargetCenter(), this.size);
  }
  @Override
  public boolean isPreviewVisible() {
    return this.getField(Fields.RENDERPARTICLES.ordinal()) == 1;
  }
}