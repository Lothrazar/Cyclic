package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;

public class RecipeRegistry {

  public static void setup() {
    RecipeMelter.initAllRecipes();
    RecipeSolidifier.initAllRecipes();
  }
}
