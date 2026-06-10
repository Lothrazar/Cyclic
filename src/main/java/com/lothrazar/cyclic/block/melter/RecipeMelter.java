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
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.ByteBufCodecs;

public class RecipeMelter implements Recipe<MelterRecipeInput> {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;
  private final EnergyIngredient energy;

  public RecipeMelter(NonNullList<Ingredient> ingredientsIn, FluidStack out, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = NonNullList.create();
    ingredients.addAll(ingredientsIn);
    while (ingredients.size() < 1) {
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() > 1) {
      throw new IllegalArgumentException("Melter recipe must have exactly one ingredient");
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
      return matches(inv.getItem(0), ingredients.get(0));
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
      return false;
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
    return width <= 1 && height <= 1;
  }

  public int getEnergyCost() {
    return this.energy.getEnergyTotal();
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }

  public static class SerializeMelter implements RecipeSerializer<RecipeMelter> {

    public static final MapCodec<RecipeMelter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.getIngredients()),
        FluidStack.CODEC.fieldOf("result").forGetter(r -> r.getRecipeFluid()),
        EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.getEnergy())
    ).apply(instance, (ingredients, fluid, energy) -> new RecipeMelter(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0])), fluid, energy)));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMelter> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.getIngredients(),
        FluidStack.OPTIONAL_STREAM_CODEC, r -> r.getRecipeFluid(),
        EnergyIngredient.STREAM_CODEC, r -> r.getEnergy(),
        (ingredients, fluid, energy) -> new RecipeMelter(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0])), fluid, energy)
    );

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
