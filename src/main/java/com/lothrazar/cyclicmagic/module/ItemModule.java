/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.module;

import java.util.ArrayList;
import java.util.Set;
import com.google.common.collect.Sets;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.dispenser.BehaviorProjectileThrowable;
import com.lothrazar.cyclicmagic.entity.EntityEnderEyeUnbreakable;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper;
import com.lothrazar.cyclicmagic.item.ItemBuildSwapper.WandType;
import com.lothrazar.cyclicmagic.item.ItemCaveFinder;
import com.lothrazar.cyclicmagic.item.ItemChestSack;
import com.lothrazar.cyclicmagic.item.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.item.ItemEnderBag;
import com.lothrazar.cyclicmagic.item.ItemEnderEyeReuse;
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
import com.lothrazar.cyclicmagic.item.ItemStirrupsReverse;
import com.lothrazar.cyclicmagic.item.ItemWarpSurface;
import com.lothrazar.cyclicmagic.item.ItemWaterRemoval;
import com.lothrazar.cyclicmagic.item.ItemWaterSpreader;
import com.lothrazar.cyclicmagic.item.ItemWaterToIce;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.cyclicwand.ItemCyclicWand;
import com.lothrazar.cyclicmagic.item.enderbook.ItemEnderBook;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemAutoTorch;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmAir;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmAntidote;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmBoat;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmFire;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmSlowfall;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmSpeed;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmVoid;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemCharmWater;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemGloveClimb;
import com.lothrazar.cyclicmagic.item.magic.chaos.ItemWandHypno;
import com.lothrazar.cyclicmagic.item.magic.dynamite.EntityDynamite;
import com.lothrazar.cyclicmagic.item.magic.dynamite.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.item.magic.dynamite.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.item.magic.dynamite.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.item.magic.dynamite.ItemProjectileTNT.ExplosionType;
import com.lothrazar.cyclicmagic.item.magic.energy.EntityHomingProjectile;
import com.lothrazar.cyclicmagic.item.magic.energy.ItemMagicMissile;
import com.lothrazar.cyclicmagic.item.magic.fire.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.item.magic.fire.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.magic.ice.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.item.magic.ice.ItemProjectileSnow;
import com.lothrazar.cyclicmagic.item.magic.lightning.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.item.magic.lightning.ItemProjectileLightning;
import com.lothrazar.cyclicmagic.item.magic.locate.EntityDungeonEye;
import com.lothrazar.cyclicmagic.item.magic.locate.ItemProjectileDungeon;
import com.lothrazar.cyclicmagic.item.magic.shears.EntityShearingBolt;
import com.lothrazar.cyclicmagic.item.magic.shears.ItemShearsRanged;
import com.lothrazar.cyclicmagic.item.magic.torch.EntityTorchBolt;
import com.lothrazar.cyclicmagic.item.magic.torch.ItemProjectileTorch;
import com.lothrazar.cyclicmagic.item.magic.torch.ItemTorchThrower;
import com.lothrazar.cyclicmagic.item.magicnet.EntityMagicNetEmpty;
import com.lothrazar.cyclicmagic.item.magicnet.EntityMagicNetFull;
import com.lothrazar.cyclicmagic.item.magicnet.ItemProjectileMagicNet;
import com.lothrazar.cyclicmagic.item.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseTame;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.mobs.ItemVillagerMagic;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.item.storagesack.ItemStorageBag;
import com.lothrazar.cyclicmagic.playerupgrade.ItemAppleStep;
import com.lothrazar.cyclicmagic.playerupgrade.ItemChorusCorrupted;
import com.lothrazar.cyclicmagic.playerupgrade.ItemChorusGlowing;
import com.lothrazar.cyclicmagic.playerupgrade.ItemFoodCrafting;
import com.lothrazar.cyclicmagic.playerupgrade.ItemFoodInventory;
import com.lothrazar.cyclicmagic.playerupgrade.ItemHeartContainer;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.MaterialRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemModule extends BaseModule implements IHasConfig {

  private boolean enableEmeraldApple;
  private boolean enableHeartContainer;
  private boolean enableInventoryCrafting;
  private boolean enableInventoryUpgrade;
  private boolean enableCorruptedChorus;
  private boolean enableHorseFoodUpgrades;
  private boolean enableGlowingChorus;
  private boolean enableLapisApple;
  private boolean foodStep;
  private boolean mountInverse;
  private boolean enableFire;
  private boolean enableSea;
  private boolean enableVoid;
  private boolean enableWater;
  private boolean antidoteCharm;
  private boolean slowfallCharm;
  private boolean autoTorch;
  private boolean enableSpeed;
  private boolean enableAir;
  private boolean enableEnderBlaze;
  private boolean enableEnderDungeonFinder;
  private boolean enderSnow;
  private boolean enderWool;
  private boolean enderTorch;
  private boolean enderWater;
  private boolean enderLightning;
  private boolean enderBombsEnabled;
  ArrayList<BaseItemProjectile> projectiles = new ArrayList<BaseItemProjectile>();
  private boolean dynamiteSafe;
  private boolean dynamiteMining;
  private boolean magicNet;
  private boolean enableChaos;
  private boolean enableMissile;
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
  private boolean enderEyeReuse;
  public static ItemStorageBag storage_bag;//ref by ContainerStorage

  @Override
  public void onPreInit() {
    if (enableCGlove) {
      ItemGloveClimb glove_climb = new ItemGloveClimb();
      ItemRegistry.register(glove_climb, "glove_climb", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(glove_climb);
    }
    if (evokerFang) {
      ItemFangs evoker_fangs = new ItemFangs();
      ItemRegistry.register(evoker_fangs, "evoker_fang", GuideCategory.ITEM);
      LootTableRegistry.registerLoot(evoker_fangs);
      ModCyclic.instance.events.register(evoker_fangs);
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
      ModCyclic.instance.events.register(tool_trade);
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
    if (enderEyeReuse) {
      ItemEnderEyeReuse ender_eye_orb = new ItemEnderEyeReuse();
      ItemRegistry.register(ender_eye_orb, "ender_eye_orb");
      EntityProjectileRegistry.registerModEntity(EntityEnderEyeUnbreakable.class, "ender_eye_orb", 1029);
    }
    if (enderSack) {
      ItemEnderBag sack_ender = new ItemEnderBag();
      ItemRegistry.register(sack_ender, "sack_ender");
      LootTableRegistry.registerLoot(sack_ender);
    }
    if (enableTorchLauncher) {
      ItemTorchThrower tool_torch_launcher = new ItemTorchThrower();
      ItemRegistry.register(tool_torch_launcher, "tool_torch_launcher");
    }
    if (enableStirrups) {
      ItemStirrups tool_mount = new ItemStirrups();
      ItemRegistry.register(tool_mount, "tool_mount");
    }
    if (enableChestSack) {
      ItemChestSackEmpty chest_sack_empty = new ItemChestSackEmpty();
      ItemChestSack chest_sack = new ItemChestSack();
      chest_sack.setEmptySack(chest_sack_empty);
      chest_sack_empty.setFullSack(chest_sack);
      // ItemRegistry.registerWithJeiDescription(chest_sack);
      ItemRegistry.register(chest_sack, "chest_sack", null);
      ItemRegistry.register(chest_sack_empty, "chest_sack_empty");
      //   ItemRegistry.registerWithJeiDescription(chest_sack_empty);
    }
    if (enableEnderBook) {
      ItemEnderBook book_ender = new ItemEnderBook();
      ItemRegistry.register(book_ender, "book_ender", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(book_ender);
      LootTableRegistry.registerLoot(book_ender);
      // ItemRegistry.registerWithJeiDescription(book_ender);
    }
    if (storageBagEnabled) {
      storage_bag = new ItemStorageBag();
      ItemRegistry.register(storage_bag, "storage_bag");
      ModCyclic.instance.events.register(storage_bag);
      LootTableRegistry.registerLoot(storage_bag, ChestType.BONUS);
      // ItemRegistry.registerWithJeiDescription(storage_bag);
    }
    if (enableCarbonPaper) {
      ItemPaperCarbon carbon_paper = new ItemPaperCarbon();
      ItemRegistry.register(carbon_paper, "carbon_paper");
      //ItemRegistry.registerWithJeiDescription(carbon_paper);
    }
    if (enableProspector) {
      ItemProspector tool_prospector = new ItemProspector();
      ItemRegistry.register(tool_prospector, "tool_prospector");
      LootTableRegistry.registerLoot(tool_prospector);
      //ItemRegistry.registerWithJeiDescription(tool_prospector);
    }
    if (enableCavefinder) {
      ItemCaveFinder tool_spelunker = new ItemCaveFinder();
      ItemRegistry.register(tool_spelunker, "tool_spelunker");
      // ItemRegistry.registerWithJeiDescription(tool_spelunker);
    }
    if (enableSpawnInspect) {
      ItemSpawnInspect tool_spawn_inspect = new ItemSpawnInspect();
      ItemRegistry.register(tool_spawn_inspect, "tool_spawn_inspect");
      //   ItemRegistry.registerWithJeiDescription(tool_spawn_inspect);
    }
    if (enablePearlReuse) {
      ItemEnderPearlReuse ender_pearl_reuse = new ItemEnderPearlReuse(ItemEnderPearlReuse.OrbType.NORMAL);
      ItemRegistry.register(ender_pearl_reuse, "ender_pearl_reuse");
      LootTableRegistry.registerLoot(ender_pearl_reuse);
      //   ItemRegistry.registerWithJeiDescription(ender_pearl_reuse);
    }
    if (enablePearlReuseMounted) {
      ItemEnderPearlReuse ender_pearl_mounted = new ItemEnderPearlReuse(ItemEnderPearlReuse.OrbType.MOUNTED);
      ItemRegistry.register(ender_pearl_mounted, "ender_pearl_mounted");
      LootTableRegistry.registerLoot(ender_pearl_mounted);
      //      AchievementRegistry.registerItemAchievement(ender_pearl_mounted);
      // ItemRegistry.registerWithJeiDescription(ender_pearl_mounted);
    }
    if (enableHarvestWeeds) {
      ItemScythe tool_harvest_weeds = new ItemScythe(ItemScythe.ScytheType.WEEDS);
      ItemRegistry.register(tool_harvest_weeds, "tool_harvest_weeds");
      LootTableRegistry.registerLoot(tool_harvest_weeds, ChestType.BONUS);
      //  ItemRegistry.registerWithJeiDescription(tool_harvest_weeds);
    }
    if (enableToolHarvest) {
      ItemScythe tool_harvest_crops = new ItemScythe(ItemScythe.ScytheType.CROPS);
      ItemRegistry.register(tool_harvest_crops, "tool_harvest_crops");
      LootTableRegistry.registerLoot(tool_harvest_crops);
      //      AchievementRegistry.registerItemAchievement(tool_harvest_crops);
      // ItemRegistry.registerWithJeiDescription(tool_harvest_crops);
    }
    if (enableHarvestLeaves) {
      ItemScythe tool_harvest_leaves = new ItemScythe(ItemScythe.ScytheType.LEAVES);
      ItemRegistry.register(tool_harvest_leaves, "tool_harvest_leaves");
      LootTableRegistry.registerLoot(tool_harvest_leaves, ChestType.BONUS);
      // ItemRegistry.registerWithJeiDescription(tool_harvest_leaves);
    }
    if (enableToolPush) {
      ItemPistonWand tool_push = new ItemPistonWand();
      ItemRegistry.register(tool_push, "tool_push");
      ModCyclic.instance.events.register(tool_push);
      LootTableRegistry.registerLoot(tool_push);
      //      AchievementRegistry.registerItemAchievement(tool_push);
      //  ItemRegistry.registerWithJeiDescription(tool_push);
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
      ModCyclic.TAB.setTabItemIfNull(cyclic_wand_build);
      //    ItemRegistry.registerWithJeiDescription(cyclic_wand_build);
    }
    if (enableWarpHomeTool) {
      ItemEnderWing tool_warp_home = new ItemEnderWing(ItemEnderWing.WarpType.BED);
      ItemRegistry.register(tool_warp_home, "tool_warp_home", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(tool_warp_home);
      //      AchievementRegistry.registerItemAchievement(tool_warp_home);
      // ItemRegistry.registerWithJeiDescription(tool_warp_home);
    }
    if (enableWarpSpawnTool) {
      ItemEnderWing tool_warp_spawn = new ItemEnderWing(ItemEnderWing.WarpType.SPAWN);
      ItemRegistry.register(tool_warp_spawn, "tool_warp_spawn", GuideCategory.TRANSPORT);
      LootTableRegistry.registerLoot(tool_warp_spawn);
      //   ItemRegistry.registerWithJeiDescription(tool_warp_spawn);
    }
    if (enableSwappers) {
      ItemBuildSwapper tool_swap = new ItemBuildSwapper(WandType.NORMAL);
      ItemRegistry.register(tool_swap, "tool_swap");
      ModCyclic.instance.events.register(tool_swap);
      ItemBuildSwapper tool_swap_match = new ItemBuildSwapper(WandType.MATCH);
      ItemRegistry.register(tool_swap_match, "tool_swap_match");
      ModCyclic.instance.events.register(tool_swap_match);
    }
    if (enableRando) {
      ItemRandomizer tool_randomize = new ItemRandomizer();
      ItemRegistry.register(tool_randomize, "tool_randomize");
      ModCyclic.instance.events.register(tool_randomize);
      //   ItemRegistry.registerWithJeiDescription(tool_randomize);
    }
    if (enableMattock) {
      final Set<Block> blocks = Sets.newHashSet(Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE, Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH);
      final Set<Material> materials = Sets.newHashSet(Material.ANVIL, Material.GLASS, Material.ICE, Material.IRON, Material.PACKED_ICE, Material.PISTON, Material.ROCK, Material.GRASS, Material.GROUND, Material.SAND, Material.SNOW, Material.CRAFTED_SNOW, Material.CLAY);
      ItemMattock mattock = new ItemMattock(2, -1, MaterialRegistry.emeraldToolMaterial, blocks, materials);
      ItemRegistry.register(mattock, "mattock");
    }
    if (enableChaos) {
      ItemWandHypno wand_hypno = new ItemWandHypno();
      ItemRegistry.register(wand_hypno, "wand_hypno", GuideCategory.ITEMTHROW);
    }
    if (enableMissile) {
      ItemMagicMissile magic_missile = new ItemMagicMissile();
      ItemRegistry.register(magic_missile, "wand_missile", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityHomingProjectile.class, "magic_missile", 1020);
    }
    if (enableEnderBlaze) {
      Item fire_dark_anim = new Item();
      ItemRegistry.register(fire_dark_anim, "fire_dark_anim");
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ender_blaze.setRepairItem(new ItemStack(fire_dark_anim));
      ItemRegistry.register(ender_blaze, "ender_blaze", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      ModCyclic.instance.events.register(ender_blaze);
    }
    if (enableEnderDungeonFinder) {
      ItemProjectileDungeon ender_dungeon = new ItemProjectileDungeon();
      ItemRegistry.register(ender_dungeon, "ender_dungeon", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityDungeonEye.class, "dungeonbolt", 1006);
      LootTableRegistry.registerLoot(ender_dungeon);
    }
    if (enderWool) {
      ItemShearsRanged ender_wool = new ItemShearsRanged();
      ItemRegistry.register(ender_wool, "ender_wool", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityShearingBolt.class, "woolbolt", 1003);
    }
    if (enderTorch) {
      ItemProjectileTorch ender_torch = new ItemProjectileTorch();
      ItemRegistry.register(ender_torch, "ender_torch", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityTorchBolt.class, "torchbolt", 1002);
    }
    if (enderWater) {
      ItemWaterRemoval ender_water = new ItemWaterRemoval();
      ItemRegistry.register(ender_water, "ender_water", GuideCategory.ITEMTHROW);
      ModCyclic.instance.events.register(ender_water);
    }
    if (enderSnow) {
      ItemProjectileSnow ender_snow = new ItemProjectileSnow();
      ItemRegistry.register(ender_snow, "ender_snow", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntitySnowballBolt.class, "frostbolt", 1001);
      ModCyclic.instance.events.register(ender_snow);
    }
    if (enderLightning) {
      ItemProjectileLightning ender_lightning = new ItemProjectileLightning();
      ItemRegistry.register(ender_lightning, "ender_lightning", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityLightningballBolt.class, "lightningbolt", 999);
      LootTableRegistry.registerLoot(ender_lightning);
      ModCyclic.instance.events.register(ender_lightning);
    }
    if (dynamiteSafe) {
      ItemProjectileTNT dynamite_safe = new ItemProjectileTNT(6, ExplosionType.BLOCKSAFE);
      ItemRegistry.register(dynamite_safe, "dynamite_safe", GuideCategory.ITEMTHROW);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, dynamite_safe);
      EntityProjectileRegistry.registerModEntity(EntityDynamiteBlockSafe.class, "tntblocksafebolt", 1009);
      EntityDynamiteBlockSafe.renderSnowball = dynamite_safe;
      projectiles.add(dynamite_safe);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(dynamite_safe, 6),
          "gunpowder",
          new ItemStack(Items.SUGAR),
          "gunpowder",
          "paper",
          new ItemStack(Items.CLAY_BALL),
          new ItemStack(Blocks.BROWN_MUSHROOM),
          "feather",
          new ItemStack(Items.WHEAT_SEEDS),
          "cobblestone"));
    }
    if (magicNet) {
      ItemProjectileMagicNet magic_net = new ItemProjectileMagicNet();
      ItemRegistry.register(magic_net, "magic_net", GuideCategory.ITEMTHROW);
      EntityMagicNetEmpty.renderSnowball = magic_net;
      EntityProjectileRegistry.registerModEntity(EntityMagicNetFull.class, "magicnetfull", 1011);
      EntityProjectileRegistry.registerModEntity(EntityMagicNetEmpty.class, "magicnetempty", 1012);
      projectiles.add(magic_net);
    }
    if (dynamiteMining) {
      ItemProjectileTNT dynamite_mining = new ItemProjectileTNT(6, ExplosionType.MINING);
      ItemRegistry.register(dynamite_mining, "dynamite_mining", GuideCategory.ITEMTHROW);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, dynamite_mining);
      EntityProjectileRegistry.registerModEntity(EntityDynamiteMining.class, "tntminingbolt", 1010);
      EntityDynamiteMining.renderSnowball = dynamite_mining;
      projectiles.add(dynamite_mining);
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(dynamite_mining, 6),
          "gunpowder",
          "ingotIron",
          "gunpowder",
          "paper",
          new ItemStack(Items.CLAY_BALL),
          new ItemStack(Blocks.RED_MUSHROOM),
          "feather",
          new ItemStack(Items.WHEAT_SEEDS),
          "ingotBrickNether"));
    }
    if (enderBombsEnabled) {
      ItemProjectileTNT ender_tnt_1 = new ItemProjectileTNT(1, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_2 = new ItemProjectileTNT(2, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_3 = new ItemProjectileTNT(3, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_4 = new ItemProjectileTNT(4, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_5 = new ItemProjectileTNT(5, ExplosionType.NORMAL);
      ItemProjectileTNT ender_tnt_6 = new ItemProjectileTNT(6, ExplosionType.NORMAL);
      ItemRegistry.register(ender_tnt_1, "ender_tnt_1", null);
      ItemRegistry.register(ender_tnt_2, "ender_tnt_2", null);
      ItemRegistry.register(ender_tnt_3, "ender_tnt_3", null);
      ItemRegistry.register(ender_tnt_4, "ender_tnt_4", null);
      ItemRegistry.register(ender_tnt_5, "ender_tnt_5", null);
      ItemRegistry.register(ender_tnt_6, "ender_tnt_6", null);
      GuideItem page = GuideRegistry.register(GuideCategory.ITEMTHROW, ender_tnt_1);
      EntityProjectileRegistry.registerModEntity(EntityDynamite.class, "tntbolt", 1007);
      projectiles.add(ender_tnt_1);
      projectiles.add(ender_tnt_2);
      projectiles.add(ender_tnt_3);
      projectiles.add(ender_tnt_4);
      projectiles.add(ender_tnt_5);
      projectiles.add(ender_tnt_6);
      //first the basic recipes
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_1, 12), new ItemStack(Blocks.TNT), "paper", new ItemStack(Items.CLAY_BALL), "enderpearl"));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_5), new ItemStack(Items.CLAY_BALL)));
      //default recipes are added already insice the IRecipe
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(ender_tnt_1), new ItemStack(Items.CLAY_BALL)));
      //two 3s is four 2s
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(ender_tnt_2), new ItemStack(Items.CLAY_BALL)));
      //four 3s is two 4s is one 5
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_5), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(ender_tnt_3), new ItemStack(Items.CLAY_BALL)));
      page.addRecipePage(RecipeRegistry.addShapelessRecipe(new ItemStack(ender_tnt_6), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(ender_tnt_4), new ItemStack(Items.CLAY_BALL)));
      LootTableRegistry.registerLoot(ender_tnt_6);
    }
    if (enableAir) {
      ItemCharmAir charm_air = new ItemCharmAir();
      ItemRegistry.register(charm_air, "charm_air", GuideCategory.ITEMBAUBLES);
      ModCyclic.instance.events.register(charm_air);
      LootTableRegistry.registerLoot(charm_air);
    }
    if (enableFire) {
      ItemCharmFire charm_fire = new ItemCharmFire();
      ItemRegistry.register(charm_fire, "charm_fire", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_fire);
    }
    if (enableSea) {
      ItemCharmBoat charm_boat = new ItemCharmBoat();
      ItemRegistry.register(charm_boat, "charm_boat", GuideCategory.ITEMBAUBLES);
    }
    if (enableVoid) {
      ItemCharmVoid charm_void = new ItemCharmVoid();
      ItemRegistry.register(charm_void, "charm_void", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_void);
    }
    if (enableWater) {
      ItemCharmWater charm_water = new ItemCharmWater();
      ItemRegistry.register(charm_water, "charm_water", GuideCategory.ITEMBAUBLES);
    }
    if (antidoteCharm) {
      ItemCharmAntidote charm_antidote = new ItemCharmAntidote();
      ItemRegistry.register(charm_antidote, "charm_antidote", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_antidote);
    }
    if (slowfallCharm) {
      ItemCharmSlowfall charm_wing = new ItemCharmSlowfall();
      ItemRegistry.register(charm_wing, "charm_wing", GuideCategory.ITEMBAUBLES);
      LootTableRegistry.registerLoot(charm_wing);
    }
    if (autoTorch) {
      ItemAutoTorch tool_auto_torch = new ItemAutoTorch();
      ItemRegistry.register(tool_auto_torch, "tool_auto_torch", GuideCategory.ITEMBAUBLES);
      ModCyclic.instance.events.register(tool_auto_torch);
      LootTableRegistry.registerLoot(tool_auto_torch);
    }
    if (enableSpeed) {
      ItemCharmSpeed charm_speed = new ItemCharmSpeed();
      ItemRegistry.register(charm_speed, "charm_speed", GuideCategory.ITEMBAUBLES);
    }
    if (foodStep) {
      ItemAppleStep food_step = new ItemAppleStep();
      ItemRegistry.register(food_step, "food_step");
      ModCyclic.instance.events.register(food_step);
    }
    if (mountInverse) {
      ItemStirrupsReverse stirrup_inverse = new ItemStirrupsReverse();
      ItemRegistry.register(stirrup_inverse, "tool_mount_inverse");
    }
    if (enableHorseFoodUpgrades) {
      Item emerald_carrot = new ItemHorseUpgrade(HorseUpgradeType.TYPE, new ItemStack(Items.FERMENTED_SPIDER_EYE));
      Item lapis_carrot = new ItemHorseUpgrade(HorseUpgradeType.VARIANT, new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
      Item diamond_carrot = new ItemHorseUpgrade(HorseUpgradeType.HEALTH, new ItemStack(Items.DIAMOND));
      Item redstone_carrot = new ItemHorseUpgrade(HorseUpgradeType.SPEED, new ItemStack(Items.REDSTONE));
      Item ender_carrot = new ItemHorseUpgrade(HorseUpgradeType.JUMP, new ItemStack(Items.ENDER_EYE));
      ItemRegistry.register(emerald_carrot, "horse_upgrade_type");
      ItemRegistry.register(lapis_carrot, "horse_upgrade_variant");
      ItemRegistry.register(diamond_carrot, "horse_upgrade_health");
      ItemRegistry.register(redstone_carrot, "horse_upgrade_speed");
      ItemRegistry.register(ender_carrot, "horse_upgrade_jump");
      ModCyclic.instance.events.register(this);//for SubcribeEvent hooks 
    }
    if (enableLapisApple) {
      ItemHorseTame apple_lapis = new ItemHorseTame();
      ItemRegistry.register(apple_lapis, "apple_lapis");
      ModCyclic.instance.events.register(apple_lapis);
    }
    if (enableEmeraldApple) {
      ItemVillagerMagic apple_emerald = new ItemVillagerMagic();
      ItemRegistry.register(apple_emerald, "apple_emerald");
      LootTableRegistry.registerLoot(apple_emerald);
      ModCyclic.instance.events.register(apple_emerald);
    }
    if (enableHeartContainer) {
      ItemHeartContainer heart_food = new ItemHeartContainer();
      ItemRegistry.register(heart_food, "heart_food");
      ModCyclic.instance.events.register(heart_food);
      LootTableRegistry.registerLoot(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(heart_food, ChestType.IGLOO);
    }
    if (enableInventoryCrafting) {
      ItemFoodCrafting crafting_food = new ItemFoodCrafting();
      ItemRegistry.register(crafting_food, "crafting_food");
      LootTableRegistry.registerLoot(crafting_food);
    }
    if (enableInventoryUpgrade) {
      ItemFoodInventory inventory_food = new ItemFoodInventory();
      ItemRegistry.register(inventory_food, "inventory_food");
      LootTableRegistry.registerLoot(inventory_food);
    }
    if (enableCorruptedChorus) {
      ItemChorusCorrupted corrupted_chorus = new ItemChorusCorrupted();
      ItemRegistry.register(corrupted_chorus, "corrupted_chorus");
      ModCyclic.instance.events.register(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus);
      LootTableRegistry.registerLoot(corrupted_chorus, ChestType.ENDCITY);
    }
    if (enableGlowingChorus) {
      ItemChorusGlowing glowing_chorus = new ItemChorusGlowing();
      ItemRegistry.register(glowing_chorus, "glowing_chorus");
      ModCyclic.instance.events.register(glowing_chorus);
    }
  }

  @Override
  public void onPostInit() {
    for (BaseItemProjectile item : projectiles) {
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorProjectileThrowable(item));
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    enableChaos = config.getBoolean("ChaosSiren", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableMissile = config.getBoolean("MagicMissile", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    magicNet = config.getBoolean("MonsterBall", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteSafe = config.getBoolean("DynamiteSafe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteMining = config.getBoolean("DynamiteMining", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBlaze = config.getBoolean("EnderBlaze", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderDungeonFinder = config.getBoolean("EnderDungeonFinder", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    // enderFishing = config.getBoolean("EnderFishing", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderSnow = config.getBoolean("EnderSnow", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWool = config.getBoolean("EnderWool", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderTorch = config.getBoolean("EnderTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderLightning = config.getBoolean("EnderLightning", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderWater = config.getBoolean("EnderWater", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enderBombsEnabled = config.getBoolean("EnderBombs", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableAir = config.getBoolean("AirCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpeed = config.getBoolean("SpeedCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    slowfallCharm = config.getBoolean("WingCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableFire = config.getBoolean("FireCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSea = config.getBoolean("SailorCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableVoid = config.getBoolean("VoidCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableWater = config.getBoolean("WaterCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    antidoteCharm = config.getBoolean("AntidoteCharm", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    autoTorch = config.getBoolean("AutomaticTorch", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    foodStep = config.getBoolean("AppleStature", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    mountInverse = config.getBoolean("StirrupInverse", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLapisApple = config.getBoolean("LapisApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableGlowingChorus = config.getBoolean("GlowingChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEmeraldApple = config.getBoolean("EmeraldApple", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHeartContainer = config.getBoolean("HeartContainer(food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryCrafting = config.getBoolean("InventoryCrafting(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableInventoryUpgrade = config.getBoolean("InventoryUpgrade(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCorruptedChorus = config.getBoolean("CorruptedChorus(Food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHorseFoodUpgrades = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String category = Const.ConfigCategory.modpackMisc;
    ItemHorseUpgrade.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemHorseUpgrade.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemHorseUpgrade.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
    this.enderEyeReuse = config.getBoolean("item.ender_eye_orb", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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
