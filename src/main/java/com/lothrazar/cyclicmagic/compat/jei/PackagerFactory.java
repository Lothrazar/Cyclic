package com.lothrazar.cyclicmagic.compat.jei;

import com.lothrazar.cyclicmagic.block.packager.RecipePackage;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

public class PackagerFactory implements IRecipeWrapperFactory<RecipePackage> {

  @Override
  public IRecipeWrapper getRecipeWrapper(RecipePackage recipe) {
    return new PackagerWrapper(recipe);
  }
}