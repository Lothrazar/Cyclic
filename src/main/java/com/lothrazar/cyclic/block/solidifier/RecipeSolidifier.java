package com.lothrazar.cyclic.block.solidifier;

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
import net.minecraft.world.item.ItemStackTemplate;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecipeSolidifier implements Recipe<SolidifierRecipeInput> {

  // result uses ItemStackTemplate (not ItemStack) so JSON parsing doesn't require the result item's
  // components to already be bound - matches vanilla ShapedRecipe/ShapelessRecipe's own "result" field,
  // which hits the same DataResult.Error["Item X does not have components yet"] failure otherwise
  // (ItemStack.CODEC's "id" field validates Item#areComponentsBound(), ItemStackTemplate's doesn't).
  // Critically, ItemStackTemplate#create() must NOT be called during decode either - components are
  // still unbound at that point too, so create() throws its own NullPointerException("Components not
  // bound yet") if called eagerly. The template is stored as-is and only turned into a real ItemStack
  // lazily, the first time getResult() is actually called (well after the registry has finished
  // binding components) - see getResult() below.
  // .validate(...) here (not a constructor-side throw) so a malformed recipe fails gracefully as a
  // per-file DataResult.Error (logged with the actual file name by SimpleJsonResourceReloadListener)
  // instead of throwing a raw exception that aborts the entire datapack reload for every recipe type.
  public static final MapCodec<RecipeSolidifier> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC.listOf().validate(list -> list.size() <= 3
              ? com.mojang.serialization.DataResult.success(list)
              : com.mojang.serialization.DataResult.error(() -> "Solidifier recipe must have at most three ingredients, got " + list.size()))
          .fieldOf("ingredients").forGetter(r -> r.getIngredients()),
      SizedFluidIngredient.CODEC.fieldOf("mix").forGetter(r -> r.fluidIngredient),
      ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.resultTemplate),
      EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.getEnergy())
  ).apply(instance, RecipeSolidifier::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, RecipeSolidifier> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.getIngredients(),
      SizedFluidIngredient.STREAM_CODEC, r -> r.fluidIngredient,
      ItemStack.OPTIONAL_STREAM_CODEC, r -> r.getResult(),
      EnergyIngredient.STREAM_CODEC, r -> r.getEnergy(),
      (ingredients, fluid, result, energy) -> new RecipeSolidifier(ingredients, fluid, ItemStackTemplate.fromNonEmptyStack(result), energy)
  );

  public static final RecipeSerializer<RecipeSolidifier> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

  private final ItemStackTemplate resultTemplate;
  private ItemStack result;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;
  public final SizedFluidIngredient fluidIngredient;

  public RecipeSolidifier(List<Ingredient> inList, SizedFluidIngredient fluid, ItemStackTemplate resultTemplate, EnergyIngredient energy) {
    this.energy = energy;
    ingredients = NonNullList.create();
    ingredients.addAll(inList);
    // Slots beyond the declared ingredient count are simply unused - do NOT pad with Ingredient.of(),
    // which now throws UnsupportedOperationException("Ingredients can't be empty") in 26.1 (an empty
    // Ingredient can no longer be constructed at all). at()/matchItems() below treat missing slots as
    // Optional.empty(), matching vanilla's own ShapedRecipePattern convention for optional slots.
    if (ingredients.size() > 3) {
      throw new IllegalArgumentException("Solidifier recipe must have at most three ingredients");
    }
    this.fluidIngredient = fluid;
    this.resultTemplate = resultTemplate;
  }

  public ItemStack getResult() {
    if (result == null) {
      result = resultTemplate.create();
    }
    return result;
  }

  public FluidStack getRecipeFluid() {
    var matches = fluidIngredient.ingredient().fluids();
    return matches.isEmpty() ? FluidStack.EMPTY : new FluidStack(matches.get(0), fluidIngredient.amount());
  }

  public List<FluidStack> getMatchingFluids() {
    return fluidIngredient.ingredient().fluids().stream().map(h -> new FluidStack(h, fluidIngredient.amount())).toList();
  }

  public int getAmount() {
    return fluidIngredient.amount();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(SolidifierRecipeInput inv) {
    return getResult().copy();
  }

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
    } catch (Exception e) {
      return false;
    }
  }

  private int findMatchingSlot(SolidifierRecipeInput inv, Optional<Ingredient> shapeless, final List<Integer> skip) {
    for (int i = 0; i < 3; i++) {
      if (skip.contains(i)) {
        continue;
      }
      if (Ingredient.testOptionalIngredient(shapeless, inv.getItem(i))) {
        return i;
      }
    }
    return -1;
  }

  // Optional<Ingredient>, not Ingredient: slots beyond the declared ingredient count have no
  // requirement at all, matching vanilla's own Optional<Ingredient>/testOptionalIngredient convention
  // (see ShapedRecipePattern) rather than a padded empty Ingredient (which can't be constructed in 26.1).
  public Optional<Ingredient> at(int slot) {
    return slot < ingredients.size() ? Optional.of(ingredients.get(slot)) : Optional.empty();
  }

  private boolean matchItems(SolidifierRecipeInput inv) {
    Optional<Ingredient> top = at(0);
    Optional<Ingredient> middle = at(1);
    Optional<Ingredient> bottom = at(2);
    List<Integer> matchingSlots = new ArrayList<>();
    matchingSlots.add(findMatchingSlot(inv, top, matchingSlots));
    matchingSlots.add(findMatchingSlot(inv, middle, matchingSlots));
    matchingSlots.add(findMatchingSlot(inv, bottom, matchingSlots));
    if (matchingSlots.contains(-1)) {
      return false;
    }
    return matchingSlots.contains(0) && matchingSlots.contains(1) && matchingSlots.contains(2);
  }

  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  public ItemStack[] ingredientAt(int slot) {
    return at(slot).map(i -> i.items().map(ItemStack::new).toArray(ItemStack[]::new)).orElse(new ItemStack[0]);
  }

  public ItemStack getResultItem(HolderLookup.Provider ra) {
    return getResult().copy();
  }

  @Override
  public RecipeType<RecipeSolidifier> getType() {
    return CyclicRecipeType.SOLID.get();
  }

  @Override
  public RecipeSerializer<RecipeSolidifier> getSerializer() {
    return CyclicRecipeType.SOLID_S.get();
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
}
