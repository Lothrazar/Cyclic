package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.mojang.serialization.MapCodec;
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

public class RecipeMelter implements Recipe<MelterRecipeInput> {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;
  private final EnergyIngredient energy;

  public RecipeMelter(NonNullList<Ingredient> ingredientsIn, FluidStack out, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = ingredientsIn;
    while (ingredients.size() < 2) {
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() > 2) {
      throw new IllegalArgumentException("Melter recipe must have at most two ingredients");
    }
    this.outFluid = out;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(MelterRecipeInput inv, Level worldIn) {
    try {
      boolean matchLeft = matches(inv.getItem(0), ingredients.get(0));
      boolean matchRight = matches(inv.getItem(1), ingredients.get(1));
      return matchLeft && matchRight;
    }
    catch (Exception e) {
      return false;
    }
  }

  public boolean matches(ItemStack current, Ingredient ing) {
    if (ing == Ingredient.EMPTY) {
      return current.isEmpty();
    }
    if (current.isEmpty()) {
      return ing == Ingredient.EMPTY;
    }
    return ing.test(current);
  }

  public ItemStack[] ingredientAt(int slot) {
    return at(slot).getItems();
  }

  public Ingredient at(int slot) {
    return ingredients.get(slot);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  public FluidStack getRecipeFluid() {
    return outFluid.copy();
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.MELTER.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.MELTER_S.get();
  }

  @Override
  public ItemStack assemble(MelterRecipeInput t, HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width <= 2 && height <= 1;
  }

  public int getEnergyCost() {
    return this.energy.getEnergyTotal();
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }

  public static class SerializeMelter implements RecipeSerializer<RecipeMelter> {

    // TODO: implement proper codec/streamCodec using EnergyIngredient/FluidStack serialization
    public static final MapCodec<RecipeMelter> CODEC = MapCodec.unit(
        new RecipeMelter(NonNullList.create(), FluidStack.EMPTY, new EnergyIngredient(0, 0))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMelter> STREAM_CODEC =
        StreamCodec.unit(new RecipeMelter(NonNullList.create(), FluidStack.EMPTY, new EnergyIngredient(0, 0)));

    @Override
    public MapCodec<RecipeMelter> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeMelter> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
