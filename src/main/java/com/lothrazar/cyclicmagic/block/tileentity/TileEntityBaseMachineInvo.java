package com.lothrazar.cyclicmagic.block.tileentity;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

public class TileEntityBaseMachineInvo extends TileEntityBaseMachine implements IInventory, ISidedInventory {
  private static final String NBT_INV = "Inventory";
  private static final String NBT_SLOT = "Slot";
  protected NonNullList<ItemStack> inv;
  public TileEntityBaseMachineInvo(int invoSize) {
    super();
    inv = NonNullList.withSize(invoSize, ItemStack.EMPTY);
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
    for (int i = 0; i < this.getSizeInventory() - 1; i++) {
      shiftPairUp(i, i + 1);
    }
  }
  protected void shiftPairUp(int low, int high) {
    ItemStack main = getStackInSlot(low);
    ItemStack second = getStackInSlot(high);
    if (main == null && second != null) { // if the one below this is not
      // empty, move it up
      this.setInventorySlotContents(high, null);
      this.setInventorySlotContents(low, second);
    }
  }
  @Override
  public int getSizeInventory() {
    return inv.size();
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return inv.get(index);
  }
  @Override
  public ItemStack decrStackSize(int index, int count) {
    ItemStack stack = getStackInSlot(index);
    if (stack != null) {
      if (stack.getMaxStackSize() <= count) {
        setInventorySlotContents(index, null);
      }
      else {
        stack = stack.splitStack(count);
        if (stack.getMaxStackSize() == 0) {
          setInventorySlotContents(index, null);
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
    if (stack != null && stack.getMaxStackSize() > getInventoryStackLimit()) {
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
    return super.writeToNBT(compound);
  }
  private void writeInvoToNBT(NBTTagCompound compound) {
    NBTTagList itemList = new NBTTagList();
    for (int i = 0; i < inv.size(); i++) {
      ItemStack stack = inv.get(i);
      if (stack != null) {
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
    else{
      return super.receiveClientEvent(id, value);
    }
  }
}
