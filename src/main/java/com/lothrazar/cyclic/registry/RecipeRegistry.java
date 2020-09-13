package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent.Register;

public class RecipeRegistry {

  public static void registerRecipeSerializers(Register<IRecipeSerializer<?>> event) {
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.SOLID.toString(), CyclicRecipeType.SOLID);
    event.getRegistry().register(RecipeSolidifier.SERIALIZER);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.MELTER.toString(), CyclicRecipeType.MELTER);
    event.getRegistry().register(RecipeMelter.SERIALMELTER);
  }
}
