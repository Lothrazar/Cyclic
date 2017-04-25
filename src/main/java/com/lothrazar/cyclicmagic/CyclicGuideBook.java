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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Optional.Interface(iface = "amerifrance.guideapi.api.GuideAPI", modid = "guideapi", striprefs = true)
@GuideBook
public class CyclicGuideBook implements IGuideBook {
  public static Book book;
  private Section section = new Section();
  List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();

  @Override
  public Book buildBook() {
    
    Map<ResourceLocation, EntryAbstract> entries = new HashMap<ResourceLocation, EntryAbstract>();
    String firstCategory = section.nextCategory();
    List<IPage> pages = new ArrayList<IPage>();
    pages.add(new PageText(  section.nextPage() ));//just text on the screen
    pages.add(new PageIRecipe(new ShapedOreRecipe(Blocks.DIRT, " s", "sp", " d", 's', "stickWood", 'p', "paper", 'd', "dye")));
    
    
    
    //this puts a link on the main page each time
    entries.put(new ResourceLocation(Const.MODID, section.toString()), new EntryItemStack(pages, section.toString(), new ItemStack(Items.DIAMOND)));
    
    
    
    pages = new ArrayList<IPage>();
    pages.add(new PageText(  section.nextPage() ));//just text on the screen
    pages.add(new PageIRecipe(new ShapedOreRecipe(Blocks.GRASS, " s", "sp", " d", 's', "stickWood", 'p', "paper", 'd', "dye")));
    //this puts a link on the main page each time
    entries.put(new ResourceLocation(Const.MODID, section.toString()), new EntryItemStack(pages, section.nextPage(), new ItemStack(Items.EMERALD)));
    
    
    
    categories.add(new CategoryItemStack(entries,  firstCategory, new ItemStack(Blocks.ENDER_CHEST)));
    
    
    createBook();
    return book;
  }
  private void createBook() {
    book = new Book();
    book.setTitle("guide.title");
    book.setDisplayName(UtilChat.lang("item.guide.name"));
    book.setWelcomeMessage(UtilChat.lang("guide.welcome"));
    book.setAuthor("The_Fireplace");
    book.setColor(Color.RED);
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
      return lang("guide.item."+toString());
    }
    public String nextPage() {
      page++;
      item = 0;
      return lang("guide.page."+toString());
    }
    public String nextCategory() {
      category++;
      page = 0;
      item = 0;
      return lang("guide.category."+toString());
    }
    private String lang(String s) {
      return UtilChat.lang(s);//for ease of typing. and in case we need extra metadata here
    }
  }
}
