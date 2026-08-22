package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;
// import net.neoforged.neoforge.registries.ForgeRegistries;

public class CyclicRecipeType {

  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModCyclic.MODID);
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, ModCyclic.MODID);
  //
  public static final java.util.function.Supplier<RecipeType<RecipeSolidifier>> SOLID = RECIPE_TYPES.register("solidifier", () -> new RecipeType<RecipeSolidifier>() {
  });
  public static final java.util.function.Supplier<RecipeSerializer<RecipeSolidifier>> SOLID_S = RECIPE_SERIALIZERS.register("solidifier", () -> RecipeSolidifier.SERIALIZER);
  //
  public static final java.util.function.Supplier<RecipeType<RecipeMelter>> MELTER = RECIPE_TYPES.register("melter", () -> new RecipeType<RecipeMelter>() {
  });
  public static final java.util.function.Supplier<RecipeSerializer<RecipeMelter>> MELTER_S = RECIPE_SERIALIZERS.register("melter", () -> RecipeMelter.SERIALIZER);
  //
  public static final java.util.function.Supplier<RecipeType<RecipeCrusher>> CRUSHER = RECIPE_TYPES.register("crusher", () -> new RecipeType<RecipeCrusher>() {
  });
  public static final java.util.function.Supplier<RecipeSerializer<RecipeCrusher>> CRUSHER_S = RECIPE_SERIALIZERS.register("crusher", () -> RecipeCrusher.SERIALIZER);
  //
  public static final java.util.function.Supplier<RecipeType<RecipeGeneratorItem>> GENERATOR_ITEM = RECIPE_TYPES.register("generator_item", () -> new RecipeType<RecipeGeneratorItem>() {
  });
  public static final java.util.function.Supplier<RecipeSerializer<RecipeGeneratorItem>> GENERATOR_ITEM_S = RECIPE_SERIALIZERS.register("generator_item", () -> RecipeGeneratorItem.SERIALIZER);
  //
  public static final java.util.function.Supplier<RecipeType<RecipeGeneratorFluid>> GENERATOR_FLUID = RECIPE_TYPES.register("generator_fluid", () -> new RecipeType<RecipeGeneratorFluid>() {
  });
  public static final java.util.function.Supplier<RecipeSerializer<RecipeGeneratorFluid>> GENERATOR_FLUID_S = RECIPE_SERIALIZERS.register("generator_fluid", () -> RecipeGeneratorFluid.SERIALIZER);
}
