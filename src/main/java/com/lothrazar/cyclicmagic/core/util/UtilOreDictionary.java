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
package com.lothrazar.cyclicmagic.core.util;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UtilOreDictionary {

  public static boolean doesMatchOreDict(final ItemStack stack, final String oreId) {
    return doesMatchOreDict(stack, oreId, false);
  }

  public static boolean doesMatchOreDict(final ItemStack stack, final String oreId, boolean strict) {
    if (OreDictionary.doesOreNameExist(oreId)) {
      for (ItemStack stackCurrent : OreDictionary.getOres(oreId)) {
        if (OreDictionary.itemMatches(stackCurrent, stack, strict)) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean doesMatchOreDict(final ItemStack stack, final List<String> oreIds) {
    return doesMatchOreDict(stack, oreIds.toArray(new String[0]));
  }

  public static boolean doesMatchOreDict(final ItemStack stack, final String[] oreIds) {
    return doesMatchOreDict(stack, oreIds, false);
  }

  public static boolean doesMatchOreDict(ItemStack stack, String[] oreIds, boolean strict) {
    for (String oreId : oreIds) {
      if (doesMatchOreDict(stack, oreId, strict)) {
        return true;
      }
    }
    return false;
  }

  public static boolean itemMatchesAllowAir(ItemStack toMatch, ItemStack stack) {
    if (toMatch.isEmpty() || stack.isEmpty()) {
      return true;
    }
    return OreDictionary.itemMatches(toMatch, stack, false);
  }
}
