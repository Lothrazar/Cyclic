package com.lothrazar.cyclicmagic.block.imbue;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.NonNullList;

public class RecipeImbue {

  private NonNullList<ItemStack> recipeInput = NonNullList.withSize(2, ItemStack.EMPTY);
  private Potion potion;
  private int charges;

  public RecipeImbue(ItemStack[] in, Potion potion, int charges) {
    if (in.length != 2) {
      throw new IllegalArgumentException("Input array must be length 2");
    }
    recipeInput.set(0, in[0]);
    recipeInput.set(1, in[1]);
    this.potion = potion;
    this.charges = charges;
  }
}
