package com.lothrazar.cyclic.capability;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerIO extends ItemStackHandler {

  private List<Integer> allowedInsert;
  private List<Integer> allowedExtract;

  public ItemStackHandlerIO(int size, List<Integer> allowedIn, List<Integer> allowedOut) {
    super(size);
    this.allowedInsert = allowedIn;
    this.allowedExtract = allowedOut;
    if (allowedInsert == null) {
      allowedInsert = new ArrayList<>();
    }
    if (allowedExtract == null) {
      allowedExtract = new ArrayList<>();
    }
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!allowedInsert.contains(slot)) {
      return stack;
    }
    return super.insertItem(slot, stack, simulate);
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (!allowedExtract.contains(slot)) {
      return ItemStack.EMPTY;
    }
    return super.extractItem(slot, amount, simulate);
  }
}
