package com.lothrazar.cyclic.block.crafter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrafterOutputSlot extends SlotItemHandler {

  @Override
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    return true;
  }

  @Override
  public void putStack(ItemStack stack) {
    super.putStack(stack);
  }

  public CrafterOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }
}
