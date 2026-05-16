package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public class RecipeSolidifier implements Recipe<SolidifierRecipeInput> {

  public ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;
  public final SizedFluidIngredient fluidIngredient;

  public RecipeSolidifier(NonNullList<Ingredient> inList, SizedFluidIngredient fluid, ItemStack result, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = NonNullList.create();
    ingredients.addAll(inList);
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
    FluidStack[] stacks = fluidIngredient.getFluids();
    return stacks.length == 0 ? FluidStack.EMPTY : stacks[0];
  }

  public List<FluidStack> getMatchingFluids() {
    return List.of(fluidIngredient.getFluids());
  }

  public int getAmount() {
    return fluidIngredient.amount();
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
      return matchItems(inv) && fluidIngredient.test(inv.getFluid());
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

    public static final MapCodec<RecipeSolidifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.getIngredients()),
        SizedFluidIngredient.FLAT_CODEC.fieldOf("mix").forGetter(r -> r.fluidIngredient),
        ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result),
        EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.getEnergy())
    ).apply(instance, (ingredients, fluid, result, energy) -> new RecipeSolidifier(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0])), fluid, result, energy)));
    public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSolidifier> STREAM_CODEC = StreamCodec.composite(
        Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.getIngredients(),
        SizedFluidIngredient.STREAM_CODEC, r -> r.fluidIngredient,
        ItemStack.OPTIONAL_STREAM_CODEC, r -> r.result,
        EnergyIngredient.STREAM_CODEC, r -> r.getEnergy(),
        (ingredients, fluid, result, energy) -> new RecipeSolidifier(NonNullList.of(Ingredient.EMPTY, ingredients.toArray(new Ingredient[0])), fluid, result, energy)
    );

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
