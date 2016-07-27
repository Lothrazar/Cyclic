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
  public static final ItemEnderBook book_ender = new ItemEnderBook();
  public static final ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze()  ;
  public static final ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
  public static final ItemProjectileFishing ender_fishing = new ItemProjectileFishing();
  public static final ItemProjectileWool ender_wool = new ItemProjectileWool()   ;
  public static final ItemProjectileTorch ender_torch = new ItemProjectileTorch()  ;
  public static final ItemProjectileWater ender_water = new ItemProjectileWater()  ;
  public static final ItemProjectileSnow ender_snow = new ItemProjectileSnow()   ;
  public static final ItemProjectileLightning ender_lightning = new ItemProjectileLightning()   ;
  public static ItemInventoryStorage storage_bag = new ItemInventoryStorage()   ;

  public static ItemSproutSeeds sprout_seed;
  
  public static void construct() {

    addItem(ender_blaze, "ender_blaze");
    addItem(ender_dungeon, "ender_dungeon");
    addItem(ender_fishing, "ender_fishing");
    addItem(ender_wool  , "ender_wool");
    addItem(ender_torch  , "ender_torch");
    addItem(ender_water , "ender_water");
    addItem(ender_snow  , "ender_snow");
    addItem(ender_lightning, "ender_lightning");
    addItem(book_ender, "book_ender");
    addItem(sleeping_mat, "sleeping_mat");
    addItem(new ItemFoodCrafting(), "crafting_food");
    addItem(new ItemFoodInventory(), "inventory_food");
    addItem(tool_spawn_inspect, "tool_spawn_inspect");
    addItem(ender_pearl_reuse, "ender_pearl_reuse");
    addItem(tool_harvest_weeds, "tool_harvest_weeds");
    addItem(tool_harvest_crops, "tool_harvest_crops");
    addItem(tool_harvest_leaves, "tool_harvest_leaves");
    addItem(tool_push, "tool_push");
    chest_sack.setHidden();
    addItem(chest_sack, "chest_sack");
    addItem(chest_sack_empty, "chest_sack_empty");
    addItem(ender_tnt_1, "ender_tnt_1");
    addItem(ender_tnt_2, "ender_tnt_2");
    addItem(ender_tnt_3, "ender_tnt_3");
    addItem(ender_tnt_4, "ender_tnt_4");
    addItem(ender_tnt_5, "ender_tnt_5");
    addItem(ender_tnt_6, "ender_tnt_6");
    addItem(corrupted_chorus, "corrupted_chorus");
    addItem(heart_food, "heart_food");
    addItem(apple_emerald, "apple_emerald");
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
