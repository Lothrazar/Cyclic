package com.lothrazar.cyclic.recipe.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;

public class RandomizedOutputIngredient {

  private static final String KEY_PERCENT = "percent";
  private static final String KEY_BONUS = "bonus";
  public ItemStack bonus = ItemStack.EMPTY;
  public int percent;

  public RandomizedOutputIngredient(JsonObject json) {
    parseData(json);
  }

  public RandomizedOutputIngredient(int readInt, ItemStack readItem) {
    this.percent = readInt;
    this.bonus = readItem;
  }

  private void parseData(JsonObject json) {
    if (json.has(KEY_BONUS) && json.has(KEY_PERCENT)) {
      bonus = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, KEY_BONUS));
      percent = json.get(KEY_PERCENT).getAsInt();
      percent = Math.max(0, percent);
      if (percent > 100) {
        percent = 100;
      }
    }
  }
}
