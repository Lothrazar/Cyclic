package com.lothrazar.cyclic.block.packager;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.common.Tags;

public class UtilPackager {

  private static final Map<Item, ICraftingRecipe> fourItemRecipeCache = new HashMap<>();
  private static final Map<Item, ICraftingRecipe> nineItemRecipeCache = new HashMap<>();
  private static final Map<ICraftingRecipe, Integer> ingredientsInRecipeCache = new HashMap<>();
  private static final Map<Item, Boolean> itemValidCache = new HashMap<>();
  private static final Map<ICraftingRecipe, Boolean> recipeValidCache = new HashMap<>();

  private UtilPackager() {}

  private static Map<Item, ICraftingRecipe> getFourItemRecipeCache(final RecipeManager recipeManager) {
    if (fourItemRecipeCache.isEmpty()) {
      buildRecipeCaches(recipeManager);
    }
    return fourItemRecipeCache;
  }

  public static int getIngredientsInRecipe(final ICraftingRecipe recipe) {
    return ingredientsInRecipeCache.computeIfAbsent(recipe, k -> {
      int count = 0;
      for (final Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == Ingredient.EMPTY) {
          continue;
        }
        final ItemStack[] matchingStacks = ingredient.getMatchingStacks();
        if (matchingStacks.length == 0) {
          continue;
        }
        count++;
      }
      return count;
    });
  }

  private static Map<Item, ICraftingRecipe> getNineItemRecipeCache(final RecipeManager recipeManager) {
    if (nineItemRecipeCache.isEmpty()) {
      buildRecipeCaches(recipeManager);
    }
    return nineItemRecipeCache;
  }

  public static ICraftingRecipe getRecipeForItemStack(final RecipeManager recipeManager, final ItemStack itemStack) {
    if (itemStack.getCount() >= 9) {
      final ICraftingRecipe recipe = getNineItemRecipeCache(recipeManager).get(itemStack.getItem());
      if (recipe != null) {
        return recipe;
      }
    }
    if (itemStack.getCount() >= 4) {
      return getFourItemRecipeCache(recipeManager).get(itemStack.getItem());
    }
    return null;
  }

  public static boolean isItemStackValid(final RecipeManager recipeManager, final ItemStack itemStack) {
    return UtilPackager.itemValidCache.computeIfAbsent(itemStack.getItem(), item -> getFourItemRecipeCache(recipeManager).containsKey(item)
        || getNineItemRecipeCache(recipeManager).containsKey(item));
  }

  public static boolean isRecipeValid(final ICraftingRecipe recipe) {
    return recipeValidCache.computeIfAbsent(recipe, k -> {
      final ItemStack recipeOutput = recipe.getRecipeOutput();
      if (recipeOutput.getMaxStackSize() == 1 || recipeOutput.getCount() != 1) {
        return false;
      }
      Ingredient mainIngredient = null;
      ItemStack mainIngredientStack = null;
      int count = 0;
      for (final Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == Ingredient.EMPTY) {
          continue;
        }
        final ItemStack[] matchingStacks = ingredient.getMatchingStacks();
        if (matchingStacks.length == 0) {
          continue;
        }
        final ItemStack matchingStack = matchingStacks[0];
        if (mainIngredient != null && !mainIngredient.test(matchingStack)) {
          return false;
        }
        mainIngredient = ingredient;
        mainIngredientStack = matchingStack;
        count++;
      }
      if (mainIngredient == null) {
        return false;
      }
      boolean outIsStorage = recipeOutput.getItem().isIn(Tags.Items.STORAGE_BLOCKS);
      final Item mainIngredientItem = mainIngredientStack.getItem();
      boolean inIsIngot = mainIngredientItem.isIn(Tags.Items.INGOTS);
      if (!outIsStorage && inIsIngot) {
        return false;
      }
      return count == 4 || count == 9;
    });
  }

  public static void buildRecipeCaches(final RecipeManager recipeManager) {
    recipeLoop: for (final ICraftingRecipe recipe : recipeManager.getRecipesForType(IRecipeType.CRAFTING)) {
      final ItemStack recipeOutput = recipe.getRecipeOutput();
      if (recipeOutput.getMaxStackSize() == 1 || recipeOutput.getCount() != 1) {
        continue;
      }
      Ingredient mainIngredient = null;
      ItemStack mainIngredientStack = null;
      int count = 0;
      for (final Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == Ingredient.EMPTY) {
          continue;
        }
        final ItemStack[] matchingStacks = ingredient.getMatchingStacks();
        if (matchingStacks.length != 1) {
          recipeValidCache.put(recipe, false);
          continue recipeLoop;
        }
        final ItemStack matchingStack = matchingStacks[0];
        if (mainIngredient != null && !mainIngredient.test(matchingStack)) {
          recipeValidCache.put(recipe, false);
          continue recipeLoop;
        }
        mainIngredient = ingredient;
        mainIngredientStack = matchingStack;
        count++;
      }
      if (mainIngredient == null) {
        continue;
      }
      boolean outIsStorage = recipeOutput.getItem().isIn(Tags.Items.STORAGE_BLOCKS);
      final Item mainIngredientItem = mainIngredientStack.getItem();
      boolean inIsIngot = mainIngredientItem.isIn(Tags.Items.INGOTS);
      if (!outIsStorage && inIsIngot) {
        continue;
      }
      if (count == 4) {
        fourItemRecipeCache.put(mainIngredientItem, recipe);
        ingredientsInRecipeCache.put(recipe, 4);
        recipeValidCache.put(recipe, true);
      }
      else if (count == 9) {
        nineItemRecipeCache.put(mainIngredientItem, recipe);
        ingredientsInRecipeCache.put(recipe, 9);
        recipeValidCache.put(recipe, true);
      }
      else {
        recipeValidCache.put(recipe, false);
      }
    }
  }
}
