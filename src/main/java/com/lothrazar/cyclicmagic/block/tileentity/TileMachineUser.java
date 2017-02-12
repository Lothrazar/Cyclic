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
import net.minecraft.nbt.NBTTagList;
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
  public static int maxHeight = 10;
  public static int TIMER_FULL = 80;
  private ItemStack[] inv;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  final int RADIUS = 4;//center plus 4 in each direction = 9x9
  private static final String NBTPLAYERID = "uuid";
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_REDST = "redstone";
  private static final String NBT_SPEED = "h";//WTF why did i name it this
  private static final String NBT_LR = "lr";
  private int speed = 1;
  private int rightClickIfZero = 0;
  private WeakReference<FakePlayer> fakePlayer;
  private UUID uuid;
  private int timer;
  private int needsRedstone = 1;
  public static enum Fields {
    TIMER, SPEED, REDSTONE, LEFTRIGHT
  }
  public TileMachineUser() {
    inv = new ItemStack[9];
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
          ModCyclic.logger.warn("Warning: Fake player failed to init ");
          return;
        }
      }
      ItemStack maybeTool = tryEquipItem();
      if (isRunning()) {
        timer -= this.getSpeed();
        if (timer <= 0) {
          timer = 0;
        }
        if (timer == 0) {
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
                fakePlayer.get().interact(ent, maybeTool, EnumHand.MAIN_HAND);
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
              if (held != null)
                for (AttributeModifier modifier : held.getAttributeModifiers(EntityEquipmentSlot.MAINHAND).get(SharedMonsterAttributes.ATTACK_DAMAGE.getAttributeUnlocalizedName()))
                damage.applyModifier(modifier);
              float dmgVal = (float) damage.getAttributeValue();
              float f1 = EnchantmentHelper.getModifierForCreature(held, (ent).getCreatureAttribute());
              //              UtilChat.addChatMessage(this.getWorld(), "baseWeapon" + dmgVal);
              //              UtilChat.addChatMessage(this.getWorld(), "enchant" + f1);
              //        
              ent.attackEntityFrom(DamageSource.causePlayerDamage(fakePlayer.get()), dmgVal + f1);
            }
          }
        }
      }
      else {
        timer = 1;//allows it to run on a pulse
      }
    }
  }
  private ItemStack tryEquipItem() {
    int toolSlot = 0;
    ItemStack maybeTool = getStackInSlot(toolSlot);
    if (maybeTool != null) {
      //do we need to make it null
      if (maybeTool.stackSize == 0) {
        maybeTool = null;
      }
    }
    fakePlayer.get().setPosition(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());//seems to help interact() mob drops like milk
    fakePlayer.get().onUpdate();//trigger   ++this.ticksSinceLastSwing; among other things
    if (maybeTool == null) {//null for any reason
      fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, null);
      inv[toolSlot] = null;
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
    //invo stuff
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.length; i++) {
      ItemStack stack = inv[i];
      if (stack != null) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    tagCompound.setTag(NBT_INV, itemList);
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
    //invo stuff
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  @Override
  public int getSizeInventory() {
    return inv.length;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv[index];
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.stackSize <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.stackSize == 0) {
          setInventorySlotContents(index, null);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      setInventorySlotContents(index, null);
    }
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
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
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else
      return super.receiveClientEvent(id, value);
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
