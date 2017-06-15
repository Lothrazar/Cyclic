package com.lothrazar.cyclicmagic.gui.slot;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOnlyItems extends Slot {
  public SlotOnlyItems(IInventory inventoryIn, int index, int xPosition, int yPosition) {
    super(inventoryIn, index, xPosition, yPosition);
  }
  @Override
  public boolean isItemValid(ItemStack itemstack) {
    return Block.getBlockFromItem(itemstack.getItem()) == null
        || Block.getBlockFromItem(itemstack.getItem()) == Blocks.AIR; // no items only blocks
  }
}
