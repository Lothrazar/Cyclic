package com.lothrazar.cyclic.block.crusher;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class CrusherRecipeInput implements RecipeInput {

  private final ItemStack input;

  public CrusherRecipeInput(ItemStack input) {
    this.input = input;
  }

  @Override
  public ItemStack getItem(int index) {
    return index == 0 ? input : ItemStack.EMPTY;
  }

  @Override
  public int size() {
    return 1;
  }
}
