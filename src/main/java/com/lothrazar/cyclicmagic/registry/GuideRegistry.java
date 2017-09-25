package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.brewing.BrewingRecipe;

public class GuideRegistry {
  private static List<GuideItem> items = new ArrayList<GuideItem>();
  private static final String SUFFIX = ".guide";
  public enum GuideCategory {
    BLOCK, ITEM, WORLD, GEAR, POTION, ENCHANT, BLOCKMACHINE, BLOCKPLATE, ITEMBAUBLES, ITEMTHROW, TRANSPORT;
    public String text() {
      return "guide.category." + name().toLowerCase();
    }
    public ItemStack icon() {
      switch (this) {
        case BLOCK:
          return new ItemStack(Blocks.ENDER_CHEST);
        case BLOCKMACHINE:
          return new ItemStack(Blocks.FURNACE);
        case ENCHANT:
          return new ItemStack(Items.ENCHANTED_BOOK);
        case GEAR:
          return new ItemStack(Items.DIAMOND_SWORD);
        case ITEM:
          return new ItemStack(Items.STICK);
        case POTION:
          return new ItemStack(Items.POTIONITEM);
        case WORLD:
          return new ItemStack(Blocks.GOLD_ORE);
        case BLOCKPLATE:
          return new ItemStack(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE);
        case ITEMBAUBLES:
          return new ItemStack(Items.TOTEM_OF_UNDYING);
        case ITEMTHROW:
          return new ItemStack(Items.ELYTRA);
        case TRANSPORT:
          return new ItemStack(Items.FURNACE_MINECART);
        default:
          return new ItemStack(Blocks.DIRT);//wont happen unless new cat undefined
      }
    }
  }
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
  public static class GuidePage {
    public String text = null;
    public IRecipe recipe = null;
    public BrewingRecipe brewRecipe = null;
    public GuidePage(String t) {
      text = t;
    }
    public GuidePage(IRecipe t) {
      recipe = t;
    }
    public GuidePage(BrewingRecipe t) {
      brewRecipe = t;
    }
  }
  public static class GuideItem {
    public GuideCategory cat;
    public Item icon;
    public String title;
    public List<GuidePage> pages = new ArrayList<GuidePage>();
    public GuideItem(@Nonnull GuideCategory cat, @Nonnull Item icon, @Nonnull String title, @Nonnull String text, @Nullable IRecipe recipe) {
      this.cat = cat;
      this.icon = icon;
      this.title = UtilChat.lang(title);
      if (text != null) {
        this.pages.add(new GuidePage(UtilChat.lang(text)));
      }
      if (recipe != null) {
        this.pages.add(new GuidePage(recipe));
      }
    }
    public void addRecipePage(IRecipe t) {
      if (t == null) {
        return;
      }
      this.pages.add(new GuidePage(t));
    }
    public void addRecipePage(BrewingRecipe t) {
      if (t == null) {
        return;
      }
      this.pages.add(new GuidePage(t));
    }
    public void addTextPage(String t) {
      this.pages.add(new GuidePage(t));
    }
  }
}
