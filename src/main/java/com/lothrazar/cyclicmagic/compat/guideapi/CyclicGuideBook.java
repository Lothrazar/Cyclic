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
package com.lothrazar.cyclicmagic.compat.guideapi;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideItem;
import com.lothrazar.cyclicmagic.guide.GuidePage;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.data.Const;
import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.api.util.PageHelper;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageBrewingRecipe;
import amerifrance.guideapi.page.PageIRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "amerifrance.guideapi.api.GuideAPI", modid = "guideapi", striprefs = true)
@GuideBook
public class CyclicGuideBook implements IGuideBook {

  //private static final String GUIDE_API_MOD_ID = "guideapi";
  private static final int MAX_PAGE_LENGTH = 314;
  private static Book book;
  private List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesBlocks = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesItems = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesGear = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesWorld = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesEnchants = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesBlockMachine = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesBlockPlate = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesItemBaubles = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesItemThrow = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesTransport = new HashMap<ResourceLocation, EntryAbstract>();

  private void addEntry(GuideCategory cat, List<IPage> page, String pageTitle, ItemStack icon) {
    switch (cat) {
      case BLOCK:
        entriesBlocks.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case ITEM:
        entriesItems.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case GEAR:
        entriesGear.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case WORLD:
        entriesWorld.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case ENCHANT:
        entriesEnchants.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case BLOCKMACHINE:
        entriesBlockMachine.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case BLOCKPLATE:
        entriesBlockPlate.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case ITEMBAUBLES:
        entriesItemBaubles.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case ITEMTHROW:
        entriesItemThrow.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case TRANSPORT:
        entriesTransport.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      default:
      break;
    }
  }

  private void buildPages() {
    List<GuideItem> items = GuideRegistry.getItems();
    Comparator<GuideItem> comparator = new Comparator<GuideItem>() {

      @Override
      public int compare(final GuideItem first, final GuideItem second) {
        return first.title.compareTo(second.title);
      }
    };
    Collections.sort(items, comparator);
    for (GuideItem item : items) {
      List<IPage> pages = new ArrayList<IPage>();
      for (GuidePage p : item.pages) {
        if (p.text != null) {
          for (IPage textPage : PageHelper.pagesForLongText(p.text, MAX_PAGE_LENGTH)) {
            pages.add(textPage);
          }
        }
        if (p.recipe != null) {
          pages.add(new PageIRecipe(p.recipe));
        }
        if (p.brewRecipe != null) {
          pages.add(new PageBrewingRecipe(p.brewRecipe));
        }
      }
      if (item.cat == null) {
        item.cat = GuideCategory.WORLD;
      }
      addEntry(item.cat, pages, item.title, new ItemStack(item.icon));
    }
  }

  @Override
  public Book buildBook() {
    buildPages();
    buildCategories();
    buildBookItem();
    return book;
  }

  private void buildCategories() {
    addCategory(entriesBlocks, GuideCategory.BLOCK);
    addCategory(entriesBlockMachine, GuideCategory.BLOCKMACHINE);
    addCategory(entriesItems, GuideCategory.ITEM);
    addCategory(entriesGear, GuideCategory.GEAR);
    //    addCategory(entriesPotion, GuideCategory.POTION);
    addCategory(entriesEnchants, GuideCategory.ENCHANT);
    addCategory(entriesWorld, GuideCategory.WORLD);
    addCategory(entriesBlockPlate, GuideCategory.BLOCKPLATE);
    addCategory(entriesItemBaubles, GuideCategory.ITEMBAUBLES);
    addCategory(entriesItemThrow, GuideCategory.ITEMTHROW);
    addCategory(entriesTransport, GuideCategory.TRANSPORT);
  }

  private void addCategory(Map<ResourceLocation, EntryAbstract> entriesBlockPlate,
      GuideCategory cat) {
    categories.add(new CategoryItemStack(entriesBlockPlate,
        cat.text(), cat.icon()));
  }

  private void buildBookItem() {
    book = new Book();
    book.setTitle("guide.title");
    book.setDisplayName(UtilChat.lang("item.guide.name"));
    book.setWelcomeMessage(UtilChat.lang("guide.welcome"));
    book.setAuthor("Lothrazar");
    book.setColor(Color.MAGENTA);
    book.setCategoryList(categories);
    book.setRegistryName(new ResourceLocation(Const.MODID, "guide"));
    book.setSpawnWithBook(true);
  }

  @Override
  public void handleModel(ItemStack bookStack) {
    GuideAPI.setModel(book);
    //    ResourceLocation location = new ResourceLocation(GUIDE_API_MOD_ID, Const.MODID + "_guidebook");
    //    IRecipe recipe = new ShapedOreRecipe(location, bookStack,
    //        " b ",
    //        "coc",
    //        " s ",
    //        'c', Blocks.COBBLESTONE_WALL,
    //        'b', Items.BOOK,
    //        'o', Blocks.GRAVEL,
    //        's', Items.STICK);
    //    //replace with guideapi prefix
    //    // we get a forge warning if using cyclic mod id, since recipe base doesnt match item base
    //    RecipeRegistry.add(recipe, location);
  }

  @Override
  public void handlePost(ItemStack bookStack) {
    // recipe used to work in handle post, had to move into handleModel in 1.12
  }
}
