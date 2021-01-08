package com.lothrazar.cyclic.capability;

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerWrapper extends ItemStackHandler {

  private ItemStackHandler output;
  private ItemStackHandler input;

  public ItemStackHandlerWrapper(ItemStackHandler input, ItemStackHandler output) {
    super(input.getSlots() + output.getSlots());
    this.input = input;
    this.output = output;
  }

  @Override
  @Nonnull
  public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
    if (slot >= input.getSlots()) {
      return stack;
    }
    return input.insertItem(slot, stack, simulate);
  }

  @Override
  @Nonnull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (slot >= output.getSlots()) {
      return ItemStack.EMPTY;
    }
    return output.extractItem(slot, amount, simulate);
  }
}
