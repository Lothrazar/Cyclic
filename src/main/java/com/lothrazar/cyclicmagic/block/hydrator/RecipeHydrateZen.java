/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.hydrator;

import com.lothrazar.cyclicmagic.ModCyclic;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.cyclicmagic.Hydrator")
@ZenRegister
public class RecipeHydrateZen {

  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeShapedRecipe(IItemStack output) {
    ItemStack out = toStack(output);
    for (RecipeHydrate rec : RecipeHydrate.recipes) {
      if (rec.getRecipeOutput().isItemEqual(out)) {
        RecipeHydrate.recipes.remove(rec);
        ModCyclic.logger.info("ZenScript: removed hydrator recipe for " + output.getDisplayName());
      }
    }
  }

  @Deprecated
  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeShapelessRecipe(IItemStack output) {
    removeShapedRecipe(output);
  }

  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void addRecipe(IItemStack output, IItemStack[] inputs, int water) {
    ModCyclic.logger.info("ZenScript: added hydrator recipe for " + output.getDisplayName());
    RecipeHydrate.addRecipe(new RecipeHydrate(toStacks(inputs), toStack(output), water));
  }

  /**
   * THANKS TO https://github.com/jaredlll08/MTLib/blob/1.12/src/main/java/com/blamejared/mtlib/helpers/InputHelper.java @ https://github.com/jaredlll08/MTLib which is MIT license
   * https://github.com/jaredlll08/MTLib/blob/1.12/LICENSE.md
   */
  @Optional.Method(modid = "crafttweaker")
  public static ItemStack toStack(IItemStack iStack) {
    if (iStack == null) {
      return ItemStack.EMPTY;
    }
    else {
      Object internal = iStack.getInternal();
      if (!(internal instanceof ItemStack)) {
        return ItemStack.EMPTY;
      }
      return (ItemStack) internal;
    }
  }

  /**
   * THANKS TO https://github.com/jaredlll08/MTLib/blob/1.12/src/main/java/com/blamejared/mtlib/helpers/InputHelper.java @ https://github.com/jaredlll08/MTLib which is MIT license
   * https://github.com/jaredlll08/MTLib/blob/1.12/LICENSE.md
   */
  @Optional.Method(modid = "crafttweaker")
  public static ItemStack[] toStacks(IItemStack[] iStack) {
    if (iStack == null) {
      return null;
    }
    else {
      ItemStack[] output = new ItemStack[iStack.length];
      for (int i = 0; i < iStack.length; i++) {
        output[i] = toStack(iStack[i]);
      }
      return output;
    }
  }
}
