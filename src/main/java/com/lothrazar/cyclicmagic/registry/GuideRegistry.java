package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class GuideRegistry {
  private static List<GuideItem> items = new ArrayList<GuideItem>();
  private static final String SUFFIX = ".guide";
  public enum GuideCategory {
    BLOCK, ITEM, WORLD, GEAR, POTION, ENCHANT;
    public String text() {
      return name().toLowerCase();
    }
  }
  public static void register(Enchantment ench, @Nonnull List<String> args) {
    args.add(ench.getRarity().name().toLowerCase().replace("_", " "));
    GuideRegistry.register(GuideCategory.ENCHANT, new ItemStack(Items.ENCHANTED_BOOK), ench.getName(), ench.getName() + SUFFIX, null, args);
  }
  public static void register(GuideCategory cat, Block block, IRecipe recipe, @Nullable List<String> args) {
    String pageTitle = block.getUnlocalizedName() + ".name";
    String text = block.getUnlocalizedName() + SUFFIX;
    register(cat, new ItemStack(block), pageTitle, text, recipe, args);
  }
  public static void register(GuideCategory cat, Item item, IRecipe recipe, @Nullable List<String> args) {
    String pageTitle = item.getUnlocalizedName() + ".name";
    String above = item.getUnlocalizedName() + SUFFIX;
    register(cat, new ItemStack(item), pageTitle, above, recipe, args);
  }
  public static void register(GuideCategory cat, ItemStack icon, String title, String text) {
    register(cat, icon, title, text, null, null);
  }
  //  //the main one. others are helper/wrappers
  //  public static void register(GuideCategory cat, ItemStack icon, String title, String text, @Nullable IRecipe recipe, @Nullable List<String>  args) {
  //    
  //  }
  public static void register(GuideCategory cat, ItemStack icon, String title, String text, @Nullable IRecipe recipes, @Nullable List<String> args) {
    //layer of seperation between guidebook api. 1 for optional include and 2 in case i ever need to split it out and 3 for easy registering
    if (args != null && args.size() > 0) {
      text = UtilChat.lang(text);
      for (int i = 0; i < args.size(); i++) {
        //in the lang file we have something like "Rarity is $1, similar to $2 "
        //so the text is translatable but we swap out values like this
        System.out.println(text + " -> " + i + " convert to " + args.get(i));
        text = text.replace("$" + i, args.get(i));
      }
    }
    items.add(new GuideItem(cat, icon, title, text, recipes));
    //then if guide book exists it pulls this in
    //addPage(CategoryType cat, String pageTitle, ItemStack icon, String above, @Nullable IRecipe recipe) {
  }
  public static List<GuideItem> getItems() {
    return items;
  }
  public static class GuidePage {
    public String text = null;
    public IRecipe recipe = null;
    public GuidePage(String t) {
      text = t;
    }
    public GuidePage(IRecipe t) {
      recipe = t;
    }
  }
  public static class GuideItem {
    public GuideCategory cat;
    public ItemStack icon;
    public String title;
    public List<GuidePage> pages = new ArrayList<GuidePage>();
    public GuideItem(@Nonnull GuideCategory cat, @Nonnull ItemStack icon, @Nonnull String title, @Nonnull String text, @Nullable IRecipe recipe) {
      this.cat = cat;
      this.icon = icon;
      this.title = UtilChat.lang(title);
      //default is a text page followed by recipe. more added later
      this.pages.add(new GuidePage(UtilChat.lang(text)));
      if (recipe != null)
        this.pages.add(new GuidePage(recipe));
    }
    public void addRecipePage(IRecipe t) {
      this.pages.add(new GuidePage(t));
    }
    public void addTextPage(String t) {
      this.pages.add(new GuidePage(t));
    }
  }
}
