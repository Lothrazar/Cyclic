package com.lothrazar.cyclic.block.melter;

import java.util.List;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidInstance;
import net.neoforged.neoforge.fluids.FluidStack;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.codec.ByteBufCodecs;

public class RecipeMelter implements Recipe<MelterRecipeInput> {

  // Plain FluidStack.CODEC's "id" field validates Fluid#areComponentsBound(), which fails during
  // datapack/recipe reload with DataResult.Error["Fluid X does not have components yet"] - same bug
  // class as ItemStack.CODEC (see RecipeSolidifier/RecipeCrusher's "result" field fix). Using the
  // unbound FluidInstance#FLUID_HOLDER_CODEC for the "id" sub-field alone isn't enough though: even
  // constructing a plain `new FluidStack(holder, amount, patch)` throws its own
  // NullPointerException("Components not bound yet") the moment the constructor touches
  // holder.components() to build the fluid's default component prototype - so no real FluidStack can be
  // built at decode time at all. This tiny template record (mirroring vanilla's ItemStackTemplate, which
  // NeoForge has no fluid equivalent of) decodes the same 3 fields but only builds the real FluidStack
  // lazily, in create() - see getRecipeFluid() below, which is never called until well after the
  // registry has finished binding components.
  private record ResultTemplate(Holder<Fluid> fluid, int amount, DataComponentPatch patch) {
    static final MapCodec<ResultTemplate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
        FluidInstance.FLUID_HOLDER_CODEC.fieldOf("id").forGetter(ResultTemplate::fluid),
        ExtraCodecs.POSITIVE_INT.fieldOf("amount").forGetter(ResultTemplate::amount),
        DataComponentPatch.CODEC.optionalFieldOf("components", DataComponentPatch.EMPTY).forGetter(ResultTemplate::patch)
    ).apply(i, ResultTemplate::new));

    static ResultTemplate fromStack(FluidStack stack) {
      return new ResultTemplate(stack.typeHolder(), stack.getAmount(), stack.getComponentsPatch());
    }

    FluidStack create() {
      return new FluidStack(fluid, amount, patch);
    }
  }

  // .validate(...) here (not a constructor-side throw) so a malformed recipe fails gracefully as a
  // per-file DataResult.Error (logged with the actual file name by SimpleJsonResourceReloadListener)
  // instead of throwing a raw exception that aborts the entire datapack reload for every recipe type.
  public static final MapCodec<RecipeMelter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC.listOf().validate(list -> list.size() == 1
          ? com.mojang.serialization.DataResult.success(list)
          : com.mojang.serialization.DataResult.error(() -> "Melter recipe must have exactly one ingredient, got " + list.size()))
          .fieldOf("ingredients").forGetter(r -> r.getIngredients()),
      ResultTemplate.CODEC.codec().fieldOf("result").forGetter(r -> r.resultTemplate),
      EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.getEnergy())
  ).apply(instance, RecipeMelter::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, RecipeMelter> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.getIngredients(),
      FluidStack.OPTIONAL_STREAM_CODEC, r -> r.getRecipeFluid(),
      EnergyIngredient.STREAM_CODEC, r -> r.getEnergy(),
      (ingredients, fluid, energy) -> new RecipeMelter(ingredients, ResultTemplate.fromStack(fluid), energy)
  );

  public static final RecipeSerializer<RecipeMelter> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

  private final ResultTemplate resultTemplate;
  private FluidStack outFluid;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;

  public RecipeMelter(List<Ingredient> ingredientsIn, ResultTemplate resultTemplate, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = NonNullList.create();
    ingredients.addAll(ingredientsIn);
    // No padding with Ingredient.of() here - it now throws UnsupportedOperationException("Ingredients
    // can't be empty") in 26.1 (empty Ingredients can no longer be constructed at all). A melter recipe
    // legitimately declaring zero ingredients is a genuine data error, not a case to silently paper over.
    if (ingredients.size() != 1) {
      throw new IllegalArgumentException("Melter recipe must have exactly one ingredient, got " + ingredients);
    }
    this.resultTemplate = resultTemplate;
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
    if (outFluid == null) {
      outFluid = resultTemplate.create();
    }
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
