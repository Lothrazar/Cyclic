package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.melter.RecipeMelter;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class MelterFactory implements IRecipeWrapperFactory<RecipeMelter> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipeMelter recipe) {
    return new MelterWrapper(recipe);
  }
}