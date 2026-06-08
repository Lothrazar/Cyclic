package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

/**
 * "Fake" slot used by filter data cards. Holds a copy of the displayed item with count=1 only;
 * never accepts a real transfer from the player and never gives anything back.
 * <p>
 * This exists because filter_data / fluid_data stack to 64 and persist their backing
 * {@code ItemStackHandler} into {@code CUSTOM_DATA}. Components copy on stack split, so if the
 * slots held real items a stack of 64 cards could trivially dupe (or void) those items. With ghost
 * semantics there are no real items in the backing handler - just visual references to item types.
 * <p>
 * Modelled on {@code CrafterGridSlot}.
 */
public class GhostFilterSlot extends SlotItemHandler {

  public GhostFilterSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
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
    if (!copy.isEmpty()) {
      copy.setCount(1);
    }
    super.set(copy);
    //hack for JEI auto-fill: refill the cursor stack we just "drained"
    if (!stack.isEmpty()) {
      stack.grow(1);
    }
  }
}
