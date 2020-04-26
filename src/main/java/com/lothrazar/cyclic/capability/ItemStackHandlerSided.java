package com.lothrazar.cyclic.capability;

import javax.annotation.Nonnull;
import com.lothrazar.cyclic.block.solidifier.TileSolidifier;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerSided extends ItemStackHandler {

  //TODO
  public ItemStackHandlerSided(int i) {
    super(i);
  }

  @Override
  @Nonnull
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (slot != TileSolidifier.SLOT_OUTPUT) {
      return ItemStack.EMPTY;//do not allow
    }
    return super.extractItem(slot, amount, simulate);
  }
}
