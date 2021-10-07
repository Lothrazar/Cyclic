package com.lothrazar.cyclic.block.crafter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrafterOutputSlot extends SlotItemHandler {

  @Override
  public boolean mayPlace(ItemStack stack) {
    return false;
  }

  @Override
  public boolean mayPickup(Player playerIn) {
    return true;
  }

  @Override
  public void set(ItemStack stack) {
    super.set(stack);
  }

  public CrafterOutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
    super(itemHandler, index, xPosition, yPosition);
  }
}
