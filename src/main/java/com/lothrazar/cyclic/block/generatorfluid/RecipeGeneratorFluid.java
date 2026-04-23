package com.lothrazar.cyclic.block.generatorfluid;

import java.util.List;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.FluidTagIngredient;
import com.lothrazar.library.util.RecipeUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

public class RecipeGeneratorFluid implements Recipe<GeneratorFluidRecipeInput> {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final FluidTagIngredient fluidIng;
  private final EnergyIngredient energy;

  public RecipeGeneratorFluid(FluidTagIngredient in, EnergyIngredient energy) {
    this.fluidIng = in;
    this.energy = energy;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(GeneratorFluidRecipeInput inv, HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  public FluidStack getRecipeFluid() {
    return fluidIng.getFluidStack();
  }

  @Deprecated
  public List<Fluid> getFluidsFromTag() {
    return null;
  }

  public TagKey<Fluid> getTag() {
    return TagKey.create(Registries.FLUID, ResourceLocation.parse(this.fluidIng.getTag()));
  }

  @Override
  public boolean matches(GeneratorFluidRecipeInput inv, Level worldIn) {
    try {
      return RecipeUtil.matchFluid(inv.getFluid(), this.fluidIng);
    }
    catch (Exception e) {
      return false;
    }
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.GENERATOR_FLUID.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.GENERATOR_FLUID_S.get();
  }

  public int getTicks() {
    return energy.getTicks();
  }

  public int getRfpertick() {
    return energy.getRfPertick();
  }

  public int getRfTotal() {
    return this.getRfpertick() * this.getTicks();
  }

  public static class SerializeGenerateFluid implements RecipeSerializer<RecipeGeneratorFluid> {

    // TODO: implement proper codec/streamCodec with FluidTagIngredient/EnergyIngredient serialization
    public static final MapCodec<RecipeGeneratorFluid> CODEC = MapCodec.unit(
        new RecipeGeneratorFluid(new FluidTagIngredient(FluidStack.EMPTY, "minecraft:water", 1000), new EnergyIngredient(0, 0))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorFluid> STREAM_CODEC =
        StreamCodec.unit(new RecipeGeneratorFluid(new FluidTagIngredient(FluidStack.EMPTY, "minecraft:water", 1000), new EnergyIngredient(0, 0)));

    @Override
    public MapCodec<RecipeGeneratorFluid> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorFluid> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
