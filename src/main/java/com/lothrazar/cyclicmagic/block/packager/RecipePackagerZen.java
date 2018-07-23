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
package com.lothrazar.cyclicmagic.block.packager;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrateZen;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.cyclicmagic.Packager")
@ZenRegister
public class RecipePackagerZen {

  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void removeShapedRecipe(IItemStack output) {

    ItemStack out = RecipeHydrateZen.toStack(output);
    for (RecipePackage rec : RecipePackage.recipes) {
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
  public static void addRecipe(IItemStack output, IItemStack inputs) {

    ModCyclic.logger.info("ZenScript: added packager recipe for " + output.getDisplayName());
    RecipePackage.addRecipe(new RecipePackage(RecipeHydrateZen.toStack(inputs), RecipeHydrateZen.toStack(output)));
  }

}
