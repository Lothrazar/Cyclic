package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class UtilUncraft {
  public static boolean dictionaryFreedom;
  public static List<String> blacklistInput = new ArrayList<String>();
  public static List<String> blacklistOutput = new ArrayList<String>();
  public static List<String> blacklistMod = new ArrayList<String>();
  private ArrayList<ItemStack> drops;
  private ItemStack toUncraft;
  private int outsize;
  public UtilUncraft(ItemStack stuff) {
    this.drops = new ArrayList<ItemStack>();
    this.toUncraft = stuff;
    this.outsize = 0;
  }
  public static enum BlacklistType {
    INPUT, OUTPUT, MODNAME;//, CONTAINS;
  }
  public static void resetBlacklists() {
    blacklistInput = new ArrayList<String>();
    blacklistOutput = new ArrayList<String>();
    blacklistMod = new ArrayList<String>();
  }
  public static void setBlacklist(String[] list, BlacklistType type) {
    if (list == null || list.length == 0) { return; }
    for (String iname : list) {
      switch (type) {
      case MODNAME:
        blacklistMod.add(iname);
        break;
      case INPUT:
        blacklistInput.add(iname);
        break;
      case OUTPUT:
        blacklistOutput.add(iname);
        break;
      }
    }
  }
  private boolean isItemInBlacklist(ItemStack drop, BlacklistType type) {
    if (drop == null || drop.getItem() == null) { return true; }
    return isItemInBlacklist(drop.getItem(), type);
  }
  private String getStringForItem(Item item) {
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
  }
  private boolean isItemInBlacklist(Item item, BlacklistType type) {
    String itemName;
    switch (type) {
    case INPUT:
      itemName = getStringForItem(item);
      for (String s : blacklistInput) {//dont use .contains on the list. must use .equals on string
        if (s != null && s.equals(itemName)) { return true; }
      }
    case OUTPUT:
      itemName = getStringForItem(item);
      for (String s : blacklistOutput) {//dont use .contains on the list. must use .equals on string
        if (s != null && s.equals(itemName)) { return true; }
      }
    case MODNAME:
      String modId = item.getRegistryName().getResourceDomain();// the minecraft part of minecraft:wool (without colon)
      for (String s : blacklistMod) {//dont use .contains on the list. must use .equals on string
        if (s != null && s.equals(modId)) { return true; }
      }
      break;
    default:
      break;
    }
    return false;
  }
  /**
   * It works but we dont want to use it right now
   * 
   * @param istack
   * @return
   */
  @SuppressWarnings("unused")
  private boolean isRemovedSinceContainerItem(ItemStack istack) {
    boolean hasContainerItem = istack.getItem().getContainerItem(istack) != null;
    //EXAMPLE: milk_bucket has containerItem == milk
    return hasContainerItem;
  }
  public ArrayList<ItemStack> getDrops() {
    return drops;
  }
  public int getOutsize() {
    return outsize;
  }
  /**
   * try to add an entry that is either a stack, or a list of possibilities from
   * ore dictionary
   * 
   * @param maybeOres
   */
  private void tryAddOreDictionaryDrop(Object maybeOres) {
    // thanks  http://stackoverflow.com/questions/20462819/java-util-collectionsunmodifiablerandomaccesslist-to-collections-singletonlist
    if (maybeOres instanceof List<?> && (List<ItemStack>) maybeOres != null) {
      // so if there is silver or tin added by multiple ores for example. also affects wooden planks
      List<ItemStack> ores = (List<ItemStack>) maybeOres;
      if (ores.size() == 1) {
        tryAddTrop(ores.get(0));
      }
      else if ((ores.size() > 1) && dictionaryFreedom) {
        tryAddTrop(ores.get(0));
      }
      // else size is > 1 , so its something like wooden planks but not for now
    }
  }
  /**
   * add drop after checking blacklists
   * 
   * @param stackInput
   */
  private void tryAddTrop(ItemStack stackInput) {
    // this fn is null safe, it gets nulls all the time
    if (stackInput == null || stackInput.getItem() == null) { return; }
    //    if(isRemovedSinceContainerItem(stackInput)){
    //      ModMain.logger.info("Removed because it has a container item"+stackInput.getUnlocalizedName());
    //      return;
    //    }
    if (isItemInBlacklist(stackInput, BlacklistType.OUTPUT)) { return; }
    ItemStack stack = stackInput.copy();
    stack.stackSize = 1;
    if (stack.getItemDamage() == 32767) {
      if (dictionaryFreedom) {
        stack.setItemDamage(0);// do not make invalid quartz
      }
      else {
        return;
      }
    }
    drops.add(stack);
  }
  private boolean doesRecipeMatch(ShapedOreRecipe r) {
    return r != null && r.getRecipeOutput() != null && doesRecipeInputMatch(r.getRecipeOutput());
  }
  private boolean doesRecipeMatch(ShapelessOreRecipe r) {
    return r != null && r.getRecipeOutput() != null && doesRecipeInputMatch(r.getRecipeOutput());
  }
  private boolean doesRecipeMatch(ShapedRecipes r) {
    return r != null && r.getRecipeOutput() != null && doesRecipeInputMatch(r.getRecipeOutput());
  }
  private boolean doesRecipeMatch(ShapelessRecipes r) {
    return r != null && r.getRecipeOutput() != null && doesRecipeInputMatch(r.getRecipeOutput());
  }
  private boolean doesRecipeInputMatch(ItemStack recipeOutput) {
    boolean itemEqual = recipeOutput.isItemEqual(toUncraft);
    if (itemEqual == false) { return false;//items dont match
    }
    boolean enchantingMatches = recipeOutput.isItemEnchanted() == toUncraft.isItemEnchanted();
    if (!enchantingMatches) {
      ModCyclic.logger.info("enchanting mismatch");
    }
    return enchantingMatches;// either they are both ench, or both not ench
  }
  public boolean doUncraftStart() {
    if (toUncraft == null || toUncraft.getItem() == null) { return false; }
    if (isItemInBlacklist(toUncraft, BlacklistType.INPUT)) { return false; }
    if (isItemInBlacklist(toUncraft, BlacklistType.MODNAME)) { return false; }
    outsize = 0;
    // outsize is 3 means the recipe makes three items total. so MINUS three
    // from the toUncraft for EACH LOOP
    List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
    for (IRecipe next : recipeList) {
      outsize = next.getRecipeOutput().stackSize;
      if (toUncraft.stackSize < outsize) {//we dont have enough to satisfy
        continue;
      }
      // check ore dictionary for some
      if (next instanceof ShapedOreRecipe) {
        ShapedOreRecipe r = (ShapedOreRecipe) next;
        if (doesRecipeMatch(r)) {
          doUncraftShapedOreRecipe(r);
          break;
        }
      }
      else if (next instanceof ShapelessOreRecipe) {
        ShapelessOreRecipe r = (ShapelessOreRecipe) next;
        if (doesRecipeMatch(r)) {
          doUncraftShapelessOreRecipe(r);
          break;
        }
      }
      else if (next instanceof ShapedRecipes) {
        ShapedRecipes r = (ShapedRecipes) next;
        if (doesRecipeMatch(r)) {
          doUncraftShapedRecipes(r);
          break;
        }
      }
      else if (next instanceof ShapelessRecipes) {
        ShapelessRecipes r = (ShapelessRecipes) next;
        if (doesRecipeMatch(r)) {
          doUncraftShapelessRecipes(r);
          break;
        }
      }
    }
    return (this.drops.size() > 0);
  }
  private void doUncraftShapelessRecipes(ShapelessRecipes r) {
    for (int i = 0; i < r.recipeItems.size(); i++) {
      tryAddTrop((ItemStack) r.recipeItems.get(i));
    }
  }
  private void doUncraftShapedRecipes(ShapedRecipes r) {
    for (int i = 0; i < r.recipeItems.length; i++) {
      tryAddTrop(r.recipeItems[i]);
    }
  }
  private void doUncraftShapelessOreRecipe(ShapelessOreRecipe r) {
    ArrayList<Object> input = r.getInput();
    Object maybeOres;
    for (int i = 0; i < input.size(); i++) {
      maybeOres = input.get(i);
      if (maybeOres instanceof ItemStack) {
        tryAddTrop((ItemStack) maybeOres);
      }
      else {
        tryAddOreDictionaryDrop(maybeOres);
      }
    }
  }
  private void doUncraftShapedOreRecipe(ShapedOreRecipe r) {
    Object[] input = r.getInput();
    Object maybeOres;
    for (int i = 0; i < input.length; i++) {
      maybeOres = input[i];
      if (maybeOres == null) {
        continue;
      }
      if (maybeOres instanceof ItemStack) {
        tryAddTrop((ItemStack) maybeOres);
      }
      else {
        tryAddOreDictionaryDrop(maybeOres);
      }
    }
  }
  public boolean canUncraft() {
    // ?? make this actually different and more efficient?
    // ex we dont need drops and such
    return this.doUncraftStart();
  }
}
