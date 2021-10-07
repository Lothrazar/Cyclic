package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.core.Registry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistry {

  public static void registerRecipeSerializers(Register<RecipeSerializer<?>> event) {
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.SOLID.toString(), CyclicRecipeType.SOLID);
    event.getRegistry().register(RecipeSolidifier.SERIALIZER);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.MELTER.toString(), CyclicRecipeType.MELTER);
    event.getRegistry().register(RecipeMelter.SERIALMELTER);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.GENERATOR_ITEM.toString(), CyclicRecipeType.GENERATOR_ITEM);
    event.getRegistry().register(RecipeGeneratorItem.SERIALGENERATOR);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.GENERATOR_FLUID.toString(), CyclicRecipeType.GENERATOR_FLUID);
    event.getRegistry().register(RecipeGeneratorFluid.SERIALGENERATORF);
  }
}
