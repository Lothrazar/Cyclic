package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.block.BlockHarvester;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilWorld;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;// net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

public class TileEntityHarvester extends TileEntity implements IInventory, ITickable, ISidedInventory {
  private int timer;
  public static final int TIMER_FULL = 80;
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots
  private HarestCropsConfig conf;
  private static final String NBT_TIMER = "Timer";
  private static final int HARVEST_RADIUS = 8;
  public static enum Fields {
    TIMER
  }
  public TileEntityHarvester() {
    this.timer = TIMER_FULL;
    conf = new HarestCropsConfig();
    conf.doesCrops = true;
    conf.doesMushroom = true;
    conf.doesPumpkinBlocks = true;
    conf.doesMelonBlocks = true;
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
    return 0;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return null;
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
      default:
        break;
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
      default:
        break;
      }
  }
  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
  }
  public void setHarvestConf(HarestCropsConfig c) {
    conf = c;
  }
  public HarestCropsConfig getHarvestConf() {
    return conf;
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void clear() {
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
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
  public boolean isBurning() {
    return this.timer > 0 && this.timer < TIMER_FULL;
  }
  @Override
  public void update() {
    if (this.worldObj.getStrongPower(this.getPos()) == 0) {
      // it works ONLY if its powered
      this.markDirty();
      return;
    }
    boolean trigger = false;
    // center of the block
    double x = this.getPos().getX() + 0.5;
    double y = this.getPos().getY() + 0.5;
    double z = this.getPos().getZ() + 0.5;
    timer -= this.getSpeed();
    if (timer <= 0) {
      timer = TIMER_FULL;
      trigger = true;
    }
    if (trigger) {
      BlockPos harvest = getHarvestPos();
      //TODO:spit drops out the facing side, just like uncrafter
      // -> to this end, add new conf flag
      if (UtilHarvestCrops.harvestSingle(this.worldObj, harvest, conf)) {
//        ModMain.logger.info("harvested :" + UtilChat.blockPosToString(harvest));
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.DRAGON_BREATH, harvest);
        timer = TIMER_FULL;//harvest worked!
      }
      else {
//        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.SMOKE_NORMAL, harvest);
        timer = 3;//harvest didnt work, try again really quick
      }
    }
    else {
      // dont trigger an event, its still processing
      if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1) {
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.SMOKE_NORMAL, x, y, z);
      }
    }
    this.markDirty();
  }
//  private BlockPos getOutputPos() {
//    BlockPos harvest = this.getPos().offset(this.getCurrentFacing());
//    return harvest;
//  }
  private BlockPos getHarvestPos() {
    //move center over that much, not including exact horizontal
    BlockPos center = this.getPos().offset(this.getCurrentFacing(),HARVEST_RADIUS+1);
    return UtilWorld.getRandomPos(this.worldObj.rand, center, HARVEST_RADIUS);
  }
  private int getSpeed() {
    return 1;
  }
  private EnumFacing getCurrentFacing() {
    BlockHarvester b = ((BlockHarvester) this.blockType);
    EnumFacing facing;
    if (b == null || this.worldObj.getBlockState(this.pos) == null || b.getFacingFromState(this.worldObj.getBlockState(this.pos)) == null)
      facing = EnumFacing.UP;
    else
      facing = b.getFacingFromState(this.worldObj.getBlockState(this.pos)).getOpposite();
    return facing;
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
