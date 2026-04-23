package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.FluidTagIngredient;
import com.lothrazar.library.util.RecipeUtil;
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

public class RecipeSolidifier implements Recipe<SolidifierRecipeInput> {

  public ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;
  public final FluidTagIngredient fluidIngredient;

  public RecipeSolidifier(NonNullList<Ingredient> inList, FluidTagIngredient fluid, ItemStack result, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = inList;
    while (ingredients.size() < 3) {
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() > 3) {
      throw new IllegalArgumentException("Solidifier recipe must have at most three ingredients");
    }
    this.fluidIngredient = fluid;
    this.result = result;
  }

  public FluidStack getRecipeFluid() {
    return this.fluidIngredient.getFluidStack();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(SolidifierRecipeInput inv, HolderLookup.Provider ra) {
    return result.copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }

  @Override
  public boolean matches(SolidifierRecipeInput inv, Level worldIn) {
    try {
      return matchItems(inv) && RecipeUtil.matchFluid(inv.getFluid(), this.fluidIngredient);
    }
    catch (Exception e) {
      return false;
    }
  }

  private int findMatchingSlot(SolidifierRecipeInput inv, Ingredient shapeless, final List<Integer> skip) {
    for (int i = 0; i < 3; i++) {
      if (skip.contains(i)) {
        continue;
      }
      if (shapeless.test(inv.getItem(i))) {
        return i;
      }
    }
    return -1;
  }

  public Ingredient at(int slot) {
    return ingredients.get(slot);
  }

  private boolean matchItems(SolidifierRecipeInput inv) {
    Ingredient top = at(0);
    Ingredient middle = at(1);
    Ingredient bottom = at(2);
    List<Integer> matchingSlots = new ArrayList<>();
    matchingSlots.add(findMatchingSlot(inv, top, matchingSlots));
    matchingSlots.add(findMatchingSlot(inv, middle, matchingSlots));
    matchingSlots.add(findMatchingSlot(inv, bottom, matchingSlots));
    if (matchingSlots.contains(-1)) {
      return false;
    }
    return matchingSlots.contains(0) && matchingSlots.contains(1) && matchingSlots.contains(2);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public ItemStack[] ingredientAt(int slot) {
    return at(slot).getItems();
  }

  @Override
  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return result.copy();
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.SOLID.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.SOLID_S.get();
  }

  public static class SerializeSolidifier implements RecipeSerializer<RecipeSolidifier> {

    // TODO: implement proper codec/streamCodec with ingredients/fluid/energy serialization
    public static final MapCodec<RecipeSolidifier> CODEC = MapCodec.unit(
        new RecipeSolidifier(NonNullList.create(), new FluidTagIngredient(FluidStack.EMPTY, "minecraft:water", 1000), ItemStack.EMPTY, new EnergyIngredient(0, 0))
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSolidifier> STREAM_CODEC =
        StreamCodec.unit(new RecipeSolidifier(NonNullList.create(), new FluidTagIngredient(FluidStack.EMPTY, "minecraft:water", 1000), ItemStack.EMPTY, new EnergyIngredient(0, 0)));

    @Override
    public MapCodec<RecipeSolidifier> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RecipeSolidifier> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
