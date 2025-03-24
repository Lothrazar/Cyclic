package com.lothrazar.cyclic.block.disenchant;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class DisenchantOutputSlot extends SlotItemHandler {

  public DisenchantOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }
}
