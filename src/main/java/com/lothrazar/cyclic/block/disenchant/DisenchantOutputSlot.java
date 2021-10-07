package com.lothrazar.cyclic.block.disenchant;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class DisenchantOutputSlot extends SlotItemHandler {

  public DisenchantOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }
}
