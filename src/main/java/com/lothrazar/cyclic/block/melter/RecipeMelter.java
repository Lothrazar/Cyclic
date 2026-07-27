package com.lothrazar.cyclic.block.melter;

import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidInstance;
import net.neoforged.neoforge.fluids.FluidStack;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.ByteBufCodecs;

public class RecipeMelter implements Recipe<MelterRecipeInput> {

  // Plain FluidStack.CODEC's "id" field validates Fluid#areComponentsBound(), which fails during
  // datapack/recipe reload with DataResult.Error["Fluid X does not have components yet"] - same bug
  // class as ItemStack.CODEC (see RecipeSolidifier/RecipeCrusher's "result" field fix), but NeoForge
  // doesn't ship a ready-made "FluidStackTemplate" equivalent to ItemStackTemplate, so this rebuilds
  // FluidStack's own MAP_CODEC shape using the unbound FluidInstance#FLUID_HOLDER_CODEC instead.
  private static final MapCodec<FluidStack> RESULT_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
      FluidInstance.FLUID_HOLDER_CODEC.fieldOf("id").forGetter(FluidStack::typeHolder),
      ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(FluidStack::getAmount),
      DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(FluidStack::getComponentsPatch)
  ).apply(i, FluidStack::new));

  public static final MapCodec<RecipeMelter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(r -> r.getIngredients()),
      RESULT_CODEC.codec().fieldOf("result").forGetter(r -> r.getRecipeFluid()),
      EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.getEnergy())
  ).apply(instance, (ingredients, fluid, energy) -> new RecipeMelter(NonNullList.of(Ingredient.of(), ingredients.toArray(new Ingredient[0])), fluid, energy)));
  public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMelter> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.getIngredients(),
      FluidStack.OPTIONAL_STREAM_CODEC, r -> r.getRecipeFluid(),
      EnergyIngredient.STREAM_CODEC, r -> r.getEnergy(),
      (ingredients, fluid, energy) -> new RecipeMelter(NonNullList.of(Ingredient.of(), ingredients.toArray(new Ingredient[0])), fluid, energy)
  );

  public static final RecipeSerializer<RecipeMelter> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack outFluid;
  private final EnergyIngredient energy;

  public RecipeMelter(NonNullList<Ingredient> ingredientsIn, FluidStack out, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = NonNullList.create();
    ingredients.addAll(ingredientsIn);
    while (ingredients.size() < 1) {
      ingredients.add(Ingredient.of());
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
    if (ing.isEmpty()) {
      return current.isEmpty();
    }
    if (current.isEmpty()) {
      return false;
    }
    return ing.test(current);
  }

  public ItemStack[] ingredientAt(int slot) {
    return at(slot).items().map(ItemStack::new).toArray(ItemStack[]::new);
  }

  public Ingredient at(int slot) {
    return ingredients.get(slot);
  }

    public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return ItemStack.EMPTY;
  }

  public FluidStack getRecipeFluid() {
    return outFluid.copy();
  }

  @Override
  public RecipeType<RecipeMelter> getType() {
    return CyclicRecipeType.MELTER.get();
  }

  @Override
  public RecipeSerializer<RecipeMelter> getSerializer() {
    return CyclicRecipeType.MELTER_S.get();
  }

  @Override
  public ItemStack assemble(MelterRecipeInput t) {
    return ItemStack.EMPTY;
  }

    public boolean canCraftInDimensions(int width, int height) {
    return width <= 1 && height <= 1;
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

  public int getEnergyCost() {
    return this.energy.getEnergyTotal();
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }
}
