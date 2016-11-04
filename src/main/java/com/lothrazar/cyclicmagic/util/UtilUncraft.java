package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class UtilUncraft {
  // static boolean blockIfCannotDoit;
  public static boolean dictionaryFreedom;
  public static List<Item> blacklistInput = new ArrayList<Item>();
  // also, when crafting cake you get the empty bucket back.
  // so dont refund full buckets or else thats free infinite iron
  public static List<Item> blacklistOutput = new ArrayList<Item>();// They
  public static enum BlacklistType {
    INPUT, OUTPUT;
  }
  public static void setBlacklist(String[] list, BlacklistType type) {
    if (list == null || list.length == 0) { return; }
    Item item = null;
    for (String iname : list) {
      item = Item.getByNameOrId(iname);
      if (item == null) {
        ModMain.logger.warn("Uncrafting Grinder Blacklist: Item or block not found " + iname);
      }
      else {
        // ModMain.logger.info("Uncrafting Grinder Blacklist: VALID" + iname);
        switch (type) {
        case INPUT:
          blacklistInput.add(item);
          break;
        case OUTPUT:
          blacklistOutput.add(item);
          break;
        }
      }
    }
  }
  private boolean isItemInBlacklist(Item item, BlacklistType type) {
    if (item == null) { return true; }
    boolean blacklist = false;
    switch (type) {
    case INPUT:
      blacklist = blacklistInput.contains(item);
    case OUTPUT:
      blacklist = blacklistOutput.contains(item);
    }
    ModMain.logger.info("Uncrafting: is it in blacklist?" + type + ":" + blacklist + "__" + item.getUnlocalizedName());
    return blacklist;
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
  // were
  // null
  private ArrayList<ItemStack> drops;
  private ItemStack toUncraft;
  private int outsize;
  public UtilUncraft(ItemStack stuff) {
    this.drops = new ArrayList<ItemStack>();
    this.toUncraft = stuff;
    this.outsize = 0;
  }
  public ArrayList<ItemStack> getDrops() {
    return drops;
  }
  public int getOutsize() {
    return outsize;
  }
  private void addDrop(ItemStack stackInput) {
    // this fn is null safe, it gets nulls all the time
    if (stackInput == null || stackInput.getItem() == null) { return; }
    //    if(isRemovedSinceContainerItem(stackInput)){
    //      ModMain.logger.info("Removed because it has a container item"+stackInput.getUnlocalizedName());
    //      return;
    //    }
    if (isItemInBlacklist(stackInput.getItem(), BlacklistType.OUTPUT)) { return; }
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
  @SuppressWarnings("unchecked")
  public boolean doUncraft() {
    if (toUncraft == null || toUncraft.getItem() == null) { return false; }
    if (isItemInBlacklist(toUncraft.getItem(), BlacklistType.INPUT)) { return false; }
    //if (blacklistInput.contains(toUncraft.getItem().getUnlocalizedName())) { return false; }
    int i;
    Object maybeOres;
    outsize = 0;
    // outsize is 3 means the recipe makes three items total. so MINUS three
    // from the toUncraft for EACH LOOP
    for (Object next : CraftingManager.getInstance().getRecipeList()) {
      // check ore dictionary for some
      if (next instanceof ShapedOreRecipe) {
        ShapedOreRecipe r = (ShapedOreRecipe) next;
        if (r != null && r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(toUncraft)) {
          outsize = r.getRecipeOutput().stackSize;
          if (toUncraft.stackSize >= outsize) {
            for (i = 0; i < r.getInput().length; i++) {
              maybeOres = r.getInput()[i];
              if (maybeOres == null) {
                continue;
              }
              // thanks  http://stackoverflow.com/questions/20462819/java-util-collectionsunmodifiablerandomaccesslist-to-collections-singletonlist
              if (maybeOres instanceof List<?> && (List<ItemStack>) maybeOres != null) // <ItemStack>
              {
                List<ItemStack> ores = (List<ItemStack>) maybeOres;
                if (ores.size() == 1) {
                  // sticks,iron,and so on
                  addDrop(ores.get(0));
                }
                else if ((ores.size() > 1) && dictionaryFreedom) {
                  addDrop(ores.get(0));
                }
                // else size is > 1 , so its something like wooden planks but not for now
              }
              else if (maybeOres instanceof ItemStack) // <ItemStack>
              {
                addDrop((ItemStack) maybeOres);
              }
            }
          }
          break;
        }
      }
      else if (next instanceof ShapelessOreRecipe) {
        ShapelessOreRecipe r = (ShapelessOreRecipe) next;
        if (r != null && r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(toUncraft)) {
          outsize = r.getRecipeOutput().stackSize;
          if (toUncraft.stackSize >= outsize) {
            for (i = 0; i < r.getInput().size(); i++) {
              maybeOres = r.getInput().get(i);
              if (maybeOres instanceof List<?> && (List<ItemStack>) maybeOres != null) // <ItemStack>
              {
                List<ItemStack> ores = (List<ItemStack>) maybeOres;
                if (ores.size() == 1) {
                  addDrop(ores.get(0));
                  // sticks,iron,and so on
                }
                else if ((ores.size() > 1) && dictionaryFreedom) {
                  addDrop(ores.get(0));
                }
              }
              if (maybeOres instanceof ItemStack) // <ItemStack>
              {
                addDrop((ItemStack) maybeOres);
              }
            }
          }
          break;
        }
      }
      else if (next instanceof ShapedRecipes) {
        ShapedRecipes r = (ShapedRecipes) next;
        if (r != null && r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(toUncraft)) {
          outsize = r.getRecipeOutput().stackSize;
          if (toUncraft.stackSize >= outsize) {
            for (i = 0; i < r.recipeItems.length; i++) {
              addDrop(r.recipeItems[i]);
            }
          }
          break;
        }
      }
      else if (next instanceof ShapelessRecipes) {
        ShapelessRecipes r = (ShapelessRecipes) next;
        if (r != null && r.getRecipeOutput() != null && r.getRecipeOutput().isItemEqual(toUncraft)) {
          outsize = r.getRecipeOutput().stackSize;
          if (toUncraft.stackSize >= outsize) {
            for (i = 0; i < r.recipeItems.size(); i++) {
              addDrop((ItemStack) r.recipeItems.get(i));
            }
          }
          break;
        }
      }
    }
    return (this.drops.size() > 0);
  }
  public boolean canUncraft() {
    // ?? make this actually different and more efficient?
    // ex we dont need drops and such
    return this.doUncraft();
  }
}
