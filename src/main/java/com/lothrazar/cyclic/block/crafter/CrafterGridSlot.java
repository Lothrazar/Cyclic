package com.lothrazar.cyclic.block.crafter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrafterGridSlot extends SlotItemHandler {

  public CrafterGridSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return true;
  }

  @Override
  public boolean mayPickup(Player playerIn) {
    return false;
  }

  @Override
  public void set(ItemStack stack) {
    ItemStack copy = stack.copy();
    copy.setCount(1);
    super.set(copy);
    stack.grow(1); //hack for JEI when they auto-fill recipes, re fill it after it drains so its a 'mock slot'
  }
}
