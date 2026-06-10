package com.lothrazar.cyclic.block.melter;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class MelterRecipeInput implements RecipeInput {

  private final ItemStack slot0;

  public MelterRecipeInput(ItemStack slot0) {
    this.slot0 = slot0;
  }

  @Override
  public ItemStack getItem(int index) {
    return index == 0 ? slot0 : ItemStack.EMPTY;
  }

  @Override
  public int size() {
    return 1;
  }
}
