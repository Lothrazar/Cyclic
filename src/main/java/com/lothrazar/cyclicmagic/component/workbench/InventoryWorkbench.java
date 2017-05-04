package com.lothrazar.cyclicmagic.component.workbench;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

//to save code just extend vanilla workbench instead of remake
public class InventoryWorkbench extends InventoryCrafting {
  private IInventory tileEntity;
  private Container container;
  public InventoryWorkbench(Container eventHandlerIn, IInventory tileEntity) {
    super(eventHandlerIn, 3, 3);
    this.tileEntity = tileEntity;
    container = eventHandlerIn;
  }
  @Override
  public int getSizeInventory() {
    return 9;
  }
  @Override
  public ItemStack getStackInSlot(int index) {
    return this.tileEntity.getStackInSlot(index);
  }
  /**
   * just like vanilla
   */
  @Override
  public ItemStack decrStackSize(int index, int count) {
    if (this.getStackInSlot(index).isEmpty()) { return ItemStack.EMPTY; }
    ItemStack stack;
    if (this.getStackInSlot(index).getCount() <= count) {
      stack = this.getStackInSlot(index);
      this.setInventorySlotContents(index, ItemStack.EMPTY);
      this.container.onCraftMatrixChanged(this);
      return stack;
    }
    else {
      stack = this.getStackInSlot(index).splitStack(count);
      if (this.getStackInSlot(index).getCount() == 0) {
        this.setInventorySlotContents(index, ItemStack.EMPTY);
      }
      this.container.onCraftMatrixChanged(this);
      return stack;
    }
  }
  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    this.tileEntity.setInventorySlotContents(index, stack);
    this.container.onCraftMatrixChanged(this);
  }
}
