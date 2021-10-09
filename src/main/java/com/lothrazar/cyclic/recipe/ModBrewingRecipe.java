package com.lothrazar.cyclic.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipe;

/**
 * Thanks to Pull request #1485 by vemerion
 */
public class ModBrewingRecipe extends BrewingRecipe {

  private ItemStack inputStack;

  public ModBrewingRecipe(ItemStack inputStack, Ingredient ingredient, ItemStack output) {
    super(Ingredient.of(inputStack), ingredient, output);
    this.inputStack = inputStack;
  }

  @Override
  public boolean isInput(ItemStack stack) {
    return super.isInput(stack) && PotionUtils.getPotion(stack) == PotionUtils.getPotion(inputStack);
  }
}
