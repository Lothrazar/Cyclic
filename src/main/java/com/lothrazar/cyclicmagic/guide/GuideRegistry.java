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
package com.lothrazar.cyclicmagic.guide;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

public class GuideRegistry {

  private static List<GuideItem> items = new ArrayList<GuideItem>();
  private static final String SUFFIX = ".guide";

  public static GuideItem register(Enchantment ench, @Nonnull List<String> args) {
    args.add(ench.getRarity().name().toLowerCase().replace("_", " "));
    return register(GuideCategory.ENCHANT, Items.ENCHANTED_BOOK, ench.getName(), ench.getName() + SUFFIX, null, args);
  }

  public static GuideItem register(GuideCategory cat, Block block) {
    //    String pageTitle = block.getUnlocalizedName() + ".name";
    //    String text = block.getUnlocalizedName() + SUFFIX;
    return register(cat, block, null, null);
  }

  public static GuideItem register(GuideCategory cat, Block block, @Nullable IRecipe recipe, @Nullable List<String> args) {
    String pageTitle = block.getUnlocalizedName() + ".name";
    String text = block.getUnlocalizedName() + SUFFIX;
    return register(cat, Item.getItemFromBlock(block), pageTitle, text, recipe, args);
  }

  public static GuideItem register(GuideCategory cat, Item item) {
    return register(cat, item, (IRecipe) null, null);
  }

  public static GuideItem register(GuideCategory cat, Item item, @Nullable IRecipe recipe, @Nullable List<String> args) {
    String pageTitle = item.getUnlocalizedName() + ".name";
    String above = item.getUnlocalizedName() + SUFFIX;
    return register(cat, item, pageTitle, above, recipe, args);
  }

  public static GuideItem register(GuideCategory cat, Item icon, String title, String text) {
    return register(cat, icon, title, text, null, null);
  }

  public static GuideItem register(GuideCategory cat, @Nonnull Item icon, String title, String text, @Nullable IRecipe recipes, @Nullable List<String> args) {
    //layer of seperation between guidebook api. 1 for optional include and 2 in case i ever need to split it out and 3 for easy registering
    if (args != null && args.size() > 0) {
      text = UtilChat.lang(text);
      for (int i = 0; i < args.size(); i++) {
        //in the lang file we have something like "Rarity is $1, similar to $2 "
        //so the text is translatable but we swap out values like this
        text = text.replace("$" + i, args.get(i));
      }
    }
    GuideItem itempage = new GuideItem(cat, icon, title, text, recipes);
    items.add(itempage);
    return itempage;
  }

  /**
   * to create an empty line item without any pages
   * 
   * @param cat
   * @param icon
   * @param title
   * @return
   */
  public static GuideItem register(GuideCategory cat, Item icon, String title) {
    GuideItem itempage = new GuideItem(cat, icon, title, null, null);
    items.add(itempage);
    return itempage;
  }

  public static List<GuideItem> getItems() {
    return items;
  }
}
