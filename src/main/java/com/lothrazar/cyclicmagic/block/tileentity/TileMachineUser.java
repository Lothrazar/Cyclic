package com.lothrazar.cyclicmagic.block.tileentity;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.UUID;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilFakePlayer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class TileMachineUser extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  private static final String NBTPLAYERID = "uuid";
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_SPEED = "h";//WTF why did i name it this
  private static final String NBT_LR = "lr";
  public static int maxHeight = 10;
  public static int TIMER_FULL = 80;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  final int RADIUS = 4;//center plus 4 in each direction = 9x9
  private int speed = 1;
  private int rightClickIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int timer;
  private int needsRedstone = 1;
  int toolSlot = 0;
  public static enum Fields {
    TIMER, SPEED, REDSTONE, LEFTRIGHT
  }
  public TileMachineUser() {
    super(9);
    timer = TIMER_FULL;
    speed = 1;
  }
  @Override
  public void update() {
    this.shiftAllUp();
    if (isRunning()) {
      this.spawnParticlesAbove();
    }
    World world = getWorld();
    if (world instanceof WorldServer) {
      verifyUuid(world);
      if (fakePlayer == null) {
        fakePlayer = UtilFakePlayer.initFakePlayer((WorldServer) world, this.uuid);
        if (fakePlayer == null) {
          ModCyclic.logger.error("Fake player failed to init ");
          return;
        }
      }
      //      ItemStack maybeTool =
      tryEquipItem();
      if (isRunning()) {
        timer -= this.getSpeed();
        if (timer <= 0) {
          timer = 0;
        }
        if (timer == 0) {
          try {
            timer = TIMER_FULL;
            //act on block
            BlockPos targetPos = this.getCurrentFacingPos();//pos.offset(this.getCurrentFacing()); //not sure if this is needed
            if (world.isAirBlock(targetPos)) {
              targetPos = targetPos.down();
            }
            int hRange = 2;
            int vRange = 1;
            //so in a radius 2 area starting one block away
            BlockPos entityCenter = this.getPos().offset(this.getCurrentFacing(), 1);
            if (rightClickIfZero == 0) {
              fakePlayer.get().interactionManager.processRightClickBlock(fakePlayer.get(), world, fakePlayer.get().getHeldItemMainhand(), EnumHand.MAIN_HAND, targetPos, EnumFacing.UP, .5F, .5F, .5F);
              //this.getWorld().markChunkDirty(this.getPos(), this);
              this.getWorld().markChunkDirty(targetPos, this);
              AxisAlignedBB range = UtilEntity.makeBoundingBox(entityCenter, hRange, vRange);
              List<EntityLivingBase> all = world.getEntitiesWithinAABB(EntityLivingBase.class, range);
              for (EntityLivingBase ent : all) {
                // on the line below: NullPointerException  at com.lothrazar.cyclicmagic.block.tileentity.TileMachineUser.func_73660_a(TileMachineUser.java:101)
                if (world.isRemote == false &&
                    ent != null && ent.isDead == false
                    && fakePlayer != null && fakePlayer.get() != null) {
                  validateTool(); //recheck this at every step so we dont go negative
                  fakePlayer.get().interactOn(ent, EnumHand.MAIN_HAND);
                }
              }
            }
            else {
              AxisAlignedBB range = UtilEntity.makeBoundingBox(entityCenter, 1, 1);
              List<EntityLivingBase> all = world.getEntitiesWithinAABB(EntityLivingBase.class, range);
              ItemStack held = fakePlayer.get().getHeldItemMainhand();
              fakePlayer.get().onGround = true;
              for (EntityLivingBase ent : all) {
                fakePlayer.get().attackTargetEntityWithCurrentItem(ent);
                //THANKS TO FORUMS http://www.minecraftforge.net/forum/index.php?topic=43152.0
                IAttributeInstance damage = new AttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
                if (held != ItemStack.EMPTY) {
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
          catch (Exception e) {
            ModCyclic.logger.error("Automated User Error");
            ModCyclic.logger.error(e.getLocalizedMessage());
            e.printStackTrace();
          }
        } //timer == 0 block
      }
      else {
        timer = 1;//allows it to run on a pulse
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
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    if (uuid != null) {
      tagCompound.setString(NBTPLAYERID, uuid.toString());
    }
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger(NBT_SPEED, speed);
    tagCompound.setInteger(NBT_LR, rightClickIfZero);
    return super.writeToNBT(tagCompound);
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
    if (tagCompound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(tagCompound.getString(NBTPLAYERID));
    }
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    rightClickIfZero = tagCompound.getInteger(NBT_LR);
    speed = tagCompound.getInteger(NBT_SPEED);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
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
      default:
      break;
      case LEFTRIGHT:
        return this.rightClickIfZero;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case SPEED:
        if (value > maxHeight) {
          value = maxHeight;
        }
        setSpeed(value);
      break;
      case TIMER:
        timer = value;
        if (timer > TIMER_FULL) {
          timer = TIMER_FULL;
        }
        if (timer < 0) {
          timer = 0;
        }
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case LEFTRIGHT:
        this.rightClickIfZero = value;
      break;
    }
  }
  public int getSpeed() {
    return this.speed;//this.getField(Fields.HEIGHT.ordinal());
  }
  public void setSpeed(int val) {
    this.speed = val;
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
}