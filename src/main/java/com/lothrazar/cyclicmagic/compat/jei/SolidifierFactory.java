package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.solidifier.RecipeSolidifier;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class SolidifierFactory implements IRecipeWrapperFactory<RecipeSolidifier> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipeSolidifier recipe) {
    return new SolidifierWrapper(recipe);
  }
}