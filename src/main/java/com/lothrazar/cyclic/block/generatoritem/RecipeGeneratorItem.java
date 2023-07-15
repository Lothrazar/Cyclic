package com.lothrazar.cyclic.block.generatoritem;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import com.lothrazar.library.recipe.ingredient.EnergyIngredient;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class RecipeGeneratorItem implements Recipe<TileGeneratorDrops> {

  private final ResourceLocation id;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private final EnergyIngredient energy;

  public RecipeGeneratorItem(ResourceLocation id, Ingredient in, EnergyIngredient energy) {
    this.id = id;
    ingredients.add(in);
    this.energy = energy;
  }

  @Override
  public String toString() {
    return "RecipeGeneratorItem [ingredients=" + ingredients.get(0) + ", energy=" + energy + "]";
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public ItemStack assemble(TileGeneratorDrops inv, RegistryAccess ra) {
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
  public boolean matches(TileGeneratorDrops inv, Level worldIn) {
    try {
      TileGeneratorDrops tile = inv;
      return matches(tile.inputSlots.getStackInSlot(0), ingredients.get(0));
    }
    catch (ClassCastException e) {
      return false;
    }
  }

  public boolean matches(ItemStack current, Ingredient ing) {
    if (ing == Ingredient.EMPTY) {
      //it must be empty
      return current.isEmpty();
    }
    if (current.isEmpty()) {
      return ing == Ingredient.EMPTY;
    }
    return ing.test(current);
  }

  public ItemStack[] ingredientAt(int slot) {
    Ingredient ing = at(slot);
    return ing.getItems();
  }

  public Ingredient at(int slot) {
    return ingredients.get(slot);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return ingredients;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess ra) {
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

    public SerializeGenerateItem() {}

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @Override
    public RecipeGeneratorItem fromJson(ResourceLocation recipeId, JsonObject json) {
      RecipeGeneratorItem r = null;
      try {
        Ingredient inputFirst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "fuel"));
        r = new RecipeGeneratorItem(recipeId, inputFirst, new EnergyIngredient(json));
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe " + recipeId, e);
      }
      return r;
    }

    @Override
    public RecipeGeneratorItem fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      RecipeGeneratorItem r = new RecipeGeneratorItem(recipeId, Ingredient.fromNetwork(buffer),
          new EnergyIngredient(buffer.readInt(), buffer.readInt()));
      //server reading recipe from client or vice/versa 
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeGeneratorItem recipe) {
      Ingredient zero = recipe.ingredients.get(0);
      zero.toNetwork(buffer);
      buffer.writeInt(recipe.energy.getTicks());
      buffer.writeInt(recipe.energy.getRfPertick());
    }
  }
}
