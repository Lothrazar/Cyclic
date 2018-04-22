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
package com.lothrazar.cyclicmagic.creativetab;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import com.lothrazar.cyclicmagic.potion.PotionTypeCyclic;
import com.lothrazar.cyclicmagic.registry.EnchantRegistry;
import com.lothrazar.cyclicmagic.registry.PotionTypeRegistry;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabCyclic extends CreativeTabs {

  Item tabItem = null;
  Comparator<ItemStack> comparator = new Comparator<ItemStack>() {

    @Override
    public int compare(final ItemStack first, final ItemStack second) {
      return first.getDisplayName().compareTo(second.getDisplayName());
    }
  };

  public CreativeTabCyclic() {
    super(Const.MODID);
  }

  @Override
  public ItemStack getTabIconItem() {
    return tabItem == null ? new ItemStack(Items.DIAMOND) : new ItemStack(tabItem);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void displayAllRelevantItems(NonNullList<ItemStack> list) {
    super.displayAllRelevantItems(list);
    Iterator<ItemStack> i = list.iterator();
    Item guidebook = Item.getByNameOrId("guideapi:" + Const.MODID + "-guide");
    while (i.hasNext()) {
      Item s = i.next().getItem(); // must be called before you can call i.remove()
      if (s == Items.ENCHANTED_BOOK
          || s == Items.LINGERING_POTION
          || s == Items.POTIONITEM
          || s == Items.SPLASH_POTION
          || s == Items.TIPPED_ARROW
          || s == guidebook) {
        i.remove();
      }
    }
    Collections.sort(list, comparator);
    for (Enchantment e : EnchantRegistry.enchants) {
      ItemStack ebook = new ItemStack(Items.ENCHANTED_BOOK);
      ItemEnchantedBook.addEnchantment(ebook, new EnchantmentData(e, e.getMaxLevel()));
      list.add(ebook);
    }
    for (PotionTypeCyclic pt : PotionTypeRegistry.potions) {
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), pt));
      list.add(PotionUtils.addPotionToItemStack(new ItemStack(Items.TIPPED_ARROW), pt));
    }
    if (guidebook != null) {
      list.add(new ItemStack(guidebook));
    }
  }

  public void setTabItemIfNull(Item i) {
    if (tabItem == null)
      tabItem = i;
  }
}
