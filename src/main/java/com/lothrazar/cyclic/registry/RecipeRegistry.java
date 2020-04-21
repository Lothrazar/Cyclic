package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.recipe.RecipeMelter;

public class RecipeRegistry {

  public static void setup() {
    RecipeMelter.initAllRecipes();
  }
}
