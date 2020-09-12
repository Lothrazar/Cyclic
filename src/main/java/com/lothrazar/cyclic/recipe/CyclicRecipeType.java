package com.lothrazar.cyclic.recipe;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import net.minecraft.item.crafting.IRecipeType;

public class CyclicRecipeType<RECIPE_TYPE extends CyclicRecipe> implements IRecipeType<RECIPE_TYPE> {

  public static final CyclicRecipeType<RecipeMelter<TileEntityBase>> SOLID = create("solidifier");
  public static final CyclicRecipeType<RecipeMelter<TileEntityBase>> MELTER = create("melter");
  //private static final List<CyclicRecipeType<? extends CyclicRecipe>> types = new ArrayList<>();

  private static <RECIPE_TYPE extends CyclicRecipe> CyclicRecipeType<RECIPE_TYPE> create(String name) {
    CyclicRecipeType<RECIPE_TYPE> type = new CyclicRecipeType<>(name);
    // types.add(type);
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
