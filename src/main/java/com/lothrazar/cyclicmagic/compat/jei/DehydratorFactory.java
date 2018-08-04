package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.dehydrator.RecipeDeHydrate;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class DehydratorFactory implements IRecipeWrapperFactory<RecipeDeHydrate> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipeDeHydrate recipe) {
    return new DehydratorWrapper(recipe);
  }
}