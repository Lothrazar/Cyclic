package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class HydratorFactory implements IRecipeWrapperFactory<RecipeHydrate> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipeHydrate recipe) {
    return new HydratorWrapper(recipe);
  }
}