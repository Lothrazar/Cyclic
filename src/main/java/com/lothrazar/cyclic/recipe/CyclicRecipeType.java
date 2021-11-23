package com.lothrazar.cyclic.recipe;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import net.minecraft.world.item.crafting.RecipeType;

public class CyclicRecipeType<RECIPE_TYPE extends CyclicRecipe> implements RecipeType<RECIPE_TYPE> {

  public static final CyclicRecipeType<RecipeSolidifier<TileBlockEntityCyclic>> SOLID = create("solidifier");
  public static final CyclicRecipeType<RecipeMelter<TileBlockEntityCyclic>> MELTER = create("melter");
  public static final CyclicRecipeType<RecipeCrusher<TileBlockEntityCyclic>> CRUSHER = create("crusher");
  public static final CyclicRecipeType<RecipeGeneratorItem<TileBlockEntityCyclic>> GENERATOR_ITEM = create("generator_item");
  public static final CyclicRecipeType<RecipeGeneratorFluid<TileBlockEntityCyclic>> GENERATOR_FLUID = create("generator_fluid");

  private static <RECIPE_TYPE extends CyclicRecipe> CyclicRecipeType<RECIPE_TYPE> create(String name) {
    CyclicRecipeType<RECIPE_TYPE> type = new CyclicRecipeType<>(name);
    return type;
  }

  private String registryName;

  private CyclicRecipeType(String name) {
    this.registryName = name;
  }

  @Override
  public String toString() {
    return ModCyclic.MODID + ":" + registryName;
  }
}
