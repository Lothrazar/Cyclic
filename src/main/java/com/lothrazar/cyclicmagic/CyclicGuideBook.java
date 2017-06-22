package com.lothrazar.cyclicmagic;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuidePage;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
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
import amerifrance.guideapi.page.PageText;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "amerifrance.guideapi.api.GuideAPI", modid = "guideapi", striprefs = true)
@GuideBook
public class CyclicGuideBook implements IGuideBook {
  private static final int MAX_PAGE_LENGTH = 314;
  private static Book book;
  private List<CategoryAbstract> categories = new ArrayList<CategoryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesBlocks = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesItems = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesGear = new HashMap<ResourceLocation, EntryAbstract>();
  private Map<ResourceLocation, EntryAbstract> entriesPotion = new HashMap<ResourceLocation, EntryAbstract>();
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
        entriesBlocks.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle,
            icon));
      break;
      case ITEM:
        entriesItems.put(new ResourceLocation(Const.MODID,
            pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case GEAR:
        entriesGear.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
      break;
      case POTION:
        entriesPotion.put(new ResourceLocation(Const.MODID, pageTitle), new EntryItemStack(page, pageTitle, icon));
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
    for (GuideItem item : items) {
      List<IPage> pages = new ArrayList<IPage>();
      for (GuidePage p : item.pages) {
        if (p.text != null) {
          pages.add(new PageText(p.text));
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
      addEntry(item.cat, pages, item.title, item.icon);
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
    addCategory(entriesGear,
        GuideCategory.GEAR);
    addCategory(entriesPotion, GuideCategory.POTION);
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
  }
  @Override
  public void handlePost(ItemStack bookStack) {
    RecipeRegistry.addShapelessRecipe(bookStack, Items.BOOK, Items.STICK,
        Items.COAL, Blocks.COBBLESTONE, Blocks.WOODEN_BUTTON);
  }
}
