package com.lothrazar.cyclicmagic.util;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class UtilOreDictionary {
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String oreId) {
    return doesMatchOreDict(stackIn, oreId, false);
  }
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String oreId, boolean strict) {
    if (OreDictionary.doesOreNameExist(oreId)) {
      for (ItemStack stackCurrent : OreDictionary.getOres(oreId)) {
        if (OreDictionary.itemMatches(stackCurrent, stackIn, strict)) {
          return true;
        }
      }
    }
    return false;
  }
  public static boolean doesMatchOreDict(final ItemStack stackIn, final String[] oreIds) {
    return doesMatchOreDict(stackIn, oreIds, false);
  }
  public static boolean doesMatchOreDict(ItemStack stackIn, String[] oreIds, boolean strict) {
    for (String oreId : oreIds) {
      if (doesMatchOreDict(stackIn, oreId, strict)) {
        return true;
      }
    }
    return false;
  }
}
