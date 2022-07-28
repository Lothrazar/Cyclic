package com.lothrazar.cyclic.block.crusher;

import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.ingredient.EnergyIngredient;
import com.lothrazar.cyclic.recipe.ingredient.RandomizedOutputIngredient;
import com.lothrazar.cyclic.registry.CyclicRecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class RecipeCrusher implements Recipe<TileCrusher> {

  private final ResourceLocation id;
  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  public final EnergyIngredient energy;
  public RandomizedOutputIngredient randOutput;

  public RecipeCrusher(ResourceLocation id, Ingredient in, EnergyIngredient energy, ItemStack out, RandomizedOutputIngredient randOutput) {
    this.id = id;
    this.energy = energy;
    ingredients.add(in);
    this.result = out;
    this.randOutput = randOutput;
  }

  @Override
  public ResourceLocation getId() {
    return id;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean matches(TileCrusher inv, Level worldIn) {
    try {
      return matches(inv.inputSlots.getStackInSlot(0), ingredients.get(0));
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
  public ItemStack getResultItem() {
    return result;
  }

  @Override
  public RecipeType<?> getType() {
    return CyclicRecipeType.CRUSHER.get();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CyclicRecipeType.CRUSHER_S.get();
  }

  public static class SerializeCrusher implements RecipeSerializer<RecipeCrusher> {

    public SerializeCrusher() {}

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @Override
    public RecipeCrusher fromJson(ResourceLocation recipeId, JsonObject json) {
      try {
        Ingredient inputFirst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        RecipeCrusher r = new RecipeCrusher(recipeId, inputFirst, new EnergyIngredient(json), resultStack, new RandomizedOutputIngredient(json));
        return r;
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
        return null;
      }
    }

    @Override
    public RecipeCrusher fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      // ing, (int, int), item, int, item
      RecipeCrusher r = new RecipeCrusher(recipeId, Ingredient.fromNetwork(buffer), new EnergyIngredient(buffer.readInt(), buffer.readInt()), buffer.readItem(),
          new RandomizedOutputIngredient(buffer.readInt(), buffer.readItem()));
      //server reading recipe from client or vice/versa 
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeCrusher recipe) {
      Ingredient zero = recipe.ingredients.get(0);
      zero.toNetwork(buffer);
      buffer.writeInt(recipe.energy.getRfPertick());
      buffer.writeInt(recipe.energy.getTicks());
      //
      buffer.writeItem(recipe.getResultItem());
      buffer.writeInt(recipe.randOutput.percent);
      buffer.writeItem(recipe.randOutput.bonus);
    }
  }
  //optional recipes for grinder ores / other mod ores
  //  

  public ItemStack createBonus(RandomSource rand) {
    ItemStack getBonus = this.randOutput.bonus.copy();
    if (this.randOutput.bonus.getCount() > 1) {
      //if its 1 just leave it. otherwise RNG
      //so if getCount==3 , then get rand [0,2] + 1 = [1,3]
      getBonus.setCount(1 + rand.nextInt(this.randOutput.bonus.getCount()));
    }
    return getBonus;
  }

  @Override
  public ItemStack assemble(TileCrusher t) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width <= 1 && height <= 1;
  }
}
