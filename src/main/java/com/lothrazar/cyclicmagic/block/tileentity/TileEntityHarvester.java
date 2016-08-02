package com.lothrazar.cyclicmagic.block.tileentity;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.BlockHarvester;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilHarvestCrops.HarestCropsConfig;
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

public class TileEntityHarvester extends TileEntity implements IInventory, ITickable, ISidedInventory {
  private int timer;
  private int buildSize;
  public static int maxSize;
  public static int maxHeight = 10;
  public static final int TIMER_FULL = 100;//one day i will add fuel AND/OR speed upgrades. till then make very slow
  private int[] hopperInput = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };// all slots

  private static final String NBT_TIMER = "Timer";
  private static final String NBT_SIZE = "size";
  public static enum Fields {
    TIMER, SIZE
  }
  public TileEntityHarvester() {
    this.buildSize = 10;
    this.timer = TIMER_FULL;
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
      case SIZE:
        return this.buildSize;
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
      case SIZE:
        this.buildSize = value;
        break;
      default:
        break;
      }
  }
  public int getTimer() {
    return this.getField(Fields.TIMER.ordinal());
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
  }
  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    timer = tagCompound.getInteger(NBT_TIMER); 
    this.buildSize = tagCompound.getInteger(NBT_SIZE);
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);

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
      timer = TIMER_FULL;
      System.out.println("harv trigger :"+buildSize);
      

      HarestCropsConfig conf = getHarvestConf();
      
      UtilHarvestCrops.harvestArea(this.worldObj, this.getPos(), this.buildSize, conf);
//      Block stuff = Block.getBlockFromItem(stack.getItem());
//      if (stuff != null) {
//        if (this.worldObj.isRemote == false) {
//          //ModMain.logger.info("try place " + this.nextPos + " type " + this.buildType + "_" + this.getBuildTypeEnum().name());
//          if (UtilPlaceBlocks.placeStateSafe(this.worldObj, null, this.nextPos, stuff.getStateFromMeta(stack.getMetadata()))) {
//            this.decrStackSize(0, 1);
//          }
//        }
//        this.incrementPosition();// even if it didnt place.
//      }
    }
    else {
      // dont trigger an event, its still processing
      if (this.worldObj.isRemote && this.worldObj.rand.nextDouble() < 0.1) {
        UtilParticle.spawnParticle(worldObj, EnumParticleTypes.SMOKE_NORMAL, x, y, z);
      }
    }
    this.markDirty();
  }

  public HarestCropsConfig getHarvestConf() {
    //TODO: REAL SAVE
    HarestCropsConfig conf = new HarestCropsConfig();
    conf.doesPumpkinBlocks = true;
    conf.doesMelonBlocks = true;
    conf.doesCrops = true;
    conf.doesCactus = true;
    conf.doesReeds = true;
    conf.doesFlowers = true;
    conf.doesMushroom = true;
    conf.doesTallgrass = true;
    conf.doesSapling = true;
    conf.doesLeaves = true;
    return conf;
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
      facing = b.getFacingFromState(this.worldObj.getBlockState(this.pos));
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
