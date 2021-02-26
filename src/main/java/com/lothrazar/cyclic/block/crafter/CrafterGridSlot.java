package com.lothrazar.cyclic.block.crafter;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrafterGridSlot extends SlotItemHandler {

  public CrafterGridSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean isItemValid(ItemStack stack) {
    return true;
  }

  @Override
  public boolean canTakeStack(PlayerEntity playerIn) {
    return true;
  }

  @Override
  public void putStack(ItemStack stack) {
    ItemStack copy = stack.copy();
    copy.setCount(1);
    super.putStack(copy);
    stack.grow(1); //hack for JEI when they auto-fill recipes, re fill it after it drains so its a 'mock slot'
  }
}
