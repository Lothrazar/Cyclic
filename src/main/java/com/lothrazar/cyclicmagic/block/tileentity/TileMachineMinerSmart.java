package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * SEE TileMachineMiner
 * 
 */
public class TileMachineMinerSmart extends TileEntityBaseMachineInvoPlayer {
  //vazkii wanted simple block breaker and block placer. already have the BlockBuilder for placing :D
  //of course this isnt standalone and hes probably found some other mod by now but doing it anyway https://twitter.com/Vazkii/status/767569090483552256
  // fake player idea ??? https://gitlab.prok.pw/Mirrors/minecraftforge/commit/f6ca556a380440ededce567f719d7a3301676ed0
  public static int maxHeight = 10;
  private boolean isCurrentlyMining;
  private float curBlockDamage;
  private boolean firstTick = true;
  private BlockPos targetPos = null;
  private ItemStack[] inv;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots for all faces
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  int height = 6;//TODO: gui field
  public static enum Fields {
    HEIGHT
  }
  public TileMachineMinerSmart() {
    inv = new ItemStack[5];
  }
  @Override
  public void update() {
    int toolSlot = inv.length - 1;
    if (this.isPowered()) {
      this.spawnParticlesAbove();
    }
    if (!worldObj.isRemote) {
      if (firstTick || fakePlayer == null) {
        firstTick = false;
        initFakePlayer();
      }
      ItemStack maybeTool = inv[toolSlot];
      if (maybeTool == null) {
        fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, null);
      }
      else {
        if (maybeTool.equals(fakePlayer.get().getHeldItem(EnumHand.MAIN_HAND))) {
          //already equipped
        }
        else {
          fakePlayer.get().setHeldItem(EnumHand.MAIN_HAND, maybeTool);
        }
      }
      if (targetPos == null) {
        targetPos = pos.offset(this.getCurrentFacing()); //not sure if this is needed
      }
      if (this.isPowered()) {
        if (isCurrentlyMining == false) { //we can mine but are not currently
          this.updateTargetPos();
          if (isTargetValid()) { //we have a valid target
            isCurrentlyMining = true;
            curBlockDamage = 0;
          }
          else { // no valid target, back out
            isCurrentlyMining = false;
            resetProgress(targetPos);
          }
        }
        // else    we can mine ANd we are already mining, dont change anything eh
      }
      else { // we do not have power
        if (isCurrentlyMining) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
        }
      }
      if (isCurrentlyMining) {
        IBlockState targetState = worldObj.getBlockState(targetPos);
        curBlockDamage += targetState.getBlock().getPlayerRelativeBlockHardness(targetState, fakePlayer.get(), worldObj, targetPos);
        if (curBlockDamage >= 1.0f) {
          isCurrentlyMining = false;
          resetProgress(targetPos);
          if (fakePlayer.get() != null) {
            fakePlayer.get().interactionManager.tryHarvestBlock(targetPos);
          }
        }
        else {
          worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, (int) (curBlockDamage * 10.0F) - 1);
        }
      }
    }
  }
  private boolean isTargetValid() {
    if (worldObj.isAirBlock(targetPos) || worldObj.getBlockState(targetPos) == null) { return true; }
    IBlockState targetState = worldObj.getBlockState(targetPos);
    Block target = targetState.getBlock();
    //else check blacklist
    ItemStack item;
    for (int i = 0; i < inv.length - 1; i++) {//minus 1 because of TOOL
      if (inv[i] == null) {
        continue;
      }
      item = inv[i];
      if (item.getItem() == Item.getItemFromBlock(target)) { return false; }
    }
    return true;
  }
  final int RADIUS = 4;//center plus 4 in each direction = 9x9
  private void updateTargetPos() {
    //lets make it a AxA?
    //always restart here so we dont offset out of bounds
    EnumFacing facing = this.getCurrentFacing();
    BlockPos center = pos.offset(facing, 5);//move one more over so we are in the exact center of a 3x3x3 area
    //so the rand range is basically [0,8], then we left shift into [-4,+4]
    targetPos = center;
    //HEIGHT
    if (height == 0) {
      height = 6;
    } //should be in first time init. it needs a default
    int rollHeight = worldObj.rand.nextInt(height);
    //now do the vertical
    if (rollHeight > 0) {
      targetPos = targetPos.offset(EnumFacing.UP, rollHeight);
    }
    //negative doesnt work ,but lets not. quarries already exist
    //    else if(rollHeight < 0){
    //      targetPos = targetPos.offset(EnumFacing.DOWN, -1*rollHeight);
    //    }
    //HORIZONTAL
    int randNS = worldObj.rand.nextInt(RADIUS * 2 + 1) - RADIUS;
    int randEW = worldObj.rand.nextInt(RADIUS * 2 + 1) - RADIUS;
    //(" H, NS, EW : "+ rollHeight +":"+ randNS +":"+ randEW);
    //both can be zero
    if (randNS > 0) {
      targetPos = targetPos.offset(EnumFacing.NORTH, randNS);
    }
    else if (randNS < 0) {
      targetPos = targetPos.offset(EnumFacing.SOUTH, -1 * randNS);
    }
    if (randEW > 0) {
      targetPos = targetPos.offset(EnumFacing.EAST, randEW);
    }
    else if (randEW < 0) {
      targetPos = targetPos.offset(EnumFacing.WEST, -1 * randEW);
    }
    return;
  }
  private static final String NBTMINING = "mining";
  private static final String NBTDAMAGE = "curBlockDamage";
  private static final String NBTPLAYERID = "uuid";
  private static final String NBTTARGET = "target";
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    if (uuid != null) {
      compound.setString(NBTPLAYERID, uuid.toString());
    }
    if (targetPos != null) {
      compound.setIntArray(NBTTARGET, new int[] { targetPos.getX(), targetPos.getY(), targetPos.getZ() });
    }
    compound.setBoolean(NBTMINING, isCurrentlyMining);
    compound.setFloat(NBTDAMAGE, curBlockDamage);
    compound.setInteger("h", height);
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
    compound.setTag(NBT_INV, itemList);
    return super.writeToNBT(compound);
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    if (compound.hasKey(NBTPLAYERID)) {
      uuid = UUID.fromString(compound.getString(NBTPLAYERID));
    }
    if (compound.hasKey(NBTTARGET)) {
      int[] coords = compound.getIntArray(NBTTARGET);
      if (coords.length >= 3) {
        targetPos = new BlockPos(coords[0], coords[1], coords[2]);
      }
    }
    isCurrentlyMining = compound.getBoolean(NBTMINING);
    curBlockDamage = compound.getFloat(NBTDAMAGE);
    height = compound.getInteger("h");
    //invo stuff
    NBTTagList tagList = compound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
  }
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    if (isCurrentlyMining && uuid != null) {
      resetProgress(pos);
    }
  }
  private void resetProgress(BlockPos targetPos) {
    if (uuid != null) {
      //BlockPos targetPos = pos.offset(state.getValue(BlockMiner.PROPERTYFACING));
      worldObj.sendBlockBreakProgress(uuid.hashCode(), targetPos, -1);
      curBlockDamage = 0;
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
    case HEIGHT:
      return getHeight();
    default:
      break;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
    case HEIGHT:
      if (value > maxHeight) {
        value = maxHeight;
      }
      setHeight(value);
    default:
      break;
    }
  }
  public int getHeight() {
    return this.height;//this.getField(Fields.HEIGHT.ordinal());
  }
  public void setHeight(int val) {
    this.height = val;
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
}
