package com.lothrazar.cyclicmagic.gui.slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotUncraft extends Slot {
  public SlotUncraft(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack stack) {
    return true;
  }
}
