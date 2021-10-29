package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RecipeRegistry {

  public static void registerRecipeSerializers(Register<RecipeSerializer<?>> event) {
    IForgeRegistry<RecipeSerializer<?>> r = event.getRegistry();
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.SOLID.toString(), CyclicRecipeType.SOLID);
    r.register(RecipeSolidifier.SERIALIZER);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.MELTER.toString(), CyclicRecipeType.MELTER);
    r.register(RecipeMelter.SERIALMELTER);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.GENERATOR_ITEM.toString(), CyclicRecipeType.GENERATOR_ITEM);
    r.register(RecipeGeneratorItem.SERIALGENERATOR);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.GENERATOR_FLUID.toString(), CyclicRecipeType.GENERATOR_FLUID);
    r.register(RecipeGeneratorFluid.SERIALGENERATORF);
    Registry.register(Registry.RECIPE_TYPE, CyclicRecipeType.CRUSHER.toString(), CyclicRecipeType.CRUSHER);
    r.register(RecipeCrusher.SERIALCRUSH);
  }
}
