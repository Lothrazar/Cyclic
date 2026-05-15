package com.lothrazar.cyclic.block.melter;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public class MelterRecipeInput implements RecipeInput {

  private final ItemStack slot0;
  private final ItemStack slot1;

  public MelterRecipeInput(ItemStack slot0, ItemStack slot1) {
    this.slot0 = slot0;
    this.slot1 = slot1 != null ? slot1 : ItemStack.EMPTY;
  }

  @Override
  public ItemStack getItem(int index) {
    return switch (index) {
      case 0 -> slot0;
      case 1 -> slot1;
      default -> ItemStack.EMPTY;
    };
  }

  @Override
  public int size() {
    return 2;
  }
}
