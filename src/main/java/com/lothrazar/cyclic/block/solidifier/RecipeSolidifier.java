package com.lothrazar.cyclic.block.solidifier;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.ingredient.EnergyIngredient;
import com.lothrazar.cyclic.recipe.ingredient.FluidTagIngredient;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.cyclic.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

public class RecipeSolidifier implements Recipe<TileSolidifier> {

  private final ResourceLocation id;
  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;
  public final FluidTagIngredient fluidIngredient;

  public RecipeSolidifier(ResourceLocation id, NonNullList<Ingredient> inList,
      FluidTagIngredient fluid, ItemStack result, EnergyIngredient energy) {
    this.id = id;
    this.energy = energy;
    ingredients = inList;
    if (ingredients.size() == 2) {
      ingredients.add(Ingredient.EMPTY);
    }
    else if (ingredients.size() == 1) {
      ingredients.add(Ingredient.EMPTY);
      ingredients.add(Ingredient.EMPTY);
    }
    if (ingredients.size() != 3) {
      throw new IllegalArgumentException("Solidifier recipe must have at most three ingredients");
    }
    this.fluidIngredient = fluid;
    this.result = result;
  }

  //  @Override
  public FluidStack getRecipeFluid() {
    return this.fluidIngredient.getFluidStack();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack assemble(TileSolidifier inv) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  public EnergyIngredient getEnergy() {
    return energy;
  }

  @Override
  public boolean matches(TileSolidifier inv, Level worldIn) {
    try {
      TileSolidifier tile = inv;
      return matchItems(tile) && RecipeUtil.matchFluid(tile.getFluid(), this.fluidIngredient);
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

  public Ingredient at(int slot) {
    return ingredients.get(slot);
  }

  private boolean matchItems(TileSolidifier tile) {
    Ingredient top = at(0);
    Ingredient middle = at(1);
    Ingredient bottom = at(2);
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
    Ingredient ing = at(slot);
    return ing.getItems();
  }

  @Override
  public ItemStack getResultItem() {
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

    @Override
    public RecipeSolidifier fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeSolidifier r = null;
      try {
        NonNullList<Ingredient> list = RecipeUtil.getIngredientsArray(json);
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        FluidTagIngredient fs = RecipeUtil.parseFluid(json, "mix");
        r = new RecipeSolidifier(recipeId, list, fs, resultStack, new EnergyIngredient(json));
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe " + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeSolidifier fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buf) {
      // ing,ing,ing, fluid, item, int, int
      NonNullList<Ingredient> ins = NonNullList.create();
      ins.add(Ingredient.fromNetwork(buf));
      ins.add(Ingredient.fromNetwork(buf));
      ins.add(Ingredient.fromNetwork(buf));
      FluidTagIngredient fsi = FluidTagIngredient.readFromPacket(buf);
      RecipeSolidifier r = new RecipeSolidifier(recipeId,
          ins, fsi,
          buf.readItem(),
          new EnergyIngredient(buf.readInt(), buf.readInt()));
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, RecipeSolidifier recipe) {
      Ingredient zero = recipe.ingredients.get(0);
      Ingredient one = recipe.ingredients.get(1);
      Ingredient two = recipe.ingredients.get(2);
      zero.toNetwork(buf);
      one.toNetwork(buf);
      two.toNetwork(buf);
      recipe.fluidIngredient.writeToPacket(buf);
      buf.writeItem(recipe.getResultItem());
      buf.writeInt(recipe.energy.getRfPertick());
      buf.writeInt(recipe.energy.getTicks());
    }
  }
}
