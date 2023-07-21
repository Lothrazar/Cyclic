package com.lothrazar.cyclic.block.packager;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.Tags;

/**
 * https://github.com/Lothrazar/Cyclic/commit/2cd2376fd07685414b5a8a2a52250caab8143d9b#diff-f5ee2272c17948b8822c0020ec2b3f35b8dea7ec714d70a11355717c9d3a92f0
 * 
 * Packager PR @author https://github.com/Lothrazar/Cyclic/pull/2013
 *
 */
public class UtilPackager {

  private static final Map<Item, CraftingRecipe> fourItemRecipeCache = new HashMap<>();
  private static final Map<Item, CraftingRecipe> nineItemRecipeCache = new HashMap<>();
  private static final Map<CraftingRecipe, Integer> ingredientsInRecipeCache = new HashMap<>();
  private static final Map<Item, Boolean> itemValidCache = new HashMap<>();
  private static final Map<CraftingRecipe, Boolean> recipeValidCache = new HashMap<>();

  private UtilPackager() {}

  private static Map<Item, CraftingRecipe> getFourItemRecipeCache(final RecipeManager recipeManager) {
    if (fourItemRecipeCache.isEmpty()) {
      buildRecipeCaches(recipeManager);
    }
    return fourItemRecipeCache;
  }

  public static int getIngredientsInRecipe(final CraftingRecipe recipe) {
    return ingredientsInRecipeCache.computeIfAbsent(recipe, k -> {
      int count = 0;
      for (final Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == Ingredient.EMPTY) {
          continue;
        }
        final ItemStack[] matchingStacks = ingredient.getItems();
        if (matchingStacks.length == 0) {
          continue;
        }
        count++;
      }
      return count;
    });
  }

  private static Map<Item, CraftingRecipe> getNineItemRecipeCache(final RecipeManager recipeManager) {
    if (nineItemRecipeCache.isEmpty()) {
      buildRecipeCaches(recipeManager);
    }
    return nineItemRecipeCache;
  }

  public static CraftingRecipe getRecipeForItemStack(final RecipeManager recipeManager, final ItemStack itemStack) {
    if (itemStack.getCount() >= 9) {
      final CraftingRecipe recipe = getNineItemRecipeCache(recipeManager).get(itemStack.getItem());
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

  public static boolean isRecipeValid(final CraftingRecipe recipe) {
    return recipeValidCache.computeIfAbsent(recipe, k -> {
      final ItemStack recipeOutput = recipe.getResultItem();
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
        final ItemStack[] matchingStacks = ingredient.getItems(); //.getMatchingStacks();
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
      boolean outIsStorage = recipeOutput.is(Tags.Items.STORAGE_BLOCKS);
      final Item mainIngredientItem = mainIngredientStack.getItem();
      boolean inIsIngot = mainIngredientStack.is(Tags.Items.INGOTS);
      if (!outIsStorage && inIsIngot) {
        return false;
      }
      return count == 4 || count == 9;
    });
  }

  public static void buildRecipeCaches(final RecipeManager recipeManager) {
    recipeLoop: for (final CraftingRecipe recipe : recipeManager.getAllRecipesFor(RecipeType.CRAFTING)) {
      final ItemStack recipeOutput = recipe.getResultItem();
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
        final ItemStack[] matchingStacks = ingredient.getItems();
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
      boolean outIsStorage = recipeOutput.is(Tags.Items.STORAGE_BLOCKS);
      final Item mainIngredientItem = mainIngredientStack.getItem();
      boolean inIsIngot = mainIngredientStack.is(Tags.Items.INGOTS);
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
