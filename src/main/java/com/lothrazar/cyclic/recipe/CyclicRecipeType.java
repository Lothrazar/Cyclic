package com.lothrazar.cyclic.recipe;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import net.minecraft.item.crafting.IRecipeType;

public class CyclicRecipeType<RECIPE_TYPE extends CyclicRecipe> implements IRecipeType<RECIPE_TYPE> {

  public static final CyclicRecipeType<RecipeSolidifier<TileEntityBase>> SOLID = create("solidifier");
  public static final CyclicRecipeType<RecipeMelter<TileEntityBase>> MELTER = create("melter");

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
