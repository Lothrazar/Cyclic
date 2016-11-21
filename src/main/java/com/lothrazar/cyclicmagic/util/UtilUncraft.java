package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  public static enum BlacklistType {
    INPUT, OUTPUT, MODNAME;//, CONTAINS;
  }
  public static enum UncraftResultType {
    BLACKLIST, NORECIPE, NOTENOUGHITEMS, SUCCESS, EMPTY;
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
  private static boolean isItemInBlacklist(ItemStack drop, BlacklistType type) {
    if (drop == null || drop.getItem() == null) { return true; }
    return isItemInBlacklist(drop.getItem(), type);
  }
  private static String getStringForItem(Item item) {
    return item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
  }
  private static boolean isItemInBlacklist(Item item, BlacklistType type) {
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
  public static class Uncrafter {
    private ArrayList<ItemStack> drops;
    private ItemStack toUncraft;
    private int outsize;
    public Uncrafter() {
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
     * try to add an entry that is either a stack, or a list of possibilities
     * from ore dictionary
     * 
     * @param maybeOres
     */
    @SuppressWarnings("unchecked")
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
    private boolean doesRecipeMatch(IRecipe r) {
      return r != null && r.getRecipeOutput() != null && doesRecipeInputMatch(r.getRecipeOutput());
    }
    private boolean doesRecipeInputMatch(ItemStack recipeOutput) {
      boolean itemEqual = recipeOutput.isItemEqual(toUncraft);
      if (itemEqual == false) { return false;//items dont match
      }
      boolean enchantingMatches = recipeOutput.isItemEnchanted() == toUncraft.isItemEnchanted();
      return enchantingMatches;// either they are both ench, or both not ench
    }
    public UncraftResultType process(ItemStack stuff) {
      this.toUncraft = stuff;
      this.drops = new ArrayList<ItemStack>();
      this.outsize = 0;
      if (toUncraft == null || toUncraft.getItem() == null) { return UncraftResultType.EMPTY; }
      if (isItemInBlacklist(toUncraft, BlacklistType.INPUT)) { return UncraftResultType.BLACKLIST; }
      if (isItemInBlacklist(toUncraft, BlacklistType.MODNAME)) { return UncraftResultType.BLACKLIST; }
      outsize = 0;
      // outsize is 3 means the recipe makes three items total. so MINUS three from the toUncraft for EACH LOOP
      UncraftResultType result = UncraftResultType.NORECIPE;//assumption
      List<IRecipe> recipeList = CraftingManager.getInstance().getRecipeList();
      for (IRecipe next : recipeList) {
        if (next == null || next.getRecipeOutput() == null) {
          continue;//be careful
        }
        if (doesRecipeMatch(next)) {
          if (toUncraft.stackSize < next.getRecipeOutput().stackSize) {
            result = UncraftResultType.NOTENOUGHITEMS;//we found a matching recipe but we dont have enough to satisfy
            continue;//keep looking but save the result type
          }
          outsize = next.getRecipeOutput().stackSize;
          List<? extends Object> input = getRecipeInput(next);
          for (Object maybeOres : input) {
            if (maybeOres instanceof ItemStack) {
              tryAddTrop((ItemStack) maybeOres);
            }
            else {
              tryAddOreDictionaryDrop(maybeOres);
            }
          }
          break;//since we are finished doing a recipe that matches, break the MAIN list
        }
      }
      if (this.drops.size() > 0) {
        return UncraftResultType.SUCCESS;
      }
      else {
        return result;//either norecipe or notenough items
      }
    }
    //TODO: get display output/simple output
    //  private List<ItemStack> getRecipeInputFlattenOreDict(IRecipe next) {
    //    List<ItemStack> ret;
    //    List<? extends Object> input = getRecipeInput(next);
    //    for (Object maybeOres : input) {
    //      if (maybeOres instanceof ItemStack) {
    //        tryAddTrop((ItemStack) maybeOres);
    //      }
    //      else {
    //        tryAddOreDictionaryDrop(maybeOres);
    //      }
    //    }
    //    return null;
    //  }
    /**
     * could be a list of ItemStacks, or a list of Objects which are ore
     * dictionary entries
     * 
     * @param next
     * @return
     */
    private List<? extends Object> getRecipeInput(IRecipe next) {
      if (next instanceof ShapedOreRecipe) {
        ShapedOreRecipe r = (ShapedOreRecipe) next;
        return new ArrayList<Object>(Arrays.asList(r.getInput()));
      }
      else if (next instanceof ShapelessOreRecipe) {
        ShapelessOreRecipe r = (ShapelessOreRecipe) next;
        return r.getInput();
      }
      else if (next instanceof ShapedRecipes) {
        ShapedRecipes r = (ShapedRecipes) next;
        return new ArrayList<ItemStack>(Arrays.asList(r.recipeItems));
      }
      else if (next instanceof ShapelessRecipes) {
        ShapelessRecipes r = (ShapelessRecipes) next;
        return r.recipeItems;
      }
      //else it could be anything from a custom mod ex: solderer
      return null;
    }
  }
}
