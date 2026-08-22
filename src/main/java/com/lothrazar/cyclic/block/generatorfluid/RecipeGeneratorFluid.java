package com.lothrazar.cyclic.block.generatorfluid;

import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.List;

public class RecipeGeneratorFluid implements Recipe<GeneratorFluidRecipeInput> {

  public static final MapCodec<RecipeGeneratorFluid> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      SizedFluidIngredient.CODEC.fieldOf("fuel").forGetter(r -> r.fluid),
      EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> new EnergyIngredient(r.getRfpertick(), r.getTicks()))
  ).apply(instance, RecipeGeneratorFluid::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorFluid> STREAM_CODEC = StreamCodec.composite(
      SizedFluidIngredient.STREAM_CODEC, r -> r.fluid,
      EnergyIngredient.STREAM_CODEC, r -> new EnergyIngredient(r.getRfpertick(), r.getTicks()),
      RecipeGeneratorFluid::new
  );

  public static final RecipeSerializer<RecipeGeneratorFluid> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final SizedFluidIngredient fluid;
  private final EnergyIngredient energy;

  public RecipeGeneratorFluid(SizedFluidIngredient in, EnergyIngredient energy) {
    this.fluid = in;
    this.energy = energy;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(GeneratorFluidRecipeInput inv) {
    return ItemStack.EMPTY;
  }

  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  public FluidStack getRecipeFluid() {
    var matches = fluid.ingredient().fluids();
    return matches.isEmpty() ? FluidStack.EMPTY : new FluidStack(matches.get(0), fluid.amount());
  }

  public List<FluidStack> getMatchingFluids() {
    return fluid.ingredient().fluids().stream().map(h -> new FluidStack(h, fluid.amount())).toList();
  }

  public int getAmount() {
    return fluid.amount();
  }

  @Override
  public boolean matches(GeneratorFluidRecipeInput inv, Level worldIn) {
    try {
      return fluid.test(inv.getFluid());
    } catch (Exception e) {
      return false;
    }
  }

  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public RecipeType<RecipeGeneratorFluid> getType() {
    return CyclicRecipeType.GENERATOR_FLUID.get();
  }

  @Override
  public RecipeSerializer<RecipeGeneratorFluid> getSerializer() {
    return CyclicRecipeType.GENERATOR_FLUID_S.get();
  }

  @Override
  public boolean showNotification() {
    return true;
  }

  @Override
  public String group() {
    return "";
  }

  @Override
  public PlacementInfo placementInfo() {
    return PlacementInfo.NOT_PLACEABLE;
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.CRAFTING_MISC;
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
}
