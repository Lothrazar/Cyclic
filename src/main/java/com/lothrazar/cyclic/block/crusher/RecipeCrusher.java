package com.lothrazar.cyclic.block.crusher;

import java.util.Random;
import com.google.gson.JsonObject;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.recipe.CyclicRecipe;
import com.lothrazar.cyclic.recipe.CyclicRecipeType;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

@SuppressWarnings("rawtypes")
public class RecipeCrusher<TileEntityBase> extends CyclicRecipe {

  private ItemStack result = ItemStack.EMPTY;
  private NonNullList<Ingredient> ingredients = NonNullList.create();
  private int ticks;
  private int rfpertick;
  public ItemStack bonus = ItemStack.EMPTY;
  public int percent;

  public RecipeCrusher(ResourceLocation id, Ingredient in, int ticks, int rfpertick, ItemStack out, int percIn, ItemStack optional) {
    super(id);
    ingredients.add(in);
    this.setTicks(Math.max(1, ticks));
    this.rfpertick = Math.max(1, rfpertick);
    this.result = out;
    this.percent = Math.max(0, percIn);
    if (percIn > 100) {
      percIn = 100;
    }
    bonus = optional;
  }

  @Override
  public boolean matches(com.lothrazar.cyclic.block.TileBlockEntityCyclic inv, Level worldIn) {
    try {
      TileCrusher tile = (TileCrusher) inv;
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
    Ingredient ing = ingredients.get(slot);
    return ing.getItems();
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
    return CyclicRecipeType.CRUSHER;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return SERIALCRUSH;
  }

  public int getTicks() {
    return ticks;
  }

  public void setTicks(int ticks) {
    this.ticks = ticks;
  }

  public int getRfpertick() {
    return rfpertick;
  }

  public void setRfpertick(int rfpertick) {
    this.rfpertick = rfpertick;
  }

  public int getRfTotal() {
    return this.getRfpertick() * this.getTicks();
  }

  public static final SerializeGenerateItem SERIALCRUSH = new SerializeGenerateItem();

  public static class SerializeGenerateItem extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RecipeCrusher<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic>> {

    SerializeGenerateItem() {
      // This registry name is what people will specify in their json files.
      this.setRegistryName(new ResourceLocation(ModCyclic.MODID, "crusher"));
    }

    /**
     * The fluid stuff i was helped out a ton by looking at this https://github.com/mekanism/Mekanism/blob/921d10be54f97518c1f0cb5a6fc64bf47d5e6773/src/api/java/mekanism/api/SerializerHelper.java#L129
     */
    @SuppressWarnings("unchecked")
    @Override
    public RecipeCrusher<? extends com.lothrazar.cyclic.block.TileBlockEntityCyclic> fromJson(ResourceLocation recipeId, JsonObject json) {
      try {
        Ingredient inputFirst = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        JsonObject energy = json.get("energy").getAsJsonObject();
        int ticks = energy.get("ticks").getAsInt();
        int rfpertick = energy.get("rfpertick").getAsInt();
        ItemStack resultStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        int percent = 0;
        ItemStack bonusStack = ItemStack.EMPTY;
        if (json.has("bonus") && json.has("percent")) {
          bonusStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "bonus"));
          percent = json.get("percent").getAsInt();
        }
        RecipeCrusher r = new RecipeCrusher(recipeId, inputFirst, ticks, rfpertick, resultStack, percent, bonusStack);
        return r;
      }
      catch (Exception e) {
        ModCyclic.LOGGER.error("Error loading recipe" + recipeId, e);
        return null;
      }
    }

    @Override
    public RecipeCrusher fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
      RecipeCrusher r = new RecipeCrusher(recipeId, Ingredient.fromNetwork(buffer), buffer.readInt(), buffer.readInt(), buffer.readItem(), buffer.readInt(), buffer.readItem());
      //server reading recipe from client or vice/versa 
      return r;
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RecipeCrusher recipe) {
      Ingredient zero = (Ingredient) recipe.ingredients.get(0);
      zero.toNetwork(buffer);
      buffer.writeInt(recipe.getTicks());
      buffer.writeInt(recipe.rfpertick);
      //
      buffer.writeItem(recipe.getResultItem());
      buffer.writeInt(recipe.percent);
      buffer.writeItem(recipe.bonus);
    }
  }
  //optional recipes for grinder ores / other mod ores
  //  

  public ItemStack createBonus(Random rand) {
    ItemStack getBonus = this.bonus.copy();
    if (this.bonus.getCount() > 1) {
      //if its 1 just leave it. otherwise RNG
      //so if getCount==3 , then get rand [0,2] + 1 = [1,3]
      getBonus.setCount(1 + rand.nextInt(this.bonus.getCount()));
    }
    return getBonus;
  }
}
