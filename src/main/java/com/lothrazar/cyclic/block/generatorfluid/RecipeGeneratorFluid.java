package com.lothrazar.cyclic.block.generatorfluid;

import java.util.List;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class RecipeGeneratorFluid implements Recipe<GeneratorFluidRecipeInput> {

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
    FluidStack[] stacks = fluid.getFluids();
    return stacks.length == 0 ? FluidStack.EMPTY : stacks[0];
  }

  public List<FluidStack> getMatchingFluids() {
    return List.of(fluid.getFluids());
  }

  public int getAmount() {
    return fluid.amount();
  }

  @Override
  public boolean matches(GeneratorFluidRecipeInput inv, Level worldIn) {
    try {
      return fluid.test(inv.getFluid());
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

    public static final MapCodec<RecipeGeneratorFluid> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        SizedFluidIngredient.FLAT_CODEC.fieldOf("fuel").forGetter(r -> r.fluid),
        EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> new EnergyIngredient(r.getRfpertick(), r.getTicks()))
    ).apply(instance, RecipeGeneratorFluid::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorFluid> STREAM_CODEC = StreamCodec.composite(
        SizedFluidIngredient.STREAM_CODEC, r -> r.fluid,
        EnergyIngredient.STREAM_CODEC, r -> new EnergyIngredient(r.getRfpertick(), r.getTicks()),
        RecipeGeneratorFluid::new
    );

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
