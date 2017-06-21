package com.lothrazar.cyclicmagic.registry;
import java.util.List;
import javax.annotation.Nonnull;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 
 * parts of this entire file as of 1.12 (not just this subclass) are from OSS
 * https://github.com/TechReborn/RebornCore/blob/1.12/src/main/java/reborncore/common/util/RebornCraftingHelper.java
 * copy pasted their notice here * Copyright (c) 2017 modmuss50 and Gigabit101
 *
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 *
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 * @author Sam
 *
 */
public class RecipeRegistry {
  public static class Util1pt12 {
    private static ResourceLocation buildName(ItemStack output) {
      ResourceLocation firstTry = new ResourceLocation(Const.MODID, output.getUnlocalizedName());
      int index = 0;
      while (CraftingManager.REGISTRY.containsKey(firstTry)) { // REGISTRY
        index++;
        firstTry = new ResourceLocation(Const.MODID, firstTry.getResourcePath() + "_" + index);
      }
      return firstTry;
    }
    private static NonNullList<Ingredient> convertToNonNullList(Object[] input) {
      NonNullList<Ingredient> list = NonNullList.create();
      for (Object any : input) {
        if (any instanceof Ingredient) {
          list.add((Ingredient) any);
        }
        else {
          Ingredient ing = CraftingHelper.getIngredient(any);
          if (ing == null) {
            ing = Ingredient.EMPTY;// EMPTY/.. same as new Ingredient(new ItemStack[0])
          }
          list.add(ing);
        }
      }
      return list;
    }
  }
  /**
   * wrapper for Forge addShapeless recipe, except the difference is this
   * returns it after registering it
   * 
   * so
   * 
   * @param stack
   * @param recipeComponents
   * @return
   */
  public static IRecipe addShapelessRecipe(ItemStack stack, Object... recipeComponents) {
    List<ItemStack> list = Lists.<ItemStack> newArrayList();
    for (Object object : recipeComponents) {
      if (object instanceof ItemStack) {
        list.add(((ItemStack) object).copy());
      }
      else if (object instanceof Item) {
        list.add(new ItemStack((Item) object));
      }
      else {
        if (!(object instanceof Block)) { throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!"); }
        list.add(new ItemStack((Block) object));
      }
    }
    ResourceLocation location = Util1pt12.buildName(stack);
    ShapelessRecipes recipe = new ShapelessRecipes(location.getResourceDomain(), stack, Util1pt12.convertToNonNullList(recipeComponents));
    recipe.setRegistryName(location);
    GameRegistry.register(recipe);
    //    IRecipe recipe = new ShapelessRecipes(stack, list);
    //    GameRegistry.addRecipe(recipe);
    return recipe;
  }
  /**
   * thin wrapper for addShapedRecipe
   * 
   * @param output
   * @param params
   * @return
   */
  //  public static IRecipe addShapedRecipe(@Nonnull ItemStack output, Object... params) {
  //    return GameRegistry.addShapedRecipe(output, params);
  //  }
  public static IRecipe addShapedRecipe(ItemStack output, Object... params) {
    ResourceLocation location = Util1pt12.buildName(output);
    CraftingHelper.ShapedPrimer primer = CraftingHelper.parseShaped(params);
    ShapedRecipes recipe = new ShapedRecipes(output.getItem().getRegistryName().toString(), primer.width, primer.height, primer.input, output);
    recipe.setRegistryName(location);
    GameRegistry.register(recipe);
    return recipe;
  }
  public static IRecipe addRecipe(@Nonnull ItemStack output, Object... params) {
    return addShapedRecipe(output, params);
  }
}
