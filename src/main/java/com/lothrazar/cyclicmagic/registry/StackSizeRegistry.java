package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class StackSizeRegistry {
 // private static boolean enabled = true;
  private static Map<Item, Integer> stackMap = new HashMap<Item, Integer>();
  private static Map<Item, Integer> enabledMap = new HashMap<Item, Integer>();

  public static void construct() {
    int boat = 16;
    int MAX = 64;
    stackMap.put(Items.BOAT, boat);
    stackMap.put(Items.ACACIA_BOAT, boat);
    stackMap.put(Items.BIRCH_BOAT, boat);
    stackMap.put(Items.SPRUCE_BOAT, boat);
    stackMap.put(Items.DARK_OAK_BOAT, boat);
    stackMap.put(Items.JUNGLE_BOAT, boat);
    stackMap.put(Items.MINECART, boat);
    stackMap.put(Items.CHEST_MINECART, boat);
    stackMap.put(Items.FURNACE_MINECART, boat);
    stackMap.put(Items.HOPPER_MINECART, boat);
    stackMap.put(Items.TNT_MINECART, boat);
    stackMap.put(Items.SNOWBALL, MAX);
    stackMap.put(Items.BANNER, MAX);
    stackMap.put(Items.SNOWBALL, MAX);
    stackMap.put(Items.ARMOR_STAND, MAX);
    stackMap.put(Items.SIGN, MAX);
    stackMap.put(Items.BED, MAX);
    stackMap.put(Items.BUCKET, MAX);
//    for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {
//      all += entry.getKey().getUnlocalizedName() + ",";
//    }
//    all.replace(all.substring(all.length() - 1), "");
  }
  public static void register() {


    for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {
      boolean enabled = (enabledMap.get(entry.getKey()) == 1);
      if(enabled){
        entry.getKey().setMaxStackSize(entry.getValue());
      }
    }
  }
  public static void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.itemsTack;
    // config.setCategoryComment(category, "Tons of new recipes for existing
    // blocks and items. Bonemeal to undye wool; repeater and dispenser tweaks;
    // making player skulls out of the four mob heads...");
   
    for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {
      String name = entry.getKey().getUnlocalizedName();
      int enabled = config.getBoolean(name, category, true, "Increase stack size to "+entry.getValue()) ? 1 : 0;
      enabledMap.put(entry.getKey(), enabled);
    }
  }
}
