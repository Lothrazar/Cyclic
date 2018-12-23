package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.packager.RecipePackager;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class PackagerFactory implements IRecipeWrapperFactory<RecipePackager> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipePackager recipe) {
    return new PackagerWrapper(recipe);
  }
}