package com.lothrazar.cyclic.block.crusher;

import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import com.lothrazar.library.recipe.ingredient.RandomizedOutputIngredient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
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

import java.util.Optional;

public class RecipeCrusher implements Recipe<CrusherRecipeInput> {

  // result uses ItemStackTemplate (not ItemStack) so JSON parsing doesn't require the result item's
  // components to already be bound - matches vanilla ShapedRecipe/ShapelessRecipe's own "result" field,
  // which hits the same DataResult.Error["Item X does not have components yet"] failure otherwise
  // (ItemStack.CODEC's "id" field validates Item#areComponentsBound(), ItemStackTemplate's doesn't).
  // Critically, ItemStackTemplate#create() must NOT be called during decode either - components are
  // still unbound at that point too, so create() throws its own NullPointerException("Components not
  // bound yet") if called eagerly. The template is stored as-is and only turned into a real ItemStack
  // lazily, the first time getResult() is actually called (well after the registry has finished
  // binding components) - see getResult() below.
  //
  // The "bonus" field can't just reuse FLib's RandomizedOutputIngredient.CODEC directly for JSON decode:
  // that codec's own "bonus" item field is built on ItemStack.OPTIONAL_CODEC (a bound-components codec),
  // which is a third-party bug we can't patch (compiled dependency). Every recipe with a "bonus" block
  // hit the exact same "Item X does not have components yet" DataResult.Error as the "result" field used
  // to. So BonusTemplate below decodes the same {percent, bonus} JSON shape ourselves using
  // ItemStackTemplate for the nested item, and getRandOutput() below lazily builds the real
  // RandomizedOutputIngredient the first time it's actually needed.
  private record BonusTemplate(int percent, ItemStackTemplate bonus) {
    static final Codec<BonusTemplate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.INT.fieldOf("percent").forGetter(BonusTemplate::percent),
        ItemStackTemplate.CODEC.fieldOf("bonus").forGetter(BonusTemplate::bonus)
    ).apply(instance, BonusTemplate::new));
  }

  public static final MapCodec<RecipeCrusher> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC.fieldOf("ingredient").forGetter(r -> r.at(0)),
      EnergyIngredient.CODEC.fieldOf("energy").forGetter(r -> r.energy),
      ItemStackTemplate.CODEC.fieldOf("result").forGetter(r -> r.resultTemplate),
      BonusTemplate.CODEC.optionalFieldOf("bonus").forGetter(r -> r.bonusTemplate)
  ).apply(instance, RecipeCrusher::new));

  public static final StreamCodec<RegistryFriendlyByteBuf, RecipeCrusher> STREAM_CODEC = StreamCodec.composite(
      Ingredient.CONTENTS_STREAM_CODEC, r -> r.at(0),
      EnergyIngredient.STREAM_CODEC, r -> r.energy,
      ItemStack.OPTIONAL_STREAM_CODEC, r -> r.getResult(),
      RandomizedOutputIngredient.STREAM_CODEC, r -> r.getRandOutput(),
      (ingredient, energy, result, bonus) -> new RecipeCrusher(ingredient, energy, ItemStackTemplate.fromNonEmptyStack(result), bonus)
  );

  public static final RecipeSerializer<RecipeCrusher> SERIALIZER = new RecipeSerializer<>(CODEC, STREAM_CODEC);

  private final ItemStackTemplate resultTemplate;
  private ItemStack result;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final EnergyIngredient energy;
  private final Optional<BonusTemplate> bonusTemplate;
  private RandomizedOutputIngredient randOutputCache;

  // JSON-decode path: bonus stays a lazy template, resolved on first getRandOutput() call (post component-bind).
  public RecipeCrusher(Ingredient in, EnergyIngredient energy, ItemStackTemplate resultTemplate, Optional<BonusTemplate> bonusTemplate) {
    this.energy = energy;
    ingredients.add(in);
    this.resultTemplate = resultTemplate;
    this.bonusTemplate = bonusTemplate;
  }

  // Network-decode path: bonus arrives as an already-resolved, already-bound ItemStack (live gameplay
  // data), so no lazy template resolution is needed - cache it directly.
  public RecipeCrusher(Ingredient in, EnergyIngredient energy, ItemStackTemplate resultTemplate, RandomizedOutputIngredient randOutput) {
    this.energy = energy;
    ingredients.add(in);
    this.resultTemplate = resultTemplate;
    this.bonusTemplate = Optional.empty();
    this.randOutputCache = randOutput;
  }

  public ItemStack getResult() {
    if (result == null) {
      result = resultTemplate.create();
    }
    return result;
  }

  public RandomizedOutputIngredient getRandOutput() {
    if (randOutputCache == null) {
      randOutputCache = bonusTemplate
          .map(bt -> new RandomizedOutputIngredient(bt.percent(), bt.bonus().create()))
          .orElseGet(() -> new RandomizedOutputIngredient(0, ItemStack.EMPTY));
    }
    return randOutputCache;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(CrusherRecipeInput inv, Level worldIn) {
    try {
      return matches(inv.getItem(0), ingredients.get(0));
    } catch (Exception e) {
      return false;
    }
  }

  public boolean matches(ItemStack current, Ingredient ing) {
    if (ing.isEmpty()) {
      return current.isEmpty();
    }
    if (current.isEmpty()) {
      return ing.isEmpty();
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
    return getResult();
  }

  @Override
  public RecipeType<RecipeCrusher> getType() {
    return CyclicRecipeType.CRUSHER.get();
  }

  @Override
  public RecipeSerializer<RecipeCrusher> getSerializer() {
    return CyclicRecipeType.CRUSHER_S.get();
  }

  public ItemStack createBonus(RandomSource rand) {
    ItemStack bonusStack = getRandOutput().bonus;
    ItemStack getBonus = bonusStack.copy();
    if (bonusStack.getCount() > 1) {
      getBonus.setCount(1 + rand.nextInt(bonusStack.getCount()));
    }
    return getBonus;
  }

  @Override
  public ItemStack assemble(CrusherRecipeInput t) {
    return getResult().copy();
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
}
