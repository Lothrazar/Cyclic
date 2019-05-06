package com.lothrazar.cyclicmagic.compat.jei;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.block.dehydrator.RecipeDeHydrate;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class DehydratorWrapper implements IRecipeWrapper {

  private RecipeDeHydrate recipe;

  public DehydratorWrapper(RecipeDeHydrate source) {
    this.recipe = source;
  }

  public ItemStack getOut() {
    return recipe.getRecipeOutput();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> ing = new ArrayList<ItemStack>();
    ing.add(recipe.getRecipeInput().copy());
    ingredients.setInputs(VanillaTypes.ITEM, ing);
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    ingredients.setOutput(VanillaTypes.FLUID, recipe.getOutputFluid());
  }

  public RecipeDeHydrate getRecipe() {
    return recipe;
  }
}