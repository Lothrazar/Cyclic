package com.lothrazar.cyclicmagic.module;
import java.util.Set;
import com.google.common.collect.Sets;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.component.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.component.enderbook.ItemEnderBook;
import com.lothrazar.cyclicmagic.component.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.component.storagesack.ItemStorageBag;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper.WandType;
import com.lothrazar.cyclicmagic.item.ItemCaveFinder;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemEnderBag;
import com.lothrazar.cyclicmagic.item.ItemEnderPearlReuse;
import com.lothrazar.cyclicmagic.item.ItemEnderWing;
import com.lothrazar.cyclicmagic.item.ItemFangs;
import com.lothrazar.cyclicmagic.item.ItemFireExtinguish;
import com.lothrazar.cyclicmagic.item.ItemMattock;
import com.lothrazar.cyclicmagic.item.ItemPaperCarbon;
import com.lothrazar.cyclicmagic.item.ItemPasswordRemote;
import com.lothrazar.cyclicmagic.item.ItemPistonWand;
import com.lothrazar.cyclicmagic.item.ItemPlayerLauncher;
import com.lothrazar.cyclicmagic.item.ItemProspector;
import com.lothrazar.cyclicmagic.item.ItemRandomizer;
import com.lothrazar.cyclicmagic.item.ItemRotateBlock;
import com.lothrazar.cyclicmagic.item.ItemScythe;
import com.lothrazar.cyclicmagic.item.ItemSleepingMat;
import com.lothrazar.cyclicmagic.item.ItemSoulstone;
import com.lothrazar.cyclicmagic.item.ItemSpawnInspect;
import com.lothrazar.cyclicmagic.item.ItemStirrups;
import com.lothrazar.cyclicmagic.item.ItemTorchThrower;
import com.lothrazar.cyclicmagic.item.ItemWarpSurface;
import com.lothrazar.cyclicmagic.item.ItemWaterSpreader;
import com.lothrazar.cyclicmagic.item.ItemWaterToIce;
import com.lothrazar.cyclicmagic.item.bauble.ItemGloveClimb;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;

public class ItemToolsModule extends BaseModule implements IHasConfig {
  private boolean enableSleepingMat;
  private boolean enableToolPush;
  private boolean enableHarvestLeaves;
  private boolean enableToolHarvest;
  private boolean enableHarvestWeeds;
  private boolean enablePearlReuse;
  private boolean enableSpawnInspect;
  private boolean enableCyclicWand;
  private boolean enableProspector;
  private boolean enableCavefinder;
  private boolean enableWarpHomeTool;
  private boolean enableWarpSpawnTool;
  private boolean enableSwappers;
  private boolean enableRando;
  private boolean enableMattock;
  private boolean enablePearlReuseMounted;
  private boolean enableCarbonPaper;
  private boolean storageBagEnabled;
  private boolean enableEnderBook;
  private boolean enableChestSack;
  private boolean enableStirrups;
  private boolean enableTorchLauncher;
  private boolean enderSack;
  private boolean enablewaterSpread;
  private boolean enableFreezer;
  private boolean enableFireKiller;
  private boolean enableBlockRot;
  private boolean enableCGlove;
  private boolean enableElevate;
  private boolean enableLever;
  private boolean enableTrader;
  private boolean enableSoulstone;
  private boolean enablePlayerLauncher;
  private boolean evokerFang;
  public static ItemStorageBag storage_bag;//ref by ContainerStorage

  
  @Override
  public void onPreInit() {
    if (evokerFang) {
      ItemFangs evoker_fangs = new ItemFangs();
      ItemRegistry.register(evoker_fangs, "evoker_fang", GuideCategory.ITEM);
      LootTableRegistry.registerLoot(evoker_fangs);
    }
    if (enablePlayerLauncher) {
      ItemPlayerLauncher tool_launcher = new ItemPlayerLauncher();
      ItemRegistry.register(tool_launcher, "tool_launcher", GuideCategory.TRANSPORT);
      ModCyclic.instance.events.register(tool_launcher);
    }
    if (enableSoulstone) {
      ItemSoulstone soulstone = new ItemSoulstone();
      ItemRegistry.register(soulstone, "soulstone");
      ModCyclic.instance.events.register(soulstone);
    }
    if (enableTrader) {
      ItemMerchantAlmanac tool_trade = new ItemMerchantAlmanac();
      ItemRegistry.register(tool_trade, "tool_trade");
    }
    if (enableLever) {
      ItemPasswordRemote password_remote = new ItemPasswordRemote();
      ItemRegistry.register(password_remote, "password_remote");
    }
    if (enableElevate) {
      ItemWarpSurface tool_elevate = new ItemWarpSurface();
      ItemRegistry.register(tool_elevate, "tool_elevate", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(tool_elevate);
    }
    if (enableCGlove) {
      ItemGloveClimb glove_climb = new ItemGloveClimb();
      ItemRegistry.register(glove_climb, "glove_climb", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(glove_climb);
    }
    if (enableBlockRot) {
      ItemRotateBlock tool_rotate = new ItemRotateBlock();
      ItemRegistry.register(tool_rotate, "tool_rotate");
    }
    if (enablewaterSpread) {
      ItemWaterSpreader water_spreader = new ItemWaterSpreader();
      ItemRegistry.register(water_spreader, "water_spreader");
    }
    if (enableFreezer) {
      ItemWaterToIce water_freezer = new ItemWaterToIce();
      ItemRegistry.register(water_freezer, "water_freezer");
    }
    if (enableFireKiller) {
      ItemFireExtinguish fire_killer = new ItemFireExtinguish();
      ItemRegistry.register(fire_killer, "fire_killer");
    }
    if (enderSack) {
      ItemEnderBag sack_ender = new ItemEnderBag();
      ItemRegistry.register(sack_ender, "sack_ender");
      LootTableRegistry.registerLoot(sack_ender);
      ItemRegistry.registerWithJeiDescription(sack_ender);
    }
    if (enableTorchLauncher) {
      ItemTorchThrower tool_torch_launcher = new ItemTorchThrower();
      ItemRegistry.register(tool_torch_launcher, "tool_torch_launcher");
    }
    if (enableStirrups) {
      ItemStirrups tool_mount = new ItemStirrups();
      ItemRegistry.register(tool_mount, "tool_mount");
      ItemRegistry.registerWithJeiDescription(tool_mount);
    }
    if (enableChestSack) {
      ItemChestSackEmpty chest_sack_empty = new ItemChestSackEmpty();
      ItemChestSack chest_sack = new ItemChestSack();
      chest_sack.setEmptySack(chest_sack_empty);
      chest_sack_empty.setFullSack(chest_sack);
      ItemRegistry.registerWithJeiDescription(chest_sack);
      ItemRegistry.register(chest_sack, "chest_sack", null);
      ItemRegistry.register(chest_sack_empty, "chest_sack_empty");
      ItemRegistry.registerWithJeiDescription(chest_sack_empty);
    }
    if (enableEnderBook) {
      ItemEnderBook book_ender = new ItemEnderBook();
      ItemRegistry.register(book_ender, "book_ender", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(book_ender);
      LootTableRegistry.registerLoot(book_ender);
      ItemRegistry.registerWithJeiDescription(book_ender);
    }
    if (storageBagEnabled) {
      storage_bag = new ItemStorageBag();
      ItemRegistry.register(storage_bag, "storage_bag");
      ModCyclic.instance.events.register(storage_bag);
      LootTableRegistry.registerLoot(storage_bag, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(storage_bag);
    }
    if (enableCarbonPaper) {
      ItemPaperCarbon carbon_paper = new ItemPaperCarbon();
      ItemRegistry.register(carbon_paper, "carbon_paper");
      ItemRegistry.registerWithJeiDescription(carbon_paper);
    }
    if (enableProspector) {
      ItemProspector tool_prospector = new ItemProspector();
      ItemRegistry.register(tool_prospector, "tool_prospector");
      LootTableRegistry.registerLoot(tool_prospector);
      ItemRegistry.registerWithJeiDescription(tool_prospector);
    }
    if (enableCavefinder) {
      ItemCaveFinder tool_spelunker = new ItemCaveFinder();
      ItemRegistry.register(tool_spelunker, "tool_spelunker");
      ItemRegistry.registerWithJeiDescription(tool_spelunker);
    }
    if (enableSpawnInspect) {
      ItemSpawnInspect tool_spawn_inspect = new ItemSpawnInspect();
      ItemRegistry.register(tool_spawn_inspect, "tool_spawn_inspect");
      ItemRegistry.registerWithJeiDescription(tool_spawn_inspect);
    }
    if (enablePearlReuse) {
      ItemEnderPearlReuse ender_pearl_reuse = new ItemEnderPearlReuse(ItemEnderPearlReuse.OrbType.NORMAL);
      ItemRegistry.register(ender_pearl_reuse, "ender_pearl_reuse");
      LootTableRegistry.registerLoot(ender_pearl_reuse);
      ItemRegistry.registerWithJeiDescription(ender_pearl_reuse);
    }
    if (enablePearlReuseMounted) {
      ItemEnderPearlReuse ender_pearl_mounted = new ItemEnderPearlReuse(ItemEnderPearlReuse.OrbType.MOUNTED);
      ItemRegistry.register(ender_pearl_mounted, "ender_pearl_mounted");
      LootTableRegistry.registerLoot(ender_pearl_mounted);
      //      AchievementRegistry.registerItemAchievement(ender_pearl_mounted);
      ItemRegistry.registerWithJeiDescription(ender_pearl_mounted);
    }
    if (enableHarvestWeeds) {
      ItemScythe tool_harvest_weeds = new ItemScythe(ItemScythe.HarvestType.WEEDS);
      ItemRegistry.register(tool_harvest_weeds, "tool_harvest_weeds");
      LootTableRegistry.registerLoot(tool_harvest_weeds, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(tool_harvest_weeds);
    }
    if (enableToolHarvest) {
      ItemScythe tool_harvest_crops = new ItemScythe(ItemScythe.HarvestType.CROPS);
      ItemRegistry.register(tool_harvest_crops, "tool_harvest_crops");
      LootTableRegistry.registerLoot(tool_harvest_crops);
      //      AchievementRegistry.registerItemAchievement(tool_harvest_crops);
      ItemRegistry.registerWithJeiDescription(tool_harvest_crops);
    }
    if (enableHarvestLeaves) {
      ItemScythe tool_harvest_leaves = new ItemScythe(ItemScythe.HarvestType.LEAVES);
      ItemRegistry.register(tool_harvest_leaves, "tool_harvest_leaves");
      LootTableRegistry.registerLoot(tool_harvest_leaves, ChestType.BONUS);
      ItemRegistry.registerWithJeiDescription(tool_harvest_leaves);
    }
    if (enableToolPush) {
      ItemPistonWand tool_push = new ItemPistonWand();
      ItemRegistry.register(tool_push, "tool_push");
      ModCyclic.instance.events.register(tool_push);
      LootTableRegistry.registerLoot(tool_push);
      //      AchievementRegistry.registerItemAchievement(tool_push);
      ItemRegistry.registerWithJeiDescription(tool_push);
    }
    if (enableSleepingMat) {
      ItemSleepingMat sleeping_mat = new ItemSleepingMat();
      ItemRegistry.register(sleeping_mat, "sleeping_mat");
      ModCyclic.instance.events.register(sleeping_mat);
      LootTableRegistry.registerLoot(sleeping_mat, ChestType.BONUS);
    }
    if (enableCyclicWand) {
      ItemCyclicWand cyclic_wand_build = new ItemCyclicWand();
      ItemRegistry.register(cyclic_wand_build, "cyclic_wand_build");
      SpellRegistry.register(cyclic_wand_build);
      ModCyclic.instance.events.register(this);
      LootTableRegistry.registerLoot(cyclic_wand_build, ChestType.ENDCITY, 15);
      LootTableRegistry.registerLoot(cyclic_wand_build, ChestType.GENERIC, 1);
      //      AchievementRegistry.registerItemAchievement(cyclic_wand_build);
      ModCyclic.instance.setTabItemIfNull(cyclic_wand_build);
      ItemRegistry.registerWithJeiDescription(cyclic_wand_build);
    }
    if (enableWarpHomeTool) {
      ItemEnderWing tool_warp_home = new ItemEnderWing(ItemEnderWing.WarpType.BED);
      ItemRegistry.register(tool_warp_home, "tool_warp_home", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(tool_warp_home);
      //      AchievementRegistry.registerItemAchievement(tool_warp_home);
      ItemRegistry.registerWithJeiDescription(tool_warp_home);
    }
    if (enableWarpSpawnTool) {
      ItemEnderWing tool_warp_spawn = new ItemEnderWing(ItemEnderWing.WarpType.SPAWN);
      ItemRegistry.register(tool_warp_spawn, "tool_warp_spawn", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(tool_warp_spawn);
      ItemRegistry.registerWithJeiDescription(tool_warp_spawn);
    }
    if (enableSwappers) {
      ItemBuildSwapper tool_swap = new ItemBuildSwapper(WandType.NORMAL);
      ItemRegistry.register(tool_swap, "tool_swap");
      ModCyclic.instance.events.register(tool_swap);
      ItemBuildSwapper tool_swap_match = new ItemBuildSwapper(WandType.MATCH);
      ItemRegistry.register(tool_swap_match, "tool_swap_match");
      ModCyclic.instance.events.register(tool_swap_match);
      ItemRegistry.registerWithJeiDescription(tool_swap_match);
      ItemRegistry.registerWithJeiDescription(tool_swap);
    }
    if (enableRando) {
      ItemRandomizer tool_randomize = new ItemRandomizer();
      ItemRegistry.register(tool_randomize, "tool_randomize");
      ModCyclic.instance.events.register(tool_randomize);
      ItemRegistry.registerWithJeiDescription(tool_randomize);
    }
    if (enableMattock) {
      final Set<Block> blocks = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH);
      final Set<Material> materials = Sets.newHashSet(Material.ANVIL, Material.GLASS, Material.ICE, Material.IRON, Material.PACKED_ICE, Material.PISTON, Material.ROCK, Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY);
      ItemMattock mattock = new ItemMattock(2, -1, MaterialRegistry.emeraldToolMaterial, blocks, materials);
      ItemRegistry.register(mattock, "mattock");
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    evokerFang = config.getBoolean("EvokerFang", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlayerLauncher = config.getBoolean("PlayerLauncher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSoulstone = config.getBoolean("Soulstone", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableTrader = config.getBoolean("Merchant Almanac", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLever = config.getBoolean("Remote Lever", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableElevate = config.getBoolean("RodElevation", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCGlove = config.getBoolean("ClimbingGlove", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBlockRot = config.getBoolean("BlockRotator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablewaterSpread = config.getBoolean("WaterSpreader", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFreezer = config.getBoolean("WaterFroster", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFireKiller = config.getBoolean("WaterSplasher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderSack = config.getBoolean("EnderSack", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableTorchLauncher = config.getBoolean("TorchLauncher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    storageBagEnabled = config.getBoolean("StorageBag", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBook = config.getBoolean("EnderBook", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWarpHomeTool = config.getBoolean("EnderWingPrime", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWarpSpawnTool = config.getBoolean("EnderWing", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpawnInspect = config.getBoolean("SpawnDetector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuse = config.getBoolean("EnderOrb", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestWeeds = config.getBoolean("BrushScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolHarvest = config.getBoolean("HarvestScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestLeaves = config.getBoolean("TreeScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolPush = config.getBoolean("PistonScepter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSleepingMat = config.getBoolean("SleepingMat", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCyclicWand = config.getBoolean("CyclicWand", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableProspector = config.getBoolean("Prospector", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCavefinder = config.getBoolean("Cavefinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSwappers = config.getBoolean("ExchangeScepters", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableRando = config.getBoolean("BlockRandomizer", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuseMounted = config.getBoolean("EnderOrbMounted", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCarbonPaper = config.getBoolean("CarbonPaper", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableChestSack = config.getBoolean("ChestSack", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableStirrups = config.getBoolean("Stirrups", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String[] deflist = new String[] { "minecraft:mob_spawner", "minecraft:obsidian" };
    ItemBuildSwapper.swapBlacklist = config.getStringList("ExchangeSceptersBlacklist", Const.ConfigCategory.items, deflist, "Blocks that will not be broken by the exchange scepters.  It will also not break anything that is unbreakable (such as bedrock), regardless of if its in this list or not.  ");
    enableMattock = config.getBoolean("Mattock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    
  }
 
}
