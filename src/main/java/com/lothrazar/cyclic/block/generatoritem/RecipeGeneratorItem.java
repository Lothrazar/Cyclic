package com.lothrazar.cyclic.block.generatoritem;

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
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeGeneratorItem implements Recipe<RecipeInput> {

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;

  public RecipeGeneratorItem(Ingredient in, EnergyIngredient energy) {
    ingredients.add(in);
    this.energy = energy;
  }

  @Override
  public String toString() {
    return "RecipeGeneratorItem [ingredients=" + ingredients.get(0) + ", energy=" + energy + "]";
  }

  @Override
  public ItemStack assemble(RecipeInput inv, HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(RecipeInput inv, Level worldIn) {
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
    return ItemStack.EMPTY;
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.GENERATOR_ITEM.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.GENERATOR_ITEM_S.get();
  }

  public int getTicks() {
    return energy.getTicks();
  }

  public int getRfPertick() {
    return energy.getRfPertick();
  }

  public int getEnergyTotal() {
    return this.getRfPertick() * this.getTicks();
  }

  public static class SerializeGenerateItem implements RecipeSerializer<RecipeGeneratorItem> {

    public static final MapCodec<RecipeGeneratorItem> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.at(0)),
        EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> new EnergyIngredient(r.getRfPertick(), r.getTicks()))
    ).apply(instance, RecipeGeneratorItem::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorItem> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC, r -> r.at(0),
        EnergyIngredient.STREAM_CODEC, r -> new EnergyIngredient(r.getRfPertick(), r.getTicks()),
        RecipeGeneratorItem::new
    );

    @Override
    public MapCodec<RecipeGeneratorItem> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeGeneratorItem> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
