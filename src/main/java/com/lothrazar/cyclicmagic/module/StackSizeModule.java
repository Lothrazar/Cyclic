package com.lothrazar.cyclicmagic.module;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

public class StackSizeModule extends BaseModule implements IHasConfig {
  private Map<Item, Integer> stackMap = new HashMap<Item, Integer>();
  private Map<Item, Integer> enabledMap = new HashMap<Item, Integer>();
  public StackSizeModule() {
    int boat = 16;
    int MAX = 64;
    stackMap.put(Items.SADDLE, boat);
    stackMap.put(Items.DIAMOND_HORSE_ARMOR, boat);
    stackMap.put(Items.GOLDEN_HORSE_ARMOR, boat);
    stackMap.put(Items.IRON_HORSE_ARMOR, boat);
    //DO NOT DO RECORDS until we confirm that vanilla juke boxes dont DUPE them
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
    stackMap.put(Items.ENDER_PEARL, MAX);
    stackMap.put(Items.EGG, MAX);
  }
  public void onInit() {
    for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {
      boolean enabled = (enabledMap.get(entry.getKey()) == 1);
      if (enabled) {
        entry.getKey().setMaxStackSize(entry.getValue());
      }
    }
  }
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.itemsTack;
    for (Map.Entry<Item, Integer> entry : stackMap.entrySet()) {
      String name = entry.getKey().getUnlocalizedName();
      int enabled = config.getBoolean(name, category, true, "Increase stack size to " + entry.getValue()) ? 1 : 0;
      enabledMap.put(entry.getKey(), enabled);
    }
  }
}
