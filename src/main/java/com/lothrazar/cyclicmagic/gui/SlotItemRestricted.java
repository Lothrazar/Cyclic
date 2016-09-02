package com.lothrazar.cyclicmagic.gui;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotItemRestricted extends Slot {
  private Item filter;
  public SlotItemRestricted(IInventory inventoryIn, int index, int xPosition, int yPosition, Item filt) {
    super(inventoryIn, index, xPosition, yPosition);
    filter = filt;
  }
  @Override
  public boolean isItemValid(ItemStack itemstack) {
    return itemstack.getItem() == filter;
  }
}
