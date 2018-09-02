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
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideItem;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.item.ItemFangs;
import com.lothrazar.cyclicmagic.item.ItemLeverRemote;
import com.lothrazar.cyclicmagic.item.ItemPlayerLauncher;
import com.lothrazar.cyclicmagic.item.ItemRotateBlock;
import com.lothrazar.cyclicmagic.item.ItemSoulstone;
import com.lothrazar.cyclicmagic.item.ItemWarpSurface;
import com.lothrazar.cyclicmagic.item.ItemWaterSpreader;
import com.lothrazar.cyclicmagic.item.buildswap.ItemBuildSwapper;
import com.lothrazar.cyclicmagic.item.buildswap.ItemBuildSwapper.WandType;
import com.lothrazar.cyclicmagic.item.cannon.EntityGolemLaser;
import com.lothrazar.cyclicmagic.item.cannon.ItemProjectileCannon;
import com.lothrazar.cyclicmagic.item.core.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.crashtestdummy.EntityRobot;
import com.lothrazar.cyclicmagic.item.crashtestdummy.ItemCrashSpawner;
import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.item.dynamite.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.item.dynamite.ItemProjectileTNT.ExplosionType;
import com.lothrazar.cyclicmagic.item.endereye.EntityEnderEyeUnbreakable;
import com.lothrazar.cyclicmagic.item.endereye.ItemEnderEyeReuse;
import com.lothrazar.cyclicmagic.item.enderpearl.ItemEnderPearlReuse;
import com.lothrazar.cyclicmagic.item.equipbauble.ItemGloveClimb;
import com.lothrazar.cyclicmagic.item.equipment.ItemGlowingHelmet;
import com.lothrazar.cyclicmagic.item.equipment.crystal.ItemPowerArmor;
import com.lothrazar.cyclicmagic.item.equipment.crystal.ItemPowerSword;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldArmor;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldAxe;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldHoe;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldPickaxe;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldSpade;
import com.lothrazar.cyclicmagic.item.equipment.emerald.ItemEmeraldSword;
import com.lothrazar.cyclicmagic.item.equipment.nether.ItemNetherbrickAxe;
import com.lothrazar.cyclicmagic.item.equipment.nether.ItemNetherbrickHoe;
import com.lothrazar.cyclicmagic.item.equipment.nether.ItemNetherbrickPickaxe;
import com.lothrazar.cyclicmagic.item.equipment.nether.ItemNetherbrickSpade;
import com.lothrazar.cyclicmagic.item.equipment.sandstone.ItemSandstoneAxe;
import com.lothrazar.cyclicmagic.item.equipment.sandstone.ItemSandstoneHoe;
import com.lothrazar.cyclicmagic.item.equipment.sandstone.ItemSandstonePickaxe;
import com.lothrazar.cyclicmagic.item.equipment.sandstone.ItemSandstoneSpade;
import com.lothrazar.cyclicmagic.item.merchant.ItemMerchantAlmanac;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecartChest;
import com.lothrazar.cyclicmagic.item.minecart.EntityGoldMinecartDispenser;
import com.lothrazar.cyclicmagic.item.minecart.EntityMinecartDropper;
import com.lothrazar.cyclicmagic.item.minecart.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.item.minecart.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemDropperMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemGoldMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemStoneMinecart;
import com.lothrazar.cyclicmagic.item.minecart.ItemTurretMinecart;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseUpgrade;
import com.lothrazar.cyclicmagic.item.mobs.ItemHorseUpgrade.HorseUpgradeType;
import com.lothrazar.cyclicmagic.item.scythe.ItemScythe;
import com.lothrazar.cyclicmagic.item.storagesack.ItemStorageBag;
import com.lothrazar.cyclicmagic.item.tiletransporter.ItemChestSack;
import com.lothrazar.cyclicmagic.item.tiletransporter.ItemChestSackEmpty;
import com.lothrazar.cyclicmagic.playerupgrade.ItemHeartContainer;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.tweak.dispenser.BehaviorProjectileThrowable;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.BlockDispenser;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ItemModule extends BaseModule implements IHasConfig {

  public static int intColor(int r, int g, int b) {
    return (r * 65536 + g * 256 + b);
  }

  private boolean goldMinecart;
  private boolean stoneMinecart;
  private boolean chestMinecart;
  private boolean dropperMinecart;
  private boolean dispenserMinecart;
  private boolean turretMinecart;
  private boolean enableHeartContainer;
  private boolean enableHorseFoodUpgrades;
  public static ArrayList<BaseItemProjectile> projectiles = new ArrayList<BaseItemProjectile>();
  private boolean dynamiteSafe;
  private boolean dynamiteMining;
  private boolean enableHarvestLeaves;
  private boolean enableToolHarvest;
  private boolean enableHarvestWeeds;
  private boolean enablePearlReuse;
  private boolean enableSwappers;
  private boolean enablePearlReuseMounted;
  private boolean storageBagEnabled;
  private boolean enableChestSack;

  private boolean enablewaterSpread;

  private boolean enableBlockRot;
  private boolean enableCGlove;
  private boolean enableElevate;
  private boolean enableLever;
  private boolean enableTrader;
  private boolean enableSoulstone;
  private boolean enablePlayerLauncher;
  private boolean evokerFang;
  private boolean enderEyeReuse;
  public static ItemStorageBag storage_bag = null;//ref by ContainerStorage
  private boolean enableEmeraldGear;
  private boolean enableSandstoneTools;
  private boolean enablePurpleGear;
  private boolean enablePurpleSwords;
  private boolean glowingHelmet;
  private boolean robotSpawner;
  private boolean rainbowCannon;
  private boolean enableNetherbrickTools;
  private boolean enableHeartToxic;

  @Override
  public void onPreInit() {


    if (robotSpawner) {
      EntityRegistry.registerModEntity(new ResourceLocation(Const.MODID, EntityRobot.NAME), EntityRobot.class, EntityRobot.NAME, 1030, ModCyclic.instance, 64, 1, true);
      EntityRegistry.registerEgg(new ResourceLocation(Const.MODID, EntityRobot.NAME), intColor(159, 255, 222), intColor(222, 111, 51));
      ItemCrashSpawner spawner = new ItemCrashSpawner();
      ItemRegistry.register(spawner, "robot_spawner", GuideCategory.TRANSPORT);
      ModCyclic.instance.events.register(spawner);
    }
    if (rainbowCannon) {
      ItemRegistry.register(new ItemProjectileCannon(), "laser_cannon", GuideCategory.ITEMTHROW);
      EntityRegistry.registerModEntity(new ResourceLocation(Const.MODID, EntityGolemLaser.NAME), EntityGolemLaser.class, EntityGolemLaser.NAME, 1031, ModCyclic.instance, 64, 1, true);
    }
    if (goldMinecart) {
      ItemGoldMinecart gold_minecart = new ItemGoldMinecart();
      ItemRegistry.register(gold_minecart, "gold_minecart", GuideCategory.TRANSPORT);
      EntityGoldMinecart.dropItem = gold_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecart.class, "goldminecart", 1100);
      ItemGoldFurnaceMinecart gold_furnace_minecart = new ItemGoldFurnaceMinecart();
      ItemRegistry.register(gold_furnace_minecart, "gold_furnace_minecart", GuideCategory.TRANSPORT);
      EntityGoldFurnaceMinecart.dropItem = gold_furnace_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldFurnaceMinecart.class, "goldfurnaceminecart", 1101);
    }
    if (stoneMinecart) {
      ItemStoneMinecart stone_minecart = new ItemStoneMinecart();
      ItemRegistry.register(stone_minecart, "stone_minecart", GuideCategory.TRANSPORT);
      EntityStoneMinecart.dropItem = stone_minecart;
      EntityProjectileRegistry.registerModEntity(EntityStoneMinecart.class, "stoneminecart", 1102);
    }
    if (chestMinecart) {
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartChest.class, "goldchestminecart", 1103);
    }
    if (dropperMinecart) {
      ItemDropperMinecart dropper_minecart = new ItemDropperMinecart();
      ItemRegistry.register(dropper_minecart, "dropper_minecart", GuideCategory.TRANSPORT);
      EntityMinecartDropper.dropItem = dropper_minecart;
      EntityProjectileRegistry.registerModEntity(EntityMinecartDropper.class, "golddropperminecart", 1104);
    }
    if (dispenserMinecart) {
      //BROKEN:
      //it spawns entity in the world. so like an arrow, it flies to the arget but then magically teleports back o teh  cart position
      //stop for now
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartDispenser.class, "golddispenserminecart", 1105);
    }
    if (turretMinecart) {
      ItemTurretMinecart turret_minecart = new ItemTurretMinecart();
      ItemRegistry.register(turret_minecart, "turret_minecart", GuideCategory.TRANSPORT);
      EntityMinecartTurret.dropItem = turret_minecart;
      EntityProjectileRegistry.registerModEntity(EntityMinecartTurret.class, "turretminecart", 1106);
    }
    //if i have a mob on a LEAD< i can put it in a minecart with thehit
    //maybe 2 passengers..?? idk
    //connect together??
    //DISPENSERR minecart
    //??FLUID CART?
    //TURRET CART:? shoots arrows
    //ONE THAT CAN HOLD ANY ITEM
    if (enableEmeraldGear) {
      ItemEmeraldArmor emerald_head = new ItemEmeraldArmor(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(emerald_head, "emerald_helmet", null);
      Item emerald_chest = new ItemEmeraldArmor(EntityEquipmentSlot.CHEST);
      ItemRegistry.register(emerald_chest, "emerald_chestplate", null);
      Item emerald_legs = new ItemEmeraldArmor(EntityEquipmentSlot.LEGS);
      ItemRegistry.register(emerald_legs, "emerald_leggings", null);
      Item emerald_boots = new ItemEmeraldArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.register(emerald_boots, "emerald_boots", null);
      Item emerald_sword = new ItemEmeraldSword();
      ItemRegistry.register(emerald_sword, "emerald_sword", null);
      Item emerald_pickaxe = new ItemEmeraldPickaxe();
      ItemRegistry.register(emerald_pickaxe, "emerald_pickaxe", null);
      Item emerald_axe = new ItemEmeraldAxe();
      ItemRegistry.register(emerald_axe, "emerald_axe", null);
      Item emerald_shovel = new ItemEmeraldSpade();
      ItemRegistry.register(emerald_shovel, "emerald_spade", null);
      Item emerald_hoe = new ItemEmeraldHoe();
      ItemRegistry.register(emerald_hoe, "emerald_hoe", null);
      LootTableRegistry.registerLoot(emerald_pickaxe);
      LootTableRegistry.registerLoot(emerald_sword);
      LootTableRegistry.registerLoot(emerald_chest);
      GuideRegistry.register(GuideCategory.GEAR, emerald_head, "item.emeraldgear.title", "item.emeraldgear.guide");
    }
    if (enablePurpleGear) {
      Item purple_boots = new ItemPowerArmor(EntityEquipmentSlot.FEET);
      ItemRegistry.register(purple_boots, "purple_boots", GuideCategory.GEAR);
      Item purple_leggings = new ItemPowerArmor(EntityEquipmentSlot.LEGS);
      ItemRegistry.register(purple_leggings, "purple_leggings", GuideCategory.GEAR);
      Item purple_chestplate = new ItemPowerArmor(EntityEquipmentSlot.CHEST);
      ItemRegistry.register(purple_chestplate, "purple_chestplate", GuideCategory.GEAR);
      Item purple_helmet = new ItemPowerArmor(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(purple_helmet, "purple_helmet", GuideCategory.GEAR);
    }
    if (glowingHelmet) {
      Item glowing_helmet = new ItemGlowingHelmet(EntityEquipmentSlot.HEAD);
      ItemRegistry.register(glowing_helmet, "glowing_helmet", GuideCategory.GEAR);
      ModCyclic.instance.events.register(glowing_helmet);
    }
    if (enablePurpleSwords) {
      ItemPowerSword sword_weakness = new ItemPowerSword(ItemPowerSword.SwordType.WEAK);
      ItemRegistry.register(sword_weakness, "sword_weakness", GuideCategory.GEAR);
      ItemPowerSword sword_slowness = new ItemPowerSword(ItemPowerSword.SwordType.SLOW);
      ItemRegistry.register(sword_slowness, "sword_slowness", GuideCategory.GEAR);
      ItemPowerSword sword_ender = new ItemPowerSword(ItemPowerSword.SwordType.ENDER);
      ItemRegistry.register(sword_ender, "sword_ender", GuideCategory.GEAR);
    }
    if (enableNetherbrickTools) {
      Item netherbrick_pickaxe = new ItemNetherbrickPickaxe();
      ItemRegistry.register(netherbrick_pickaxe, "netherbrick_pickaxe", null);
      Item netherbrick_axe = new ItemNetherbrickAxe();
      ItemRegistry.register(netherbrick_axe, "netherbrick_axe", null);
      Item netherbrick_spade = new ItemNetherbrickSpade();
      ItemRegistry.register(netherbrick_spade, "netherbrick_spade", null);
      Item netherbrick_hoe = new ItemNetherbrickHoe();
      ItemRegistry.register(netherbrick_hoe, "netherbrick_hoe", null);
      //NETHER CHESTYPE
      //      LootTableRegistry.registerLoot(sandstone_pickaxe, ChestType.);
      //      LootTableRegistry.registerLoot(sandstone_axe, ChestType.BONUS);
      //      LootTableRegistry.registerLoot(sandstone_spade, ChestType.BONUS);
      GuideRegistry.register(GuideCategory.GEAR, netherbrick_axe, "item.netherbrick_axe.name", "item.netherbrick_axe.guide");
    }
    if (enableSandstoneTools) {
      Item sandstone_pickaxe = new ItemSandstonePickaxe();
      ItemRegistry.register(sandstone_pickaxe, "sandstone_pickaxe", null);
      Item sandstone_axe = new ItemSandstoneAxe();
      ItemRegistry.register(sandstone_axe, "sandstone_axe", null);
      Item sandstone_spade = new ItemSandstoneSpade();
      ItemRegistry.register(sandstone_spade, "sandstone_spade", null);
      Item sandstone_hoe = new ItemSandstoneHoe();
      ItemRegistry.register(sandstone_hoe, "sandstone_hoe", null);
      LootTableRegistry.registerLoot(sandstone_pickaxe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_axe, ChestType.BONUS);
      LootTableRegistry.registerLoot(sandstone_spade, ChestType.BONUS);
      GuideRegistry.register(GuideCategory.GEAR, sandstone_axe, "item.sandstonegear.title", "item.sandstonegear.guide");
    }
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
      ItemLeverRemote password_remote = new ItemLeverRemote();
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

    if (enderEyeReuse) {
      ItemEnderEyeReuse ender_eye_orb = new ItemEnderEyeReuse();
      ItemRegistry.register(ender_eye_orb, "ender_eye_orb");
      EntityProjectileRegistry.registerModEntity(EntityEnderEyeUnbreakable.class, "ender_eye_orb", 1029);
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

    if (storageBagEnabled) {
      storage_bag = new ItemStorageBag();
      ItemRegistry.register(storage_bag, "storage_bag");
      ModCyclic.instance.events.register(storage_bag);
      LootTableRegistry.registerLoot(storage_bag, ChestType.BONUS);
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


    if (enableSwappers) {
      ItemBuildSwapper tool_swap = new ItemBuildSwapper(WandType.NORMAL);
      ItemRegistry.register(tool_swap, "tool_swap");
      ModCyclic.instance.events.register(tool_swap);
      ItemBuildSwapper tool_swap_match = new ItemBuildSwapper(WandType.MATCH);
      ItemRegistry.register(tool_swap_match, "tool_swap_match");
      ModCyclic.instance.events.register(tool_swap_match);
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


    //TODO : class exntentions for ths guys? 
    if (enableHeartContainer) {
      ItemHeartContainer heart_food = new ItemHeartContainer(1);
      ItemRegistry.register(heart_food, "heart_food");
      ModCyclic.instance.events.register(heart_food);
      LootTableRegistry.registerLoot(heart_food);
      LootTableRegistry.registerLoot(heart_food, ChestType.ENDCITY);
      LootTableRegistry.registerLoot(heart_food, ChestType.IGLOO);
    }
    if (enableHeartToxic) {
      ItemHeartContainer heart_toxic = new ItemHeartContainer(-1);
      ItemRegistry.register(heart_toxic, "heart_toxic");
    }

  }

  @Override
  public void onInit() {
    ModCyclic.proxy.initColors();
  }

  @Override
  public void onPostInit() {
    for (BaseItemProjectile item : projectiles) {
      BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(item, new BehaviorProjectileThrowable(item));
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    enableHeartToxic = config.getBoolean("heart_toxic", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    rainbowCannon = config.getBoolean("laser_cannon", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    robotSpawner = config.getBoolean("robot_spawner", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    chestMinecart = false;// config.getBoolean("GoldChestMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dispenserMinecart = false;//config.getBoolean("GoldDispenserMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dropperMinecart = config.getBoolean("GoldDropperMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    turretMinecart = config.getBoolean("GoldTurretMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    goldMinecart = config.getBoolean("GoldMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    stoneMinecart = config.getBoolean("StoneMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteSafe = config.getBoolean("DynamiteSafe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dynamiteMining = config.getBoolean("DynamiteMining", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHeartContainer = config.getBoolean("HeartContainer(food)", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHorseFoodUpgrades = config.getBoolean("HorseFood", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String category = Const.ConfigCategory.modpackMisc;
    ItemHorseUpgrade.HEARTS_MAX = config.getInt("HorseFood Max Hearts", category, 20, 1, 100, "Maximum number of upgraded hearts");
    ItemHorseUpgrade.JUMP_MAX = config.getInt("HorseFood Max Jump", category, 6, 1, 20, "Maximum value of jump.  Naturally spawned/bred horses seem to max out at 5.5");
    ItemHorseUpgrade.SPEED_MAX = config.getInt("HorseFood Max Speed", category, 50, 1, 99, "Maximum value of speed (this is NOT blocks/per second or anything like that)");
    enderEyeReuse = config.getBoolean("item.ender_eye_orb", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    evokerFang = config.getBoolean("EvokerFang", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePlayerLauncher = config.getBoolean("PlayerLauncher", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSoulstone = config.getBoolean("Soulstone", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableTrader = config.getBoolean("Merchant Almanac", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableLever = config.getBoolean("Remote Lever", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableElevate = config.getBoolean("RodElevation", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableCGlove = config.getBoolean("ClimbingGlove", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableBlockRot = config.getBoolean("BlockRotator", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablewaterSpread = config.getBoolean("WaterSpreader", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    storageBagEnabled = config.getBoolean("StorageBag", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuse = config.getBoolean("EnderOrb", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestWeeds = config.getBoolean("BrushScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableToolHarvest = config.getBoolean("HarvestScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableHarvestLeaves = config.getBoolean("TreeScythe", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSwappers = config.getBoolean("ExchangeScepters", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePearlReuseMounted = config.getBoolean("EnderOrbMounted", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableChestSack = config.getBoolean("ChestSack", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    String[] deflist = new String[] { "minecraft:mob_spawner", "minecraft:obsidian" };
    ItemBuildSwapper.swapBlacklist = config.getStringList("ExchangeSceptersBlacklist", Const.ConfigCategory.items, deflist, "Blocks that will not be broken by the exchange scepters.  It will also not break anything that is unbreakable (such as bedrock), regardless of if its in this list or not.  ");
    glowingHelmet = config.getBoolean("GlowingHelmet", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePurpleGear = config.getBoolean("PurpleArmor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSandstoneTools = config.getBoolean("SandstoneTools", Const.ConfigCategory.content, true, "Sandstone tools are between wood and stone. " + Const.ConfigCategory.contentDefaultText);
    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. " + Const.ConfigCategory.contentDefaultText);
    enablePurpleSwords = config.getBoolean("SwordsFrostEnder", Const.ConfigCategory.content, true, "Enable the epic swords. " + Const.ConfigCategory.contentDefaultText);
    enableEmeraldGear = config.getBoolean("Emerald Gear", Const.ConfigCategory.content, true, "Emerald armor and tools that are slightly weaker than diamond. " + Const.ConfigCategory.contentDefaultText);
    enableNetherbrickTools = config.getBoolean("NetherbrickTools", Const.ConfigCategory.content, true, "Netherbrick tools have mining level of stone but improved stats. " + Const.ConfigCategory.contentDefaultText);
  }
}
