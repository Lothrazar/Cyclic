package com.lothrazar.cyclic.block.solidifier;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class RecipeSolidifier<TileEntityBase> extends CyclicRecipe {

  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private FluidStack fluidInput;

  public RecipeSolidifier(ResourceLocation id,
      Ingredient in, Ingredient inSecond, Ingredient inThird, FluidStack fluid,
      ItemStack result) {
    super(id);
    this.result = result;
    this.fluidInput = fluid;
    ingredients.add(in);
    ingredients.add(inSecond);
    ingredients.add(inThird);
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.base.TileEntityBase inv, Level worldIn) {
    try {
      TileSolidifier tile = (TileSolidifier) inv;
      return matchFluid(tile.getFluid()) && matchItems(tile);
    }
    catch (ClassCastException e) {
      return false; // i think we fixed this
    }
  }

  private int findMatchingSlot(TileSolidifier tile, Ingredient shapeless, final List<Integer> skip) {
    for (int i = 0; i < 3; i++) {
      if (skip.contains(i)) {
        continue; // we already matched this one
      }
      if (shapeless.test(tile.getStackInputSlot(i))) {
        return i;
      }
    }
    //
    return -1;
  }

  private boolean matchItems(TileSolidifier tile) {
    Ingredient top = ingredients.get(0);
    Ingredient middle = ingredients.get(1);
    Ingredient bottom = ingredients.get(2);
    //
    List<Integer> matchingSlots = new ArrayList<>();
    matchingSlots.add(findMatchingSlot(tile, top, matchingSlots));
    matchingSlots.add(findMatchingSlot(tile, middle, matchingSlots));
    matchingSlots.add(findMatchingSlot(tile, bottom, matchingSlots));
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
    Ingredient ing = ingredients.get(slot);
    return ing.getItems();
  }

  @Override
  public ItemStack getResultItem() {
    return result.copy();
  }

  @Override
  public FluidStack getRecipeFluid() {
    return fluidInput.copy();
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.SOLID;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SERIALIZER;
  }

  public static final SerializeSolidifier SERIALIZER = new SerializeSolidifier();

  @SuppressWarnings("rawtypes")
  public static class SerializeSolidifier extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase>> {

    SerializeSolidifier() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "solidifier"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public RecipeSolidifier<? extends com.lothrazar.cyclic.base.TileEntityBase> fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeSolidifier r = null;
      try {
        //TODO: in 1.17 make array
        Ingredient inputTop = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "inputTop"));
        Ingredient inputMiddle = Ingredient.EMPTY;
        if (GsonHelper.isValidNode(json, "inputMiddle")) {
          inputMiddle = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "inputMiddle"));
        }
        Ingredient inputBottom = Ingredient.EMPTY;
        if (GsonHelper.isValidNode(json, "inputBottom")) {
          inputBottom = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "inputBottom"));
        }
        ItemStack resultStack = ShapedRecipe.itemFromJson(GsonHelper.getAsJsonObject(json, "result"));
        if (inputTop == Ingredient.EMPTY) {
          throw new IllegalArgumentException("Invalid items: inputTop required to be non-empty: " + json);
        }
        FluidStack fs = getFluid(json, "mix");
        //valid recipe created
        r = new RecipeSolidifier(recipeId, inputTop, inputMiddle, inputBottom, fs, resultStack);
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
      }
      ModCyclic.LOGGER.info("Recipe loaded " + r.getId().toString());
      return r;
    }

    @Override
    public RecipeSolidifier fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      RecipeSolidifier r = new RecipeSolidifier(recipeId,
          Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), Ingredient.fromNetwork(buffer), FluidStack.readFromPacket(buffer),
          buffer.readItem());
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeSolidifier recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      Ingredient one = (Ingredient) recipe.ingredients.get(1);
      Ingredient two = (Ingredient) recipe.ingredients.get(2);
      zero.toNetwork(buffer);
      one.toNetwork(buffer);
      two.toNetwork(buffer);
      recipe.fluidInput.writeToPacket(buffer);
      buffer.writeItem(recipe.getResultItem());
    }
  }
}
