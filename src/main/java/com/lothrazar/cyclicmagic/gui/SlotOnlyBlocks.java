package com.lothrazar.cyclicmagic.gui;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOnlyBlocks extends Slot {
  public SlotOnlyBlocks(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack itemstack) {
    return Block.getBlockFromItem(itemstack.getItem()) != null; // no items only blocks
  }
  @Override
  public void onSlotChanged() {
    if (this.getHasStack() && this.getStack().stackSize == 0) {
      this.putStack((ItemStack) null);
    }
    super.onSlotChanged();
  }
}
