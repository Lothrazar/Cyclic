package com.lothrazar.cyclicmagic.block.base;
import java.util.stream.IntStream;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.gui.ITileFuel;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public abstract class TileEntityBaseMachineInvo extends TileEntityBaseMachine implements IInventory, ISidedInventory, ITileFuel {
  protected static final int SPEED_FUELED = 8;
  /**
   * one second of Fuel Burn Time gives 50 RF this was computed since 1 coal item has 1600 burn time and 1 coal item also gives 80,000 RF (max output based on survey of mods) so i want to make
   * internal consumption of fuel more efficient than just getting raw power especially because actual RF mods will have better power gen anyway
   */
  private static final int FUEL_FACTOR = 50;
  private static final int MAX_SPEED = 10;
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  public static final String NBT_TIMER = "Timer";
  public static final String NBT_REDST = "redstone";
  public static final String NBT_SIZE = "size";
  public static final String NBT_FUEL = "fuel";
  public static final String NBTPLAYERID = "uuid";
  public static final String NBT_SPEED = "speed";
  public static final String NBT_RENDER = "render";
  public static final String NBT_TANK = "tankwater";
  private static final String NBT_ENERGY = "ENERGY";
  protected NonNullList<ItemStack> inv;
  protected int fuelDisplay = 0;
  private int fuelCost = 0;
  private int fuelSlot = -1;
  protected int speed = 1;
  protected int timer;
  public TileEntityBaseMachineInvo(int invoSize) {
    super();
    inv = NonNullList.withSize(invoSize, ItemStack.EMPTY);
    this.fuelSlot = -1;
  }
  protected void setFuelSlot(int slot, int fcost) {
    if (fcost > 0) {
      this.fuelSlot = slot;
      this.fuelCost = fcost;
    }
  }
  public int getFuelMax() {
    this.initEnergyStorage();
    return this.energyStorage.getMaxEnergyStored();
  }
  public int getFuelCurrent() {
    this.initEnergyStorage();
    return this.energyStorage.getEnergyStored();
  }
  protected void setFuelCurrent(int f) {
    this.initEnergyStorage();
    this.energyStorage.setEnergyStored(f);
  }
  public double getPercentFormatted() {
    if (this.getFuelMax() == 0) {
      return 0;
    }
    double percent = ((float) this.getFuelCurrent() / (float) this.getFuelMax());
    double pctOneDecimal = Math.floor(percent * 1000) / 10;
    return pctOneDecimal;
  }
 
  public boolean doesUseFuel() {
    return this.fuelCost > 0;
  }
  public int getFuelCost() {
    return this.fuelCost;
  }
  public void consumeFuel() {
    if (doesUseFuel()) {
      if (this.getFuelCurrent() >= this.getFuelCost()) {
        this.energyStorage.extractEnergy(this.getFuelCost(), false);
      }
      ItemStack itemstack = this.getStackInSlot(this.fuelSlot);
      //pull in item from fuel slot, if it has fuel burn time
      int fuelFromStack = FUEL_FACTOR * TileEntityFurnace.getItemBurnTime(itemstack);
      if (fuelFromStack > 0 && this.energyStorage.emptyCapacity() >= fuelFromStack) {
        int newEnergy = Math.min(this.getFuelMax(), this.getFuelCurrent() + fuelFromStack);
        this.energyStorage.setEnergyStored(newEnergy);
        if (itemstack.getItem() instanceof ItemBucket && itemstack.getCount() == 1) {
          this.setInventorySlotContents(this.fuelSlot, new ItemStack(Items.BUCKET));
        }
        else {
          itemstack.shrink(1);
        }
      }
      //what if item in fuel slot is an RF battery type item? start draining it ya
      if (itemstack.hasCapability(CapabilityEnergy.ENERGY, null)) {
        IEnergyStorage storage = itemstack.getCapability(CapabilityEnergy.ENERGY, null);
        if (storage != null && storage.getEnergyStored() > 0) {
          int canWithdraw = Math.min(EnergyStore.DEFAULT_FLOW, storage.getEnergyStored());
          if (canWithdraw > 0 && this.getFuelCurrent() + canWithdraw <= this.getFuelMax()) {
            storage.extractEnergy(canWithdraw, false);
            this.setFuelCurrent(this.getFuelCurrent() + canWithdraw);
          }
        }
      }
    }
  }
  public int[] getFieldArray(int length) {
    return IntStream.rangeClosed(0, length - 1).toArray();
  }
  @Override
  public boolean isRunning() {
    if (this.doesUseFuel()) {
      // update from power cables/batteries next door
      this.updateIncomingEnergy();
    }
    return super.isRunning();
  }
  public boolean updateFuelIsBurning() {
    if (this.isRunning() && doesUseFuel() && hasEnoughFuel()) {
      this.consumeFuel();
      return this.hasEnoughFuel();
    }
    return true;
  }
  /**
   * look for connected energy-compatble blocks and try to drain
   * 
   * Basically all of this function was written by @Ellpeck and then I tweaked it to fit my needs
   * https://github.com/Ellpeck/ActuallyAdditions/blob/9bed6f7ea59e8aa23fa3ba540d92cd61a04dfb2f/src/main/java/de/ellpeck/actuallyadditions/mod/util/WorldUtil.java#L151
   */
  private void updateIncomingEnergy() {
    TileEntity teConnected;
    //check every side to see if I'm connected
    for (EnumFacing side : EnumFacing.values()) {
      //it would output energy on the opposite side 
      EnumFacing sideOpp = side.getOpposite();
      teConnected = world.getTileEntity(pos.offset(side));
      if (teConnected != null &&
          teConnected.hasCapability(CapabilityEnergy.ENERGY, sideOpp)) {
        //pull energy to myself, from the next one over if it has energy
        IEnergyStorage handlerTo = this.getCapability(CapabilityEnergy.ENERGY, side);
        IEnergyStorage handlerFrom = teConnected.getCapability(CapabilityEnergy.ENERGY, sideOpp);
        if (handlerFrom != null && handlerTo != null) {
          //true means simulate the extract. then if it worked go for real
          int drain = handlerFrom.extractEnergy(EnergyStore.DEFAULT_FLOW, true);
          if (drain > 0) {
            int filled = handlerTo.receiveEnergy(drain, false);
            handlerFrom.extractEnergy(filled, false);
            return;// stop now because only pull from one side at a time
          }
        }
      }
    }
  }
  @Override
  protected void spawnParticlesAbove() {
    //turn off when its off
    if (this.isRunning() ) {//&& this.hasEnoughFuel()
      super.spawnParticlesAbove();
    }
  }
  @Override
  public boolean hasEnoughFuel() {
    if (doesUseFuel() == false) {
      return true;
    }
    return this.getFuelCurrent() >= this.getFuelCost();
  }
  protected boolean updateTimerIsZero() {
    timer -= this.getSpeed();
    if (timer < 0) {
      timer = 0;
    }
    return timer == 0;
  }
  @Override
  public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
    return this.isItemValidForSlot(index, itemStackIn);
  }
  @Override
  public String getName() {
    if (this.getBlockType() == null) {
      ModCyclic.logger.error(" null blockType:" + this.getClass().getName());
      return "";
    }
    return this.getBlockType().getUnlocalizedName() + ".name";
  }
  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return true;
  }
  @Override
  public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
    return true;
  }
  @Override
  public int getField(int id) {
    return 0;
  }
  @Override
  public void setField(int id, int value) {}
  @Override
  public int getFieldCount() {
    return 0;
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
  public int getInventoryStackLimit() {
    return 64;
  }
  @Override
  public void openInventory(EntityPlayer player) {}
  @Override
  public void closeInventory(EntityPlayer player) {}
  @Override
  public void clear() {
    for (int i = 0; i < this.inv.size(); ++i) {
      inv.set(i, ItemStack.EMPTY);
    }
  }
  protected void shiftAllUp() {
    shiftAllUp(0);
  }
  /**
   * pass in how many slots on the end ( right ) to skip
   * 
   * @param endOffset
   */
  protected void shiftAllUp(int endOffset) {
    for (int i = 0; i < this.getSizeInventory() - endOffset - 1; i++) {
      shiftPairUp(i, i + 1);
    }
  }
  protected void shiftPairUp(int low, int high) {
    ItemStack main = getStackInSlot(low);
    ItemStack second = getStackInSlot(high);
    if (main.isEmpty() && !second.isEmpty()) { // if the one below this is not
      // empty, move it up
      this.setInventorySlotContents(high, ItemStack.EMPTY);
      this.setInventorySlotContents(low, second);
    }
  }
  @Override
  public int getSizeInventory() {
    return inv.size();
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    if (index < 0 || index >= getSizeInventory()) {
      return ItemStack.EMPTY;
    }
    return inv.get(index);
  }
  public ItemStack decrStackSize(int index) {
    return this.decrStackSize(index, 1);
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (!stack.isEmpty()) {
      if (stack.getMaxStackSize() <= count) {
        setInventorySlotContents(index, ItemStack.EMPTY);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.getMaxStackSize() == 0) {
          setInventorySlotContents(index, ItemStack.EMPTY);
        }
      }
    }
    return stack;
  }
  @Override
  public ItemStack removeStackFromSlot(int index) {
    ItemStack stack = getStackInSlot(index);
    setInventorySlotContents(index, ItemStack.EMPTY);
    return stack;
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    if (stack == null) {
      stack = ItemStack.EMPTY;
    }
    if (!stack.isEmpty() && stack.getMaxStackSize() > getInventoryStackLimit()) {
      stack.setCount(getInventoryStackLimit());
    }
    inv.set(index, stack);
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] {};
  }
  @Override
  public boolean isEmpty() {
    return false;
  }
  @Override
  public boolean isUsableByPlayer(EntityPlayer player) {
    return true;
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.readInvoFromNBT(compound);
    timer = compound.getInteger(NBT_TIMER);
    speed = compound.getInteger(NBT_SPEED);
    fuelDisplay = compound.getInteger("fueldisplay");
    this.setFuelCurrent(compound.getInteger(NBT_FUEL));
    this.initEnergyStorage();
    if (energyStorage != null && doesUseFuel() && compound.hasKey(NBT_ENERGY)) {
      CapabilityEnergy.ENERGY.readNBT(energyStorage, null, compound.getTag(NBT_ENERGY));
    }
    super.readFromNBT(compound);
  }
  private void readInvoFromNBT(NBTTagCompound tagCompound) {
    NBTTagList tagList = tagCompound.getTagList(NBT_INV, 10);
    for (int i = 0; i < tagList.tagCount(); i++) {
      NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
      byte slot = tag.getByte(NBT_SLOT);
      if (slot >= 0 && slot < inv.size()) {
        inv.set(slot, UtilNBT.itemFromNBT(tag));
      }
    }
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    this.writeInvoToNBT(compound);
    compound.setInteger(NBT_SPEED, speed);
    compound.setInteger(NBT_FUEL, getFuelCurrent());
    compound.setInteger(NBT_TIMER, timer);
    compound.setInteger("fueldisplay", fuelDisplay);
    this.initEnergyStorage();
    if (energyStorage != null && doesUseFuel()) {
      compound.setTag(NBT_ENERGY, CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
    }
    return super.writeToNBT(compound);
  }
  private void writeInvoToNBT(NBTTagCompound compound) {
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.size(); i++) {
      ItemStack stack = inv.get(i);
      if (!stack.isEmpty()) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setByte(NBT_SLOT, (byte) i);
        stack.writeToNBT(tag);
        itemList.appendTag(tag);
      }
    }
    compound.setTag(NBT_INV, itemList);
  }
  @Override
  public boolean receiveClientEvent(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      this.setField(id, value);
      return true;
    }
    else {
      return super.receiveClientEvent(id, value);
    }
  }
  public ItemStack tryMergeStackIntoSlot(ItemStack held, int furnaceSlot) {
    ItemStack current = this.getStackInSlot(furnaceSlot);
    boolean success = false;
    if (current.isEmpty()) {
      this.setInventorySlotContents(furnaceSlot, held);
      held = ItemStack.EMPTY;
      success = true;
    }
    else if (held.isItemEqual(current)) {
      success = true;
      UtilItemStack.mergeItemsBetweenStacks(held, current);
    }
    if (success) {
      if (held != ItemStack.EMPTY && held.getMaxStackSize() == 0) {// so now we just fix if something is size zero
        held = ItemStack.EMPTY;
      }
      this.markDirty();
    }
    return held;
  }
  public int[] getFieldOrdinals() {
    return new int[0];
  }
  public int getSpeed() {
    if (this.doesUseFuel() == false) {
      return this.speed;// does not use fuel. use NBT saved speed value
    }
    else {
      if (this.getFuelCurrent() == 0) {
        return 0; // do not run without fuel
      }
      else {
        return SPEED_FUELED;// i have fuel, use what it says eh
      }
    }
  }
  public void setSpeed(int value) {
    if (value < 0) {
      value = 0;
    }
    speed = Math.min(value, MAX_SPEED);
  }
  public void incrementSpeed() {
    this.setSpeed(this.getSpeed() - 1);
  }
  public void decrementSpeed() {
    this.setSpeed(this.getSpeed() + 1);
  }
  //Vanilla Furnace has this -> makes it works with some modded pipes such as EXU2
  net.minecraftforge.items.IItemHandler handlerTop = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.UP);
  net.minecraftforge.items.IItemHandler handlerBottom = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.DOWN);
  net.minecraftforge.items.IItemHandler handlerSide = new net.minecraftforge.items.wrapper.SidedInvWrapper(this, net.minecraft.util.EnumFacing.WEST);
  private EnergyStore energyStorage;
  @Override
  public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, EnumFacing facing) {
    if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return true;
    }
    if (doesUseFuel() && capability == CapabilityEnergy.ENERGY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @SuppressWarnings("unchecked")
  @Override
  public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.util.EnumFacing facing) {
    if (facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      if (facing == EnumFacing.DOWN)
        return (T) handlerBottom;
      else if (facing == EnumFacing.UP)
        return (T) handlerTop;
      else
        return (T) handlerSide;
    }
    if (doesUseFuel() && capability == CapabilityEnergy.ENERGY) {
      this.initEnergyStorage();
      return CapabilityEnergy.ENERGY.cast(energyStorage);
    }
    //    
    return super.getCapability(capability, facing);
  }
  public void initEnergyStorage() {
    if (energyStorage == null)
      energyStorage = new EnergyStore();
  }
  @Override
  public void toggleFuelDisplay() {
    this.fuelDisplay = (this.fuelDisplay + 1) % 2;
  }
  @Override
  public boolean getFuelDisplay() {
    return this.fuelDisplay == 0;
  }
}
