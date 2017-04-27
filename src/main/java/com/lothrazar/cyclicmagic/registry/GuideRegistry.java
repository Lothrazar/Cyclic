package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.CyclicGuideBook;
//import com.lothrazar.cyclicmagic.CyclicGuideBook.CategoryType;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
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
  public static void register(Enchantment ench) {
    GuideRegistry.register(GuideCategory.ENCHANT, new ItemStack(Items.ENCHANTED_BOOK), ench.getName(), ench.getName() + SUFFIX, null, null);
  }
  public static void register(GuideCategory cat, Block block, IRecipe recipe, @Nullable int[] args) {
    String pageTitle = block.getUnlocalizedName() + ".name";
    String text = block.getUnlocalizedName() + SUFFIX;
    register(cat, new ItemStack(block), pageTitle, text, recipe, args);
  }
  public static void register(GuideCategory cat, Item item, IRecipe recipe, @Nullable int[] args) {
    String pageTitle = item.getUnlocalizedName() + ".name";
    String above = item.getUnlocalizedName() + SUFFIX;
    register(cat, new ItemStack(item), pageTitle, above, recipe, args);
  }
  public static void register(GuideCategory cat, ItemStack icon, String title, String text) {
    register(cat, icon, title, text, null, null);
  }
  //the main one. others are helper/wrappers
  public static void register(GuideCategory cat, ItemStack icon, String title, String text, @Nullable IRecipe recipe, @Nullable int[] args) {
    //layer of seperation between guidebook api. 1 for optional include and 2 in case i ever need to split it out and 3 for easy registering
    if (args != null && args.length > 0) {
      for (int i = 0; i < args.length; i++) {
        //in the lang file we have something like "Rarity is $1, similar to $2 "
        //so the text is translatable but we swap out values like this
        text = text.replace("$" + i, args[1] + "");
      }
    }
    items.add(new GuideItem(cat, icon, title, text, recipe));
    //then if guide book exists it pulls this in
    //addPage(CategoryType cat, String pageTitle, ItemStack icon, String above, @Nullable IRecipe recipe) {
  }
  public static List<GuideItem> getItems() {
    return items;
  }
  public static class GuideItem {
    public GuideCategory cat;
    public ItemStack icon;
    public String title;
    public String text;
    public IRecipe recipe;
    public GuideItem(@Nonnull GuideCategory cat, @Nonnull ItemStack icon, @Nonnull String title, @Nonnull String text, @Nullable IRecipe recipe) {
      this.cat = cat;
      this.icon = icon;
      this.title = title;
      this.text = text;
      this.recipe = recipe;
    }
  }
}
