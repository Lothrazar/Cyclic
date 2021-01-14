package com.lothrazar.cyclic.block.disenchant;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DisenchantOutputSlot extends SlotItemHandler {

  public DisenchantOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }
}
