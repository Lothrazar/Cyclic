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
package com.lothrazar.cyclicmagic.block.imbue;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.imbue.BlockImbue.ImbueFlavor;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.EntityEntry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.cyclicmagic.Imbue")
@ZenRegister
public class RecipeImbueZen {
  //  @Optional.Method(modid = "crafttweaker")
  //  @ZenMethod
  //  public static void removeShapedRecipe(IItemStack output) {
  //    ItemStack out = toStack(output);
  //    for (RecipeHydrate rec : RecipeHydrate.recipesShaped) {
  //      if (rec.getRecipeOutput().isItemEqual(out)) {
  //        RecipeHydrate.recipesShaped.remove(rec);
  //        ModCyclic.logger.info("removed hydrator recipe for " + output.getDisplayName());
  //      }
  //    }
  //  }


  @Optional.Method(modid = "crafttweaker")
  @ZenMethod
  public static void addRecipe(String target, String flavor, String metadata, IItemStack[] inputs, int charges) {
    if (target.equals("bow") == false) {
      ModCyclic.logger.error("Invalid target " + target + "supported: bow");
      return;
    }
    ImbueFlavor theFlavor = null;
    String valid = "";
    for (ImbueFlavor s : ImbueFlavor.class.getEnumConstants()) {
      if (s.toString().toLowerCase().equals(flavor)) {
        theFlavor = s;
        break;
      }
      valid += s.toString().toLowerCase() + " ";
    }
    if (theFlavor == null) {
      ModCyclic.logger.error("Invalid flavor " + flavor + "supported: " + valid);
      return;
    }
    if (charges <= 0) {
      ModCyclic.logger.error("Charges must be zero " + charges);
      return;
    }
    //now parse meta 
    switch (theFlavor) {
      case EXPLOSION:
      break;
      case NONE:
      break;
      case POTION:
        //validate its a potion
        Potion effect = Potion.getPotionFromResourceLocation(metadata);
        if (effect == null) {
          ModCyclic.logger.error("Invalid Potion effect " + metadata);
          return;
        }
        RecipeImbue imb = new RecipeImbue(toStacks(inputs), effect, charges);
        BlockImbue.addRecipe(imb);
      break;
      case ENTITY:
        //       net.minecraftforge.fml.common.registry.
        EntityEntry entry = net.minecraftforge.fml.common.registry.ForgeRegistries.ENTITIES.getValue(new ResourceLocation(metadata));
        if (entry == null) {
          ModCyclic.logger.error("Invalid entity " + metadata);
          return;
        }
      break;
      default:
      break;
    }
    ModCyclic.logger.error("Imbue Recipe Added " + charges);
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
