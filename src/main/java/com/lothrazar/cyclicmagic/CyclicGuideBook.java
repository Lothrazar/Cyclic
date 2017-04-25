package com.lothrazar.cyclicmagic;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import amerifrance.guideapi.api.GuideAPI;
import amerifrance.guideapi.api.GuideBook;
import amerifrance.guideapi.api.IGuideBook;
import amerifrance.guideapi.api.IPage;
import amerifrance.guideapi.api.impl.Book;
import amerifrance.guideapi.api.impl.abstraction.CategoryAbstract;
import amerifrance.guideapi.api.impl.abstraction.EntryAbstract;
import amerifrance.guideapi.category.CategoryItemStack;
import amerifrance.guideapi.entry.EntryItemStack;
import amerifrance.guideapi.page.PageIRecipe;
import amerifrance.guideapi.page.PageText;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Optional.Interface(iface = "amerifrance.guideapi.api.GuideAPI", modid = "guideapi", striprefs = true)
@GuideBook
public class CyclicGuideBook implements IGuideBook {
  public static Book book;
  private static Section section = new Section();
  public static List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
  public static Map<ResourceLocation, EntryAbstract> entriesBlocks = new HashMap<ResourceLocation, EntryAbstract>();
  public static Map<ResourceLocation, EntryAbstract> entriesItems = new HashMap<ResourceLocation, EntryAbstract>();
  public enum Cateories {
    BLOCK, ITEM, WORLD;
    public String text() {
      return name().toLowerCase();
    }
    public void addPage(List<IPage> page, String pageTitle, ItemStack icon) {
      switch (this) {
        case BLOCK:
          entriesBlocks.put(new ResourceLocation(Const.MODID, section.nextCategory()), new EntryItemStack(page, pageTitle, icon));
        break;
        case ITEM:
          entriesItems.put(new ResourceLocation(Const.MODID, section.nextCategory()), new EntryItemStack(page, pageTitle, icon));
        break;
        case WORLD:
        break;
        default:
        break;
      }
    }
  }
  public static void addPageItem(Item item, IRecipe recipe) {
    String pageTitle = item.getUnlocalizedName().replace("name", "guide");
    String above = item.getUnlocalizedName().replace("name", "guide.above");
    CyclicGuideBook.addPage(Cateories.ITEM, pageTitle, new ItemStack(item), above, recipe);
  }
  public static void addPageBlock(Block block, IRecipe recipe) {
    String pageTitle = block.getUnlocalizedName().replace("name", "guide");
    String above = block.getUnlocalizedName().replace("name", "guide.above");
    CyclicGuideBook.addPage(Cateories.BLOCK, pageTitle, new ItemStack(block), above, recipe);
  }
  private static void addPage(Cateories cat, String pageTitle, ItemStack icon, String above, IRecipe recipe) {
    List<IPage> pages = new ArrayList<IPage>();
    pages.add(new PageText(above));//just text on the screen
    if (recipe != null)
      pages.add(new PageIRecipe(recipe));
    cat.addPage(pages, pageTitle, icon);
  }
  @Override
  public Book buildBook() {
    //    //    String firstCategory = section.nextCategory();
    //    List<IPage> pages = new ArrayList<IPage>();
    //    pages.add(new PageText(section.nextPage()));//just text on the screen
    //    pages.add(new PageIRecipe(new ShapedOreRecipe(Blocks.DIRT, " s", "sp", " d", 's', "stickWood", 'p', "paper", 'd', "dye")));
    //    //this puts a link on the main page each time
    //    entriesBlocks.put(new ResourceLocation(Const.MODID, section.toString()), new EntryItemStack(pages, section.toString(), new ItemStack(Items.DIAMOND)));
    //    pages = new ArrayList<IPage>();
    //    pages.add(new PageText(section.nextPage()));//just text on the screen
    //    pages.add(new PageIRecipe(new ShapedOreRecipe(Blocks.GRASS, " s", "sp", " d", 's', "stickWood", 'p', "paper", 'd', "dye")));
    //this puts a link on the main page each time
    //    entriesBlocks.put(new ResourceLocation(Const.MODID, section.toString()), new EntryItemStack(pages, section.nextPage(), new ItemStack(Items.EMERALD)));
    categories.add(new CategoryItemStack(entriesBlocks, "guide.category." + Cateories.BLOCK.text(), new ItemStack(Blocks.ENDER_CHEST)));
    categories.add(new CategoryItemStack(entriesItems, "guide.category." + Cateories.ITEM.text(), new ItemStack(Items.STICK)));
    createBook();
    return book;
  }
  private void createBook() {
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
  }
  @Override
  public void handlePost(ItemStack bookStack) {
    GameRegistry.addShapelessRecipe(bookStack, Items.BOOK, Items.STICK, Items.COAL, Items.APPLE);
  }
  static class Section {
    private int category = 0;
    private int page = 0;
    private int item = 0;
    public Section() {}
    public String toString() {
      return category + "." + page + "." + item;
    }
    public String nextItem() {
      item++;
      return lang("guide.item." + toString());
    }
    public String nextPage() {
      page++;
      item = 0;
      return lang("guide.page." + toString());
    }
    public String nextCategory() {
      category++;
      page = 0;
      item = 0;
      return lang("guide.category." + toString());
    }
    private String lang(String s) {
      return UtilChat.lang(s);//for ease of typing. and in case we need extra metadata here
    }
  }
}
