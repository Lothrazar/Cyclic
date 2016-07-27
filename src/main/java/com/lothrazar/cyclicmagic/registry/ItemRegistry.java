package com.lothrazar.cyclicmagic.registry;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.item.BaseItem;
import com.lothrazar.cyclicmagic.item.ItemAppleEmerald;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.ItemEnderBook;
import com.lothrazar.cyclicmagic.item.ItemToolPearlReuse;
import com.lothrazar.cyclicmagic.item.ItemFoodCorruptedChorus;
import com.lothrazar.cyclicmagic.item.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.item.ItemFoodHeart;
import com.lothrazar.cyclicmagic.item.ItemFoodInventory;
import com.lothrazar.cyclicmagic.item.ItemInventoryStorage;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.item.ItemSleepingBag;
import com.lothrazar.cyclicmagic.item.ItemSproutSeeds;
import com.lothrazar.cyclicmagic.item.ItemToolHarvest;
import com.lothrazar.cyclicmagic.item.ItemToolPush;
import com.lothrazar.cyclicmagic.item.ItemToolSpawnInspect;
import com.lothrazar.cyclicmagic.item.projectile.*;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemRegistry {
  public static Map<String, Item> itemMap = new HashMap<String, Item>();
  public static final ItemFoodCorruptedChorus corrupted_chorus = new ItemFoodCorruptedChorus();

  public static ItemCyclicWand cyclic_wand_build;
  public static ItemPaperCarbon carbon_paper;
  public static final ItemFoodHeart heart_food = new ItemFoodHeart();
  public static final ItemProjectileTNT ender_tnt_1 = new ItemProjectileTNT(1);
  public static final ItemProjectileTNT ender_tnt_2 = new ItemProjectileTNT(2);
  public static final ItemProjectileTNT ender_tnt_3 = new ItemProjectileTNT(3);
  public static final ItemProjectileTNT ender_tnt_4 = new ItemProjectileTNT(4);
  public static final ItemProjectileTNT ender_tnt_5 = new ItemProjectileTNT(5);
  public static final ItemProjectileTNT ender_tnt_6 = new ItemProjectileTNT(6);
  public static final Item tool_push = new ItemToolPush();
  public static final ItemAppleEmerald apple_emerald = new ItemAppleEmerald();
  public static final ItemToolHarvest tool_harvest_weeds = new ItemToolHarvest(ItemToolHarvest.HarvestType.WEEDS);
  public static final ItemToolHarvest tool_harvest_crops = new ItemToolHarvest(ItemToolHarvest.HarvestType.CROPS);
  public static final ItemToolHarvest tool_harvest_leaves = new ItemToolHarvest(ItemToolHarvest.HarvestType.LEAVES);
  public static final ItemChestSackEmpty chest_sack_empty = new ItemChestSackEmpty();
  public static final ItemChestSack chest_sack = new ItemChestSack();
  public static final ItemToolSpawnInspect tool_spawn_inspect = new ItemToolSpawnInspect();
  public static final ItemSleepingBag sleeping_mat = new ItemSleepingBag();
  public static final ItemToolPearlReuse ender_pearl_reuse = new ItemToolPearlReuse();
  public static ItemEnderBook book_ender;
  public static final ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze()  ;
  public static final ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
  public static final ItemProjectileFishing ender_fishing = new ItemProjectileFishing();
  public static final ItemProjectileWool ender_wool = new ItemProjectileWool()   ;
  public static final ItemProjectileTorch ender_torch = new ItemProjectileTorch()  ;
  public static final ItemProjectileWater ender_water = new ItemProjectileWater()  ;
  public static final ItemProjectileSnow ender_snow = new ItemProjectileSnow()   ;
  public static final ItemProjectileLightning ender_lightning = new ItemProjectileLightning()   ;
  public static ItemInventoryStorage storage_bag;

  public static ItemSproutSeeds sprout_seed;
  
  public static void construct() {

    ItemRegistry.addItem(ItemRegistry.ender_blaze, "ender_blaze");
    ItemRegistry.addItem(ItemRegistry.ender_dungeon, "ender_dungeon");
    ItemRegistry.addItem(ItemRegistry.ender_fishing, "ender_fishing");
    ItemRegistry.addItem(ItemRegistry.ender_wool  , "ender_wool");
    ItemRegistry.addItem(ItemRegistry.ender_torch  , "ender_torch");
    ItemRegistry.addItem(ItemRegistry.ender_water , "ender_water");
    ItemRegistry.addItem(ItemRegistry.ender_snow  , "ender_snow");
    ItemRegistry.addItem(ItemRegistry.ender_lightning, "ender_lightning");
    ItemRegistry.addItem(ItemRegistry.sleeping_mat, "sleeping_mat");
    ItemRegistry.addItem(new ItemFoodCrafting(), "crafting_food");
    ItemRegistry.addItem(new ItemFoodInventory(), "inventory_food");
    ItemRegistry.addItem(ItemRegistry.tool_spawn_inspect, "tool_spawn_inspect");
    ItemRegistry.addItem(ItemRegistry.ender_pearl_reuse, "ender_pearl_reuse");
    ItemRegistry.addItem(ItemRegistry.tool_harvest_weeds, "tool_harvest_weeds");
    ItemRegistry.addItem(ItemRegistry.tool_harvest_crops, "tool_harvest_crops");
    ItemRegistry.addItem(ItemRegistry.tool_harvest_leaves, "tool_harvest_leaves");
    ItemRegistry.addItem(ItemRegistry.tool_push, "tool_push");
    ItemRegistry.chest_sack.setHidden();
    ItemRegistry.addItem(ItemRegistry.chest_sack, "chest_sack");
    ItemRegistry.addItem(ItemRegistry.chest_sack_empty, "chest_sack_empty");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_1, "ender_tnt_1");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_2, "ender_tnt_2");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_3, "ender_tnt_3");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_4, "ender_tnt_4");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_5, "ender_tnt_5");
    ItemRegistry.addItem(ItemRegistry.ender_tnt_6, "ender_tnt_6");
    ItemRegistry.addItem(ItemRegistry.corrupted_chorus, "corrupted_chorus");
    ItemRegistry.addItem(ItemRegistry.heart_food, "heart_food");
    ItemRegistry.addItem(ItemRegistry.apple_emerald, "apple_emerald");
  }
  public static Item addItem(Item i, String key) {
    i.setUnlocalizedName(key);
    itemMap.put(key, i);
    return i;
  }
  public static void syncConfig(Configuration config) {
    //		Property prop;
    Item item;
    for (String key : itemMap.keySet()) {
      item = itemMap.get(key);
      if (item instanceof IHasConfig) {
        ((IHasConfig) item).syncConfig(config);
      }
    }
  }
  private static void registerRecipes() {
    Item item;
    for (String key : itemMap.keySet()) {
      item = itemMap.get(key);
      if (item instanceof IHasRecipe) {
        ((IHasRecipe) item).addRecipe();
      }
    }
  }
  public static void register() { 
    //		addItem(new ItemFlintTool(),"flint_tool");
    //maybe one day it will be all base items
    Item item;
    for (String key : itemMap.keySet()) {
      item = itemMap.get(key);
      if (item instanceof BaseItem) {
        ((BaseItem) item).register(key);
      }
      else {
        registerItem(item, key);
      }
    }
    registerRecipes();
  }
 
  public static void registerItem(Item item, String name) {
    registerItem(item, name, false);// default is not hidden
  }
  public static void registerItem(Item item, String name, boolean isHidden) {
    GameRegistry.register(item, new ResourceLocation(Const.MODID, name));
    if (isHidden == false) {
      item.setCreativeTab(ModMain.TAB);
    }
  }
}
