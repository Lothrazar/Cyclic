package com.lothrazar.cyclic.recipe;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;

public class CyclicRecipeType<RECIPE_TYPE extends CyclicRecipe> implements IRecipeType<RECIPE_TYPE> {

  public static final CyclicRecipeType<RecipeMelter<TileEntityBase>> MELTER = create("melter");
  private static final List<CyclicRecipeType<? extends CyclicRecipe>> types = new ArrayList<>();

  private static <RECIPE_TYPE extends CyclicRecipe> CyclicRecipeType<RECIPE_TYPE> create(String name) {
    CyclicRecipeType<RECIPE_TYPE> type = new CyclicRecipeType<>(name);
    types.add(type);
    return type;
  }

  private ResourceLocation registryName;

  private CyclicRecipeType(String name) {
    this.registryName = new ResourceLocation(ModCyclic.MODID, name);
  }

  @Override
  public String toString() {
    return registryName.toString();
  }
}
