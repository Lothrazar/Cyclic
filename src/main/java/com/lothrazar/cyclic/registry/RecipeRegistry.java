package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.melter.RecipeMelter;

public class RecipeRegistry {

  public static void setup() {
    RecipeMelter.initAllRecipes();
  }
}
