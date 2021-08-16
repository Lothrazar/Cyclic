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

import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class UtilString {

  /**
   * One day i might make this a setting or an input arg for now i have no use to turn it off
   */
  public static final boolean matchWildcard = true;

  public static boolean hasOverlap(final List<String> list, final List<String> other) {
    for (String s : list) {
      for (String o : other) {
        if (s.equals(o)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * If the list has "hc:*_sapling" and input is "hc:whatever_sapling" then match is true
   *
   * @param list
   * @param toMatch
   * @return
   */
  public static boolean isInList(final List<ResourceLocation> list, ResourceLocation toMatch) {
    if (toMatch == null || list == null) {
      return false;
    }

    for (ResourceLocation resourceLocationInList : list) {
      //Definitely not a match
      if (!resourceLocationInList.getNamespace().equals(toMatch.getNamespace())) {
        continue;
      }
      //Perfect match
      if (resourceLocationInList.getPath().equals(toMatch.getPath())) {
        return true;
      }
      if (matchWildcard && resourceLocationInList.getPath().contains("*")) {
        String blockIdToMatch = toMatch.getPath();
        //resourceLocationInList.getPath() has the *
        String blockIdListWC = resourceLocationInList.getPath().replace("*", "");
        if (blockIdToMatch.contains(blockIdListWC)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * TODO: make a unit testing module, or install a framework for now these pass so i removed call to them
   */
  public static void unitTests() {
    NonNullList<ResourceLocation> blacklist = NonNullList.from(new ResourceLocation("", ""),
        new ResourceLocation("terraqueous:pergola"),
            new ResourceLocation("harvestcraft:*_sapling"),
            new ResourceLocation("croparia:block_cane_*")
    );
    ModCyclic.logger.logTestResult("1] expect true " + isInList(blacklist, new ResourceLocation("harvestcraft:fruit_sapling")));
    ModCyclic.logger.logTestResult("2] expect true " + isInList(blacklist, new ResourceLocation("croparia:block_cane_zzzzzz")));
    ModCyclic.logger.logTestResult("3] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:pampeach")));
    ModCyclic.logger.logTestResult("4] expect false " + isInList(blacklist, new ResourceLocation("harvestcraft:groundtrap")));
  }
}
