package com.lothrazar.cyclicmagic.gui.slot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;

public class SlotPotion extends Slot {
  public SlotPotion(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack stack) {
    return stack.getItem() instanceof ItemPotion;
  }
  @Override
  public int getSlotStackLimit() {
    return 1;
  }
}
