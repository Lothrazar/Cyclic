package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemStorageBag;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.item.tool.ItemCyclicWand;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
  public static Map<String, Item> itemMap = new HashMap<String, Item>();
  public static ItemCyclicWand cyclic_wand_build;
  public static ItemStorageBag storage_bag;
  public static ItemChestSackEmpty chest_sack_empty;
  public static ItemChestSack chest_sack;
  public static ItemSproutSeeds sprout_seed;
  public static Item addItem(Item i, String key) {
    i.setUnlocalizedName(key);
    itemMap.put(key, i);
    return i;
  }
  private static void registerItem(Item item, String name) {
    registerItem(item, name, false);// default is not hidden
  }
  public static void registerItem(Item item, String name, boolean isHidden) {
    GameRegistry.register(item, new ResourceLocation(Const.MODID, name));
    if (isHidden == false) {
      item.setCreativeTab(ModMain.TAB);
    }
  }
  public static void register() {
    Item item;
    for (String key : ItemRegistry.itemMap.keySet()) {
      item = ItemRegistry.itemMap.get(key);
      if (item instanceof BaseItem) {
        ((BaseItem) item).register(key);
      }
      else {
        ItemRegistry.registerItem(item, key);
      }
      if (item instanceof IHasRecipe) {
        ((IHasRecipe) item).addRecipe();
      }
    }
  }
}
