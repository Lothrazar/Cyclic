package com.lothrazar.cyclicmagic.gui;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOnlyEnchanted extends Slot {
  public SlotOnlyEnchanted(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack itemstack) {
    return EnchantmentHelper.getEnchantments(itemstack).size() > 0;
  }
}
