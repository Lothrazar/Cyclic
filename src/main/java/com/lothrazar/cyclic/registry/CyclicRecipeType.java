package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.crusher.RecipeCrusher;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid;
import com.lothrazar.cyclic.block.generatorfluid.RecipeGeneratorFluid.SerializeGenerateFluid;
import com.lothrazar.cyclic.block.generatoritem.RecipeGeneratorItem;
import com.lothrazar.cyclic.block.melter.RecipeMelter;
import com.lothrazar.cyclic.block.melter.RecipeMelter.SerializeMelter;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier;
import com.lothrazar.cyclic.block.solidifier.RecipeSolidifier.SerializeSolidifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CyclicRecipeType { // <RECIPE_TYPE extends CyclicRecipe> implements RecipeType<RECIPE_TYPE> {

  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, ModCyclic.MODID);
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ModCyclic.MODID);
  //
  public static final RegistryObject<RecipeType<RecipeSolidifier>> SOLID = RECIPE_TYPES.register("solidifier", () -> new RecipeType<RecipeSolidifier>() {});
  public static final RegistryObject<SerializeSolidifier> SOLID_S = RECIPE_SERIALIZERS.register("solidifier", () -> new SerializeSolidifier());
  //
  public static final RegistryObject<RecipeType<RecipeMelter>> MELTER = RECIPE_TYPES.register("melter", () -> new RecipeType<RecipeMelter>() {});
  public static final RegistryObject<SerializeMelter> MELTER_S = RECIPE_SERIALIZERS.register("melter", () -> new RecipeMelter.SerializeMelter());
  //
  public static final RegistryObject<RecipeType<RecipeCrusher>> CRUSHER = RECIPE_TYPES.register("crusher", () -> new RecipeType<RecipeCrusher>() {});
  public static final RegistryObject<RecipeCrusher.SerializeCrusher> CRUSHER_S = RECIPE_SERIALIZERS.register("crusher", () -> new RecipeCrusher.SerializeCrusher());
  //
  public static final RegistryObject<RecipeType<RecipeGeneratorItem>> GENERATOR_ITEM = RECIPE_TYPES.register("generator_item", () -> new RecipeType<RecipeGeneratorItem>() {});
  public static final RegistryObject<RecipeGeneratorItem.SerializeGenerateItem> GENERATOR_ITEM_S = RECIPE_SERIALIZERS.register("generator_item", () -> new RecipeGeneratorItem.SerializeGenerateItem());
  //
  public static final RegistryObject<RecipeType<RecipeGeneratorFluid>> GENERATOR_FLUID = RECIPE_TYPES.register("generator_fluid", () -> new RecipeType<RecipeGeneratorFluid>() {});
  public static final RegistryObject<SerializeGenerateFluid> GENERATOR_FLUID_S = RECIPE_SERIALIZERS.register("generator_fluid", () -> new SerializeGenerateFluid());
  //
  //
  //  public static final CyclicRecipeType<RecipeSolidifier<TileBlockEntityCyclic>> SOLID = create("solidifier");
  //  public static final CyclicRecipeType<RecipeMelter<TileBlockEntityCyclic>> MELTER = create("melter");
  //  public static final CyclicRecipeType<RecipeCrusher<TileBlockEntityCyclic>> CRUSHER = create("crusher");
  //  public static final CyclicRecipeType<RecipeGeneratorItem<TileBlockEntityCyclic>> GENERATOR_ITEM = create("generator_item");
  //  public static final CyclicRecipeType<RecipeGeneratorFluid<TileBlockEntityCyclic>> GENERATOR_FLUID = create("generator_fluid");
  //  private static <RECIPE_TYPE extends CyclicRecipe> CyclicRecipeType<RECIPE_TYPE> create(String name) {
  //    CyclicRecipeType<RECIPE_TYPE> type = new CyclicRecipeType<>(name);
  //    return type;
  //  }
  //
  //  private String registryName;
  //
  //  private CyclicRecipeType(String name) {
  //    this.registryName = name;
  //  }
  //
  //  @Override
  //  public String toString() {
  //    return ModCyclic.MODID + ":" + registryName;
  //  }
}
