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
package com.lothrazar.cyclicmagic.util;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UtilOreDictionary {
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String oreId) {
    return doesMatchOreDict(stackIn, oreId, false);
  }
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String oreId, boolean strict) {
    if (OreDictionary.doesOreNameExist(oreId)) {
      for (ItemStack stackCurrent : OreDictionary.getOres(oreId)) {
        if (OreDictionary.itemMatches(stackCurrent, stackIn, strict)) {
          return true;
        }
      }
    }
    return false;
  }
  public static boolean doesMatchOreDict(final ItemStack stackIn, final List<String> oreIds) {
    return doesMatchOreDict(stackIn, oreIds.toArray(new String[0]));
  }
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String[] oreIds) {
    return doesMatchOreDict(stackIn, oreIds, false);
  }
  public static boolean doesMatchOreDict(ItemStack stackIn, String[] oreIds, boolean strict) {
    for (String oreId : oreIds) {
      if (doesMatchOreDict(stackIn, oreId, strict)) {
        return true;
      }
    }
    return false;
  }
  public static boolean itemMatchesAllowAir(ItemStack toMatch, ItemStack itemStack) {
    if (toMatch.isEmpty() || itemStack.isEmpty()) {
      return true;
    }
    return OreDictionary.itemMatches(toMatch, itemStack, false);
  }
}
