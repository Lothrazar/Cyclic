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
package com.lothrazar.cyclicmagic.registry.module;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.CyclicContent;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.BlockLaunch;
import com.lothrazar.cyclicmagic.block.BlockSpikesRetractable;
import com.lothrazar.cyclicmagic.block.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.block.buildershape.TileEntityStructureBuilder;
import com.lothrazar.cyclicmagic.block.cable.energy.BlockPowerCable;
import com.lothrazar.cyclicmagic.block.cable.energy.TileEntityCablePower;
import com.lothrazar.cyclicmagic.block.cable.fluid.BlockCableFluid;
import com.lothrazar.cyclicmagic.block.cable.fluid.TileEntityFluidCable;
import com.lothrazar.cyclicmagic.block.cable.item.BlockCableItem;
import com.lothrazar.cyclicmagic.block.cable.item.TileEntityItemCable;
import com.lothrazar.cyclicmagic.block.cable.multi.BlockCableBundle;
import com.lothrazar.cyclicmagic.block.cable.multi.TileEntityCableBundle;
import com.lothrazar.cyclicmagic.block.cablepump.energy.BlockEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.energy.TileEntityEnergyPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.BlockFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.fluid.TileEntityFluidPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.BlockItemPump;
import com.lothrazar.cyclicmagic.block.cablepump.item.TileEntityItemPump;
import com.lothrazar.cyclicmagic.block.cablewireless.content.BlockCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.content.TileCableContentWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.BlockCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.cablewireless.energy.TileCableEnergyWireless;
import com.lothrazar.cyclicmagic.block.controlledminer.TileEntityControlledMiner;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyor.SpeedType;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorAngle;
import com.lothrazar.cyclicmagic.block.conveyor.BlockConveyorCorner;
import com.lothrazar.cyclicmagic.block.fire.BlockFireFrost;
import com.lothrazar.cyclicmagic.block.fire.BlockFireSafe;
import com.lothrazar.cyclicmagic.block.firestarter.BlockFireStarter;
import com.lothrazar.cyclicmagic.block.firestarter.TileEntityFireStarter;
import com.lothrazar.cyclicmagic.block.hydrator.RecipeHydrate;
import com.lothrazar.cyclicmagic.block.peat.BlockPeat;
import com.lothrazar.cyclicmagic.block.peat.ItemBiomass;
import com.lothrazar.cyclicmagic.block.peat.ItemPeatFuel;
import com.lothrazar.cyclicmagic.block.peat.farm.BlockPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.farm.TileEntityPeatFarm;
import com.lothrazar.cyclicmagic.block.peat.generator.BlockPeatGenerator;
import com.lothrazar.cyclicmagic.block.peat.generator.TileEntityPeatGenerator;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffolding;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingReplace;
import com.lothrazar.cyclicmagic.block.scaffolding.BlockScaffoldingResponsive;
import com.lothrazar.cyclicmagic.block.scaffolding.ItemBlockScaffolding;
import com.lothrazar.cyclicmagic.block.sorting.BlockItemCableSort;
import com.lothrazar.cyclicmagic.block.sorting.TileEntityItemCableSort;
import com.lothrazar.cyclicmagic.block.wireless.BlockRedstoneWireless;
import com.lothrazar.cyclicmagic.block.wireless.ItemBlockWireless;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessRec;
import com.lothrazar.cyclicmagic.block.wireless.TileEntityWirelessTr;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideItem;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.item.buildswap.ItemBuildSwapper;
import com.lothrazar.cyclicmagic.item.buildswap.ItemBuildSwapper.WandType;
import com.lothrazar.cyclicmagic.item.core.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteBlockSafe;
import com.lothrazar.cyclicmagic.item.dynamite.EntityDynamiteMining;
import com.lothrazar.cyclicmagic.item.dynamite.ItemProjectileTNT;
import com.lothrazar.cyclicmagic.item.dynamite.ItemProjectileTNT.ExplosionType;
import com.lothrazar.cyclicmagic.item.enderpearl.ItemEnderPearlReuse;
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
import com.lothrazar.cyclicmagic.item.firemagic.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.item.firemagic.ItemProjectileBlaze;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
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
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry;
import com.lothrazar.cyclicmagic.registry.LootTableRegistry.ChestType;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class MultiContent implements IContent {

  public static ArrayList<BaseItemProjectile> projectiles = new ArrayList<BaseItemProjectile>();
  public static ItemStorageBag storage_bag = null;//ref by ContainerStorage
  private boolean goldMinecart;
  private boolean stoneMinecart;
  private boolean chestMinecart;
  private boolean dropperMinecart;
  private boolean dispenserMinecart;
  private boolean turretMinecart;
  private boolean enableHeartContainer;
  private boolean enableHorseFoodUpgrades;
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
  private boolean enableEmeraldGear;
  private boolean enableSandstoneTools;
  private boolean enablePurpleGear;
  private boolean enablePurpleSwords;
  private boolean glowingHelmet;
  private boolean enableNetherbrickTools;
  private boolean enableHeartToxic;
  private boolean fragileEnabled;
  private boolean enablePumpAndPipes;
  private boolean enableSpikes;
  private boolean wireless;
  private boolean enablePeat;
  private boolean enableConveyor;
  private boolean launchPads;
  private boolean fire_starter;
  private boolean enableEnderBlaze;
  boolean fireDarkUsed = false;
  boolean fireFrostUsed = false;
  private boolean cableWireless;

  @Override
  public void register() {
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
    if (fire_starter) {
      BlockRegistry.registerBlock(new BlockFireStarter(), "fire_starter", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityFireStarter.class, "fire_starter_te");
      fireDarkUsed = true;
      fireFrostUsed = true;
    }
    if (enableEnderBlaze) {
      fireDarkUsed = true;
      Item fire_dark_anim = new Item();
      ItemRegistry.register(fire_dark_anim, "fire_dark_anim");
      ItemProjectileBlaze ender_blaze = new ItemProjectileBlaze();
      ender_blaze.setRepairItem(new ItemStack(fire_dark_anim));
      ItemRegistry.register(ender_blaze, "ender_blaze", GuideCategory.ITEMTHROW);
      EntityProjectileRegistry.registerModEntity(EntityBlazeBolt.class, "blazebolt", 1008);
      ModCyclic.instance.events.register(ender_blaze);
    }
    //TODO maybe a Fire REgistry LUL? 
    if (fireFrostUsed) {
      BlockRegistry.registerBlock(new BlockFireFrost(), "fire_frost", null);
    }
    if (fireDarkUsed) {
      BlockRegistry.registerBlock(new BlockFireSafe(), "fire_dark", null);
    }
    if (launchPads) {
      //med
      BlockLaunch plate_launch_med = new BlockLaunch(BlockLaunch.LaunchType.MEDIUM, SoundEvents.BLOCK_SLIME_FALL);
      BlockRegistry.registerBlock(plate_launch_med, "plate_launch_med", GuideCategory.BLOCKPLATE);
      //small
      BlockLaunch plate_launch_small = new BlockLaunch(BlockLaunch.LaunchType.SMALL, SoundEvents.BLOCK_SLIME_STEP);
      BlockRegistry.registerBlock(plate_launch_small, "plate_launch_small", null);
      //large
      BlockLaunch plate_launch_large = new BlockLaunch(BlockLaunch.LaunchType.LARGE, SoundEvents.BLOCK_SLIME_BREAK);
      BlockRegistry.registerBlock(plate_launch_large, "plate_launch_large", null);
    }
    if (enableConveyor) {
      //corner
      BlockConveyorCorner plate_push__med_corner = new BlockConveyorCorner(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push__med_corner, null, "plate_push_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_med_angle = new BlockConveyorAngle(SpeedType.MEDIUM);
      BlockRegistry.registerBlock(plate_push_med_angle, null, "plate_push_med_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_med = new BlockConveyor(plate_push__med_corner, plate_push_med_angle);
      BlockRegistry.registerBlock(plate_push_med, "plate_push", GuideCategory.BLOCKPLATE);
      plate_push__med_corner.setDrop(plate_push_med);
      plate_push_med_angle.setDrop(plate_push_med);
      plate_push_med_angle.setCorner(plate_push__med_corner);
      plate_push__med_corner.setAngled(plate_push_med_angle);
      //corner
      BlockConveyorCorner plate_push_fast_corner = new BlockConveyorCorner(SpeedType.LARGE);
      BlockRegistry.registerBlock(plate_push_fast_corner, null, "plate_push_fast_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_fast_angle = new BlockConveyorAngle(SpeedType.LARGE);
      BlockRegistry.registerBlock(plate_push_fast_angle, null, "plate_push_fast_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_fast = new BlockConveyor(plate_push_fast_corner, plate_push_fast_angle);
      BlockRegistry.registerBlock(plate_push_fast, "plate_push_fast", null);
      plate_push_fast_corner.setDrop(plate_push_fast);
      plate_push_fast_angle.setDrop(plate_push_fast);
      plate_push_fast_angle.setCorner(plate_push_fast_corner);
      plate_push_fast_corner.setAngled(plate_push_fast_angle);
      //corner
      BlockConveyorCorner plate_push_slow_corner = new BlockConveyorCorner(SpeedType.SMALL);
      BlockRegistry.registerBlock(plate_push_slow_corner, null, "plate_push_slow_corner", GuideCategory.BLOCKPLATE, false);
      // angle
      BlockConveyorAngle plate_push_slow_angle = new BlockConveyorAngle(SpeedType.SMALL);
      BlockRegistry.registerBlock(plate_push_slow_angle, null, "plate_push_slow_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_slow = new BlockConveyor(plate_push_slow_corner, plate_push_slow_angle);
      BlockRegistry.registerBlock(plate_push_slow, "plate_push_slow", null);
      plate_push_slow_corner.setDrop(plate_push_slow);
      plate_push_slow_angle.setDrop(plate_push_slow);
      plate_push_slow_angle.setCorner(plate_push_slow_corner);
      plate_push_slow_corner.setAngled(plate_push_slow_corner);
      //corner
      BlockConveyorCorner plate_push_slowest_corner = new BlockConveyorCorner(SpeedType.TINY);
      BlockRegistry.registerBlock(plate_push_slowest_corner, null, "plate_push_slowest_corner", GuideCategory.BLOCKPLATE, false);
      //angle
      BlockConveyorAngle plate_push_slowest_angle = new BlockConveyorAngle(SpeedType.TINY);
      BlockRegistry.registerBlock(plate_push_slowest_angle, null, "plate_push_slowest_angle", GuideCategory.BLOCKPLATE, false);
      //main
      BlockConveyor plate_push_slowest = new BlockConveyor(plate_push_slowest_corner, plate_push_slowest_angle);
      BlockRegistry.registerBlock(plate_push_slowest, "plate_push_slowest", null);
      plate_push_slowest_corner.setDrop(plate_push_slowest);
      plate_push_slowest_angle.setDrop(plate_push_slowest);
      plate_push_slowest_angle.setCorner(plate_push_slowest_corner);
      plate_push_slowest_corner.setAngled(plate_push_slowest_angle);
    }
    if (wireless) {
      BlockRedstoneWireless wireless_transmitter = new BlockRedstoneWireless(BlockRedstoneWireless.WirelessType.TRANSMITTER);
      BlockRedstoneWireless wireless_receiver = new BlockRedstoneWireless(BlockRedstoneWireless.WirelessType.RECEIVER);
      BlockRegistry.registerBlock(wireless_transmitter, new ItemBlockWireless(wireless_transmitter), "wireless_transmitter", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(wireless_receiver, "wireless_receiver", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityWirelessTr.class, "wireless_transmitter_te");
      GameRegistry.registerTileEntity(TileEntityWirelessRec.class, "wireless_receiver_te");
      ModCyclic.instance.events.register(BlockRedstoneWireless.class);
    }
    if (enableSpikes) {
      BlockSpikesRetractable spikes_iron = new BlockSpikesRetractable(false);
      BlockRegistry.registerBlock(spikes_iron, "spikes_iron", GuideCategory.BLOCK);
      BlockSpikesRetractable spikes_redstone_diamond = new BlockSpikesRetractable(true);
      BlockRegistry.registerBlock(spikes_redstone_diamond, "spikes_diamond", GuideCategory.BLOCK);
    }
    if (enablePeat) {
      //peat 
      ItemBiomass peat_biomass = new ItemBiomass();
      ItemRegistry.register(peat_biomass, "peat_biomass", GuideCategory.ITEM);
      ItemPeatFuel peat_fuel = new ItemPeatFuel(null);
      ItemRegistry.register(peat_fuel, "peat_fuel", GuideCategory.ITEM);
      ItemRegistry.register(new ItemPeatFuel(peat_fuel), "peat_fuel_enriched", GuideCategory.ITEM);
      //
      RecipeHydrate.addRecipe(new RecipeHydrate(
          new ItemStack[] {
              new ItemStack(Items.WHEAT_SEEDS),
              new ItemStack(Blocks.RED_FLOWER, 1, OreDictionary.WILDCARD_VALUE),
              new ItemStack(Blocks.LEAVES),
              new ItemStack(Blocks.VINE) },
          new ItemStack(peat_biomass, 8)));
      //sapling one
      RecipeHydrate.addRecipe(new RecipeHydrate(
          new ItemStack[] {
              new ItemStack(Items.REEDS),
              new ItemStack(Blocks.TALLGRASS, 1, 1),
              new ItemStack(Blocks.LEAVES),
              new ItemStack(Blocks.SAPLING, 1, OreDictionary.WILDCARD_VALUE) },
          new ItemStack(peat_biomass, 8)));
      BlockPeat bog = new BlockPeat(null);
      BlockRegistry.registerBlock(bog, "peat_unbaked", GuideCategory.BLOCKMACHINE);
      BlockRegistry.registerBlock(new BlockPeat(peat_fuel), "peat_baked", GuideCategory.BLOCKMACHINE);
      Block peat_generator = new BlockPeatGenerator(peat_fuel);
      BlockRegistry.registerBlock(peat_generator, "peat_generator", GuideCategory.BLOCKMACHINE);
      BlockRegistry.registerBlock(new BlockPeatFarm(peat_generator), "peat_farm", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileEntityPeatGenerator.class, Const.MODID + "peat_generator_te");
      GameRegistry.registerTileEntity(TileEntityPeatFarm.class, Const.MODID + "peat_farm_te");
    }
    if (cableWireless) {
      BlockCableContentWireless batteryw = new BlockCableContentWireless();
      BlockRegistry.registerBlock(batteryw, "cable_wireless", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileCableContentWireless.class, Const.MODID + "cable_wireless_te");
      // energy
      BlockCableEnergyWireless w_energy = new BlockCableEnergyWireless();
      BlockRegistry.registerBlock(w_energy, "cable_wireless_energy", GuideCategory.BLOCKMACHINE);
      GameRegistry.registerTileEntity(TileCableEnergyWireless.class, Const.MODID + "cable_wireless_energy_te");
    }
    // BOTH cableWirelses AND laser uses this 
    if (cableWireless || CyclicContent.laser.enabled()) {
      ItemLocation card_location = new ItemLocation();
      ItemRegistry.register(card_location, "card_location", GuideCategory.ITEM);
    }
    if (fragileEnabled) {
      BlockScaffolding block_fragile = new BlockScaffolding(true);
      ItemBlock ib = new ItemBlockScaffolding(block_fragile);
      BlockRegistry.registerBlock(block_fragile, ib, "block_fragile", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingResponsive block_fragile_auto = new BlockScaffoldingResponsive();
      ib = new ItemBlockScaffolding(block_fragile_auto);
      BlockRegistry.registerBlock(block_fragile_auto, ib, "block_fragile_auto", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
      BlockScaffoldingReplace block_fragile_weak = new BlockScaffoldingReplace();
      ib = new ItemBlockScaffolding(block_fragile_weak);
      BlockRegistry.registerBlock(block_fragile_weak, ib, "block_fragile_weak", GuideCategory.BLOCK);
      ModCyclic.instance.events.register(ib);
    }
    if (enablePumpAndPipes) {
      //sort
      BlockRegistry.registerBlock(new BlockItemCableSort(), "item_pipe_sort", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityItemCableSort.class, "item_pipe_sort_te");
      //Item
      BlockRegistry.registerBlock(new BlockCableItem(), "item_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockItemPump(), "item_pump", GuideCategory.BLOCK);
      GameRegistry.registerTileEntity(TileEntityItemPump.class, "item_pump_te");
      //ENERGY
      BlockRegistry.registerBlock(new BlockPowerCable(), "energy_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockEnergyPump(), "energy_pump", null);
      GameRegistry.registerTileEntity(TileEntityEnergyPump.class, "energy_pump_te");
      // FLUID 
      BlockRegistry.registerBlock(new BlockCableFluid(), "fluid_pipe", GuideCategory.BLOCK);
      BlockRegistry.registerBlock(new BlockFluidPump(), "fluid_pump", null);
      GameRegistry.registerTileEntity(TileEntityFluidPump.class, "fluid_pump_te");
      //bundled
      BlockRegistry.registerBlock(new BlockCableBundle(), "bundled_pipe", GuideCategory.BLOCK);
      //pipes // TODO: fix block registry
      GameRegistry.registerTileEntity(TileEntityItemCable.class, "item_pipe_te");
      GameRegistry.registerTileEntity(TileEntityFluidCable.class, "fluid_pipe_te");
      GameRegistry.registerTileEntity(TileEntityCablePower.class, "energy_pipe_te");
      GameRegistry.registerTileEntity(TileEntityCableBundle.class, "bundled_pipe_te");
    }
  }

  @Override
  public void syncConfig(Configuration config) {
    //    String category = Const.ConfigCategory.content;
    ItemPeatFuel.FUEL_WEAK = config.getInt("peat_fuel", Const.ConfigCategory.fuelCost, 256, 10, 99999, "Energy generated by normal Peat");
    ItemPeatFuel.FUEL_STRONG = config.getInt("peat_fuel_enriched", Const.ConfigCategory.fuelCost, 4096, 10, 99999, "Energy generated by crafted Peat");
    cableWireless = config.getBoolean("cable_wireless", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableEnderBlaze = config.getBoolean("EnderBlaze", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fire_starter = config.getBoolean("fire_starter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePumpAndPipes = config.getBoolean("PumpAndPipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    fragileEnabled = config.getBoolean("ScaffoldingBlock", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    // enablePipes = config.getBoolean("Pipes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enablePeat = config.getBoolean("PeatFeature", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText + "; this feature includes several items and blocks used by the Peat farming system");
    wireless = config.getBoolean("wireless_transmitter", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    enableSpikes = config.getBoolean("Spikes", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    TileEntityStructureBuilder.maxSize = config.getInt("builder.maxRange", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum range of the builder block that you can increase it to in the GUI");
    TileEntityStructureBuilder.maxHeight = config.getInt("builder.maxHeight", Const.ConfigCategory.modpackMisc, 64, 3, 64, "Maximum height of the builder block that you can increase it to in the GUI");
    TileEntityControlledMiner.maxHeight = config.getInt("ControlledMiner.maxHeight", Const.ConfigCategory.modpackMisc, 32, 3, 128, "Maximum height of the controlled miner block that you can increase it to in the GUI");
    enableConveyor = config.getBoolean("SlimeConveyor", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    launchPads = config.getBoolean("SlimePads", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockConveyor.doCorrections = config.getBoolean("SlimeConveyorPullCenter", Const.ConfigCategory.blocks, true, "If true, the Slime Conveyor will auto-correct entities towards the center while they are moving (keeping them away from the edge)");
    BlockConveyor.sneakPlayerAvoid = config.getBoolean("SlimeConveyorSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being pushed");
    BlockLaunch.sneakPlayerAvoid = config.getBoolean("SlimePlateSneakPlayer", Const.ConfigCategory.blocks, true, "Players can sneak to avoid being jumped");
    // TileEntityCableBase.syncConfig(config);
    TileEntityItemPump.syncConfig(config);
    TileEntityUser.syncConfig(config);
    enableHeartToxic = config.getBoolean("heart_toxic", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
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

  @Override
  public boolean enabled() {
    return true;
  }
}
