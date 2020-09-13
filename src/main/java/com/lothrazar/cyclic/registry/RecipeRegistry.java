package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent.Register;

public class RecipeRegistry {

  @Deprecated
  public static void legacy() {
    //    RecipeMelter.initAllRecipes();
    RecipeSolidifier.initAllRecipes();
  }

  public static void registerRecipeSerializers(Register<IRecipeSerializer<?>> event) {
    //    ModCyclic.LOGGER.info("Registry.RECIPE_TYPE    " + Registry.RECIPE_TYPE);
    //    ModCyclic.LOGGER.info(" CyclicRecipeType.MELTER_ID   " + CyclicRecipeType.MELTER.toString());
    //    ModCyclic.LOGGER.info("  CyclicRecipeType.MELTER " + CyclicRecipeType.MELTER);
    Registry.register(Registry.RECIPE_TYPE,
        CyclicRecipeType.MELTER.toString(), CyclicRecipeType.MELTER);
    event.getRegistry().register(RecipeMelter.SERIALIZER);
  }
}
