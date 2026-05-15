package com.lothrazar.cyclic.block.crusher;

import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.RandomizedOutputIngredient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class RecipeCrusher implements Recipe<CrusherRecipeInput> {

  public ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final EnergyIngredient energy;
  public RandomizedOutputIngredient randOutput;

  public RecipeCrusher(Ingredient in, EnergyIngredient energy, ItemStack out, RandomizedOutputIngredient randOutput) {
    this.energy = energy;
    ingredients.add(in);
    this.result = out;
    this.randOutput = randOutput;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(CrusherRecipeInput inv, Level worldIn) {
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
    return result;
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.CRUSHER.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.CRUSHER_S.get();
  }

  public ItemStack createBonus(RandomSource rand) {
    ItemStack getBonus = this.randOutput.bonus.copy();
    if (this.randOutput.bonus.getCount() > 1) {
      getBonus.setCount(1 + rand.nextInt(this.randOutput.bonus.getCount()));
    }
    return getBonus;
  }

  @Override
  public ItemStack assemble(CrusherRecipeInput t, HolderLookup.Provider ra) {
    return result.copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width <= 1 && height <= 1;
  }

  public static class SerializeCrusher implements RecipeSerializer<RecipeCrusher> {

    public static final MapCodec<RecipeCrusher> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.at(0)),
        EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.energy),
        ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
        RandomizedOutputIngredient.CODEC.optionalFieldOf("bonus", new RandomizedOutputIngredient(0, ItemStack.EMPTY)).forGetter(r -> r.randOutput)
    ).apply(instance, RecipeCrusher::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCrusher> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, r -> r.at(0),
        EnergyIngredient.STREAM_CODEC, r -> r.energy,
        ItemStack.OPTIONAL_STREAM_CODEC, r -> r.result,
        RandomizedOutputIngredient.STREAM_CODEC, r -> r.randOutput,
        RecipeCrusher::new
    );

    @Override
    public MapCodec<RecipeCrusher> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeCrusher> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
