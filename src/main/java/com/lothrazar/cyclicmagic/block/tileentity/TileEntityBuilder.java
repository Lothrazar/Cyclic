package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.BlockBuilder;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;// net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class TileEntityBuilder extends TileEntity implements IInventory, ITickable, ISidedInventory {
  private int timer;
  private int buildType;
  private int buildSpeed;
  private int buildSize;
  private int buildHeight = 3;
  private ItemStack[] inv = new ItemStack[9];
  private int shapeIndex = 0;// current index of shape array
  private List<BlockPos> shape = null;
  private BlockPos nextPos;// location of next block to be placed
  private static final int maxSpeed = 1;
  public static int maxSize;
  public static int maxHeight = 10;
  public static final int TIMER_FULL = 100;//one day i will add fuel AND/OR speed upgrades. till then make very slow
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  private static final String NBT_TIMER = "Timer";
  private static final String NBT_NEXTPOS = "Pos";
  private static final String NBT_BUILDTYPE = "build";
  private static final String NBT_SHAPE = "shape";
  private static final String NBT_SPEED = "speed";
  private static final String NBT_SIZE = "size";
  private static final String NBT_SHAPEINDEX = "shapeindex";
  public static enum Fields {
    TIMER, BUILDTYPE, SPEED, SIZE, HEIGHT
  }
  public enum BuildType {
    FACING, SQUARE, CIRCLE;
    public static BuildType getNextType(BuildType btype) {
      int type = btype.ordinal();
      type++;
      if (type > CIRCLE.ordinal()) {
        type = FACING.ordinal();
      }
      return BuildType.values()[type];
    }
  }
  public void rebuildShape() {
    BuildType buildType = getBuildTypeEnum();
    // only rebuild shapes if they are different
    switch (buildType) {
    case CIRCLE:
      this.shape = UtilPlaceBlocks.circle(this.pos, this.getSize() * 2);
      break;
    case FACING:
      this.shape = UtilPlaceBlocks.line(pos, this.getCurrentFacing().getOpposite(), this.getSize());
      break;
    case SQUARE:
      this.shape = UtilPlaceBlocks.squareHorizontalHollow(this.pos, this.getSize());
      break;
    default:
      break;
    }
    if (this.buildHeight > 1) { //first layer is already done, add remaining
      this.shape = UtilPlaceBlocks.repeatShapeByHeight(shape, buildHeight - 1);
    }
    this.shapeIndex = 0;
    if (this.shape.size() > 0)
      this.nextPos = this.shape.get(this.shapeIndex);
  }
  @Override
  public boolean hasCustomName() {
    return false;
  }
  @Override
  public ITextComponent getDisplayName() {
    return null;
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
  public void setInventorySlotContents(int index, ItemStack stack) {
    inv[index] = stack;
    if (stack != null && stack.stackSize > getInventoryStackLimit()) {
      stack.stackSize = getInventoryStackLimit();
    }
  }
  @Override
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public boolean isUseableByPlayer(EntityPlayer player) {
    return true;
  }
  @Override
  public void openInventory(EntityPlayer player) {
  }
  @Override
  public void closeInventory(EntityPlayer player) {
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) != null;
  }
  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
        return timer;
      case BUILDTYPE:
        return this.buildType;
      case SPEED:
        return this.buildSpeed;
      case SIZE:
        return this.buildSize;
      case HEIGHT:
        return this.buildHeight;
      }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount())
      switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
        break;
      case BUILDTYPE:
        this.buildType = value;
        break;
      case SPEED:
        this.buildSpeed = value;
        break;
      case SIZE:
        this.buildSize = value;
        break;
      case HEIGHT:
        this.buildHeight = value;
        break;
      default:
        break;
      }
  }
  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
  }
  public int getHeight() {
    return this.getField(Fields.HEIGHT.ordinal());
  }
  public void setHeight(int value) {
    this.setField(Fields.HEIGHT.ordinal(), value);
  }
  public int getBuildType() {
    return this.getField(Fields.BUILDTYPE.ordinal());
  }
  public void setBuildType(int value) {
    this.setField(Fields.BUILDTYPE.ordinal(), value);
  }
  public BuildType getBuildTypeEnum() {
    int bt = Math.min(this.getBuildType(), BuildType.values().length - 1);
    return BuildType.values()[bt];
  }
  public void setSpeed(int s) {
    if (s <= 0) {
      s = 1;
    }
    if (s >= maxSpeed) {
      s = maxSpeed;
    }
    this.setField(Fields.SPEED.ordinal(), s);
  }
  public int getSpeed() {
    int s = this.getField(Fields.SPEED.ordinal());
    if (s <= 0) {
      s = 1;
    }
    return s;
  }
  public void setSize(int s) {
    if (s <= 0) {
      s = 1;
    }
    if (s >= maxSize) {
      s = maxSize;
    }
    this.setField(Fields.SIZE.ordinal(), s);
  }
  public int getSize() {
    int s = this.getField(Fields.SIZE.ordinal());
    if (s <= 0) {
      s = 1;
    }
    return s;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void clear() {
    // when is this claled? what for?
    for (int i = 0; i < this.inv.length; ++i) {
      this.inv[i] = null;
    }
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
    shapeIndex = tagCompound.getInteger(NBT_SHAPEINDEX);
    nextPos = UtilNBT.stringCSVToBlockPos(tagCompound.getString(NBT_NEXTPOS));// =
    // tagCompound.getInteger(NBT_TIMER);
    if (nextPos == null || (nextPos.getX() == 0 && nextPos.getY() == 0 && nextPos.getZ() == 0)) {
      nextPos = this.pos;// fallback if it fails
    }
    this.shape = new ArrayList<BlockPos>();
    NBTTagList sh = tagCompound.getTagList(NBT_SHAPE, 10);
    for (int i = 0; i < sh.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) sh.getCompoundTagAt(i);
      BlockPos pos = UtilNBT.stringCSVToBlockPos(tag.getString("shapepos"));
      this.shape.add(pos);
    }
    // for(BlockPos p : this.shape){
    //
    // NBTTagCompound tag = new NBTTagCompound();
    // tag.setString("shapepos", UtilNBT.posToStringCSV(p));
    // sh.appendTag(tag);
    // }
    // tagCompound.setTag(NBT_SHAPE, sh);
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.length) {
        inv[slot] = ItemStack.loadItemStackFromNBT(tag);
      }
    }
    this.buildType = tagCompound.getInteger(NBT_BUILDTYPE);
    this.buildSpeed = tagCompound.getInteger(NBT_SPEED);
    this.buildSize = tagCompound.getInteger(NBT_SIZE);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_SHAPEINDEX, this.shapeIndex);
    if (nextPos == null || (nextPos.getX() == 0 && nextPos.getY() == 0 && nextPos.getZ() == 0)) {
      nextPos = this.pos;// fallback if it fails
    }
    tagCompound.setString(NBT_NEXTPOS, UtilNBT.posToStringCSV(this.nextPos));
    NBTTagList sh = new NBTTagList();
    if (this.shape == null) {
      this.shape = new ArrayList<BlockPos>();
    }
    for (BlockPos p : this.shape) {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setString("shapepos", UtilNBT.posToStringCSV(p));
      sh.appendTag(tag);
    }
    tagCompound.setTag(NBT_SHAPE, sh);
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
    tagCompound.setInteger(NBT_BUILDTYPE, this.getBuildType());
    tagCompound.setInteger(NBT_SPEED, this.getSpeed());
    tagCompound.setInteger(NBT_SIZE, this.getSize());
    return super.writeToNBT(tagCompound);
  }
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    // getDescriptionPacket()
    // Gathers data into a packet (S35PacketUpdateTileEntity) that is to be
    // sent to the client. Called on server only.
    NBTTagCompound syncData = new NBTTagCompound();
    this.writeToNBT(syncData);
    return new SPacketUpdateTileEntity(this.pos, 1, syncData);
  }
  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
    // Extracts data from a packet (S35PacketUpdateTileEntity) that was sent
    // from the server. Called on client only.
    this.readFromNBT(pkt.getNbtCompound());
    super.onDataPacket(net, pkt);
  }
  public BlockPos getNextPos() {
    return this.nextPos;
  }
  private void shiftAllUp() {
    for (int i = 0; i < this.getSizeInventory() - 1; i++) {
      shiftPairUp(i, i + 1);
    }
  }
  private void shiftPairUp(int low, int high) {
    ItemStack main = getStackInSlot(low);
    ItemStack second = getStackInSlot(high);
    if (main == null && second != null) { // if the one below this is not
      // empty, move it up
      this.setInventorySlotContents(high, null);
      this.setInventorySlotContents(low, second);
    }
  }
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    shiftAllUp();
    boolean trigger = false;
    if (nextPos == null || (nextPos.getX() == 0 && nextPos.getY() == 0 && nextPos.getZ() == 0)) {
      nextPos = pos;// fallback if it fails
    }
    if (this.worldObj.getStrongPower(this.getPos()) == 0) {
      // it works ONLY if its powered
      markDirty();
      return;
    }
    if (!worldObj.isRemote && nextPos != null && worldObj.rand.nextDouble() < 0.1 && inv[0] != null) {
      UtilParticle.spawnParticlePacket(EnumParticleTypes.DRAGON_BREATH, nextPos, 5);
    }
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.5;
    double z = this.getPos().getZ() + 0.5;
    ItemStack stack = getStackInSlot(0);
    if (stack == null) {
      timer = TIMER_FULL;// reset just like you would in a
      // furnace
    }
    else {
      timer -= this.getSpeed();
      if (timer <= 0) {
        timer = TIMER_FULL;
        trigger = true;
      }
    }
    if (trigger) {
      Block stuff = Block.getBlockFromItem(stack.getItem());
      if (stuff != null) {
        if (worldObj.isRemote == false) {
          //ModMain.logger.info("try place " + this.nextPos + " type " + this.buildType + "_" + this.getBuildTypeEnum().name());
          if (UtilPlaceBlocks.placeStateSafe(worldObj, null, nextPos, stuff.getStateFromMeta(stack.getMetadata()))) {
            this.decrStackSize(0, 1);
          }
        }
        this.incrementPosition();// even if it didnt place.
      }
    }
    else {
      // dont trigger an event, its still processing
      if (worldObj.isRemote && worldObj.rand.nextDouble() < 0.1) {
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.SMOKE_NORMAL, x, y, z);
      }
    }
    this.markDirty();
  }
  private EnumFacing getCurrentFacing() {
    BlockBuilder b = ((BlockBuilder) blockType);
    EnumFacing facing;
    if (b == null || worldObj.getBlockState(pos) == null || b.getFacingFromState(worldObj.getBlockState(pos)) == null)
      facing = EnumFacing.UP;
    else
      facing = b.getFacingFromState(worldObj.getBlockState(this.pos));
    return facing;
  }
  private void incrementPosition() {
    if (this.nextPos == null) {
      this.nextPos = this.pos;
    }
    if (this.worldObj == null) { return; }
    if (this.shape == null || this.shape.size() == 0) {
      this.rebuildShape();
    }
    else {
      int c = shapeIndex + 1;
      if (c < 0 || c >= this.shape.size()) {
        c = 0;
      }
      this.nextPos = this.shape.get(c);
      shapeIndex = c;
    }
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return hopperInput;
  }
  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn);
  }
  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    // do not let hoppers pull out of here for any reason
    return false;// direction == EnumFacing.DOWN;
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
  public String getName() {
    return null;
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
}
