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
import java.util.List;
import com.lothrazar.cyclicmagic.component.ore.BlockDimensionOre;
import com.lothrazar.cyclicmagic.component.ore.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEmeraldHeight;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEndOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenGoldRiver;
import com.lothrazar.cyclicmagic.world.gen.WorldGenNetherOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenOreSingleton;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldModule extends BaseEventModule implements IHasConfig {
  final static int weightOre = 0;
  final static int weightPlants = 2;
  public static boolean netherOreEnabled;
  public static boolean endOreEnabled;
  public static boolean oreSpawns = true;
  public static boolean pigmenEnrage = true;
  private static boolean emeraldHeight = true;
  private static boolean goldRiver;
  private static boolean oreSingletons;

  public static BlockDimensionOre end_redstone_ore;
  public static BlockDimensionOre end_coal_ore;
  public static BlockDimensionOre end_lapis_ore;
  public static BlockDimensionOre end_iron_ore;
  public static BlockDimensionOre end_emerald_ore;
  public static BlockDimensionOre end_diamond_ore;
  public static BlockDimensionOre end_gold_ore;

  public static List<BlockDimensionOre> ores = new ArrayList<BlockDimensionOre>();

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.worldGen;
    config.setCategoryComment(category, "Control any blocks that get generated in new chunks & new worlds");
    Property prop;
    prop = config.get(category, "Nether Ore", true, "Generate ore in the nether.");
    prop.setRequiresMcRestart(true);
    netherOreEnabled = prop.getBoolean();
    prop = config.get(category, "End Ore", true, "Generate ore in the end.");
    prop.setRequiresMcRestart(true);
    endOreEnabled = prop.getBoolean();
    prop = config.get(category, "Infested Ores", true, "These dimension ores (nether and end) have a chance to spawn endermites or silverfish");
    oreSpawns = prop.getBoolean();
    prop = config.get(category, "PigmenEnrage", false, "If true, mining Nether ore has a 20% chance of enraging a nearby PigZombie within 16 blocks");
    pigmenEnrage = prop.getBoolean();
    prop = config.get(category, "Emerald Ore Boost", true, "Vanilla emerald ore now can spawn at any height, not only below the ground [still only in the Extreme Hills biomes as normal]");
    prop.setRequiresMcRestart(true);
    emeraldHeight = prop.getBoolean();
    prop = config.get(category, "Gold Rivers", true, "Vanilla gold ore can spawn in and river biomes at any height");
    prop.setRequiresMcRestart(true);
    goldRiver = prop.getBoolean();
    prop = config.get(category, "Ore Singletons", true, "Vanilla ores of all kinds can rarely spawn at all world heights, but only in veins of size one.  Great for amplified terrain.");
    prop.setRequiresMcRestart(true);
    oreSingletons = prop.getBoolean();
    //NEW ORES start here
    category = Const.ConfigCategory.worldGen + ".netherorecustom";
    final String blockCountDesc = "Approximate ore vein size.  Zero means no spawns.";
    final String spawnChanceDesc = "Chance of a vein to spawn.  Zero means no spawns.";
    for (BlockDimensionOre ore : WorldModule.ores) {
      ore.config.setBlockCount( 
          config.getInt(ore.config.getBlockCountConfig(), ore.config.getConfigCategory(), 8, 0, 32, blockCountDesc));
      ore.config.setSpawnChance(
          config.getInt(ore.config.getSpawnChanceConfig(), ore.config.getConfigCategory(), 8, 0, 32, spawnChanceDesc));
    }
    category = Const.ConfigCategory.worldGen + ".endorecustom";
    WorldGenEndOre.Configs.blockCountCoal = config.getInt("blockCountCoal", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountDiamond = config.getInt("blockCountDiamond", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountEmerald = config.getInt("blockCountEmerald", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountRedstone = config.getInt("blockCountRedstone", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountLapis = config.getInt("blockCountLapis", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountIron = config.getInt("blockCountIron", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountGold = config.getInt("blockCountGold", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.spawnChanceCoal = config.getInt("spawnChanceCoal", category, 20, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceDiamond = config.getInt("spawnChanceDiamond", category, 1, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceEmerald = config.getInt("spawnChanceEmerald", category, 1, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceRedstone = config.getInt("spawnChanceRedstone", category, 18, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceLapis = config.getInt("spawnChanceLapis", category, 15, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceIron = config.getInt("spawnChanceIron", category, 12, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceGold = config.getInt("spawnChanceGold", category, 12, 0, 100, spawnChanceDesc);

  }
  @Override
  public void onPreInit() {

    super.onPreInit();
    if (netherOreEnabled || endOreEnabled) {
      registerDimensionOres();
    }
  }
  @Override
  public void onInit() {
    //syncConfig comes AFTER pre init then init. which is why the configs require a restart 

    if (netherOreEnabled) {
      GameRegistry.registerWorldGenerator(new WorldGenNetherOre(), weightOre);
    }
    if (endOreEnabled) {
      GameRegistry.registerWorldGenerator(new WorldGenEndOre(), weightOre);
    }
    if (emeraldHeight) {
      GameRegistry.registerWorldGenerator(new WorldGenEmeraldHeight(), weightOre);
    }
    if (goldRiver) {
      GameRegistry.registerWorldGenerator(new WorldGenGoldRiver(), weightOre);
    }
    if (oreSingletons) {
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.IRON_ORE, 68), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.GOLD_ORE, 34), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.LAPIS_ORE, 34), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.REDSTONE_ORE, 16), weightOre);
      GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.DIAMOND_ORE, 16), weightOre);
    }
  }
  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null && event.getState().getBlock() instanceof BlockDimensionOre) {
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      if (oreSpawns) {
        //then try spawning mob- silverfish etc
        BlockDimensionOre block = (BlockDimensionOre) event.getState().getBlock();
        block.trySpawnTriggeredEntity(world, pos);
      }
      if (pigmenEnrage && world.rand.nextDouble() < 0.2) {
        //then look for one 
        AxisAlignedBB range = UtilEntity.makeBoundingBox(pos.getX(), pos.getY(), pos.getZ(), 3, 16);
        List<EntityPigZombie> found = world.getEntitiesWithinAABB(EntityPigZombie.class, range);
        for (EntityPigZombie pz : found) {
          if (pz.isAngry() == false) {
            pz.attackEntityFrom(DamageSource.causePlayerDamage(event.getHarvester()), 0);
            // one enraged, is enough
            break;
          }
        }
      }
    }
  }
  //nether ores
  /*
   * ForgeHooks.class
   * 
   * Blocks.IRON_ORE.setHarvestLevel("pickaxe", 1); Blocks.IRON_BLOCK.setHarvestLevel("pickaxe", 1); Blocks.LAPIS_ORE.setHarvestLevel("pickaxe", 1); Blocks.LAPIS_BLOCK.setHarvestLevel("pickaxe", 1);
   * Blocks.QUARTZ_ORE.setHarvestLevel("pickaxe", 0);
   */
  final int coalHarvest = 0;
  final int ironHarvest = 1;
  final int lapisHarvest = ironHarvest;
  final int emeraldHarvest = 2;
  final int diamondHarvest = emeraldHarvest;
  final int goldHarvest = emeraldHarvest;
  final int redstoneHarvest = emeraldHarvest;
  private void registerDimensionOres() {
    BlockDimensionOre nether_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    nether_redstone_ore.setPickaxeHarvestLevel(ironHarvest)
        .setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.REDSTONE)
        .registerOreDict("oreRedstone");
    BlockRegistry.registerBlock(nether_redstone_ore, "nether_redstone_ore", null);
    nether_redstone_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCount(8)
        .setSpawnChance(8);
    ores.add(nether_redstone_ore);

    BlockDimensionOre nether_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 12);//iron nugget
    nether_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron");
    BlockRegistry.registerBlock(nether_iron_ore, "nether_iron_ore", null);
    nether_iron_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountIron")
        .setSpawnChanceConfig("spawnChanceIron")
        .setBlockCount(12)
        .setSpawnChance(10);
    ores.add(nether_iron_ore);

    BlockDimensionOre nether_gold_ore = new BlockDimensionOre(Items.GOLD_NUGGET, 0, 4);
    nether_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.SILVERFISH, 1)
        .registerSmeltingOutput(Items.GOLD_INGOT)
        .registerOreDict("oreGold");
    BlockRegistry.registerBlock(nether_gold_ore, "nether_gold_ore", null);
    nether_gold_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountGold")
        .setSpawnChanceConfig("spawnChanceGold")
        .setBlockCount(8)
        .setSpawnChance(40);
    ores.add(nether_gold_ore);

    BlockDimensionOre nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.SILVERFISH, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal");
    BlockRegistry.registerBlock(nether_coal_ore, "nether_coal_ore", null);
    nether_coal_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountCoal")
        .setSpawnChanceConfig("spawnChanceCoal")
        .setBlockCount(8)
        .setSpawnChance(20);
    ores.add(nether_coal_ore);

    BlockDimensionOre nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis");
    BlockRegistry.registerBlock(nether_lapis_ore, "nether_lapis_ore", null);
    nether_lapis_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountLapis")
        .setSpawnChanceConfig("spawnChanceLapis")
        .setBlockCount(8)
        .setSpawnChance(10);
    ores.add(nether_lapis_ore);

    BlockDimensionOre nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.SILVERFISH, 5)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald");
    BlockRegistry.registerBlock(nether_emerald_ore, "nether_emerald_ore", null);
    nether_emerald_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountEmerald")
        .setSpawnChanceConfig("spawnChanceEmerald")
        .setBlockCount(8)
        .setSpawnChance(1);
    ores.add(nether_emerald_ore);

    BlockDimensionOre nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.SILVERFISH, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond");
    BlockRegistry.registerBlock(nether_diamond_ore, "nether_diamond_ore", null);
    nether_diamond_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountDiamond")
        .setSpawnChanceConfig("spawnChanceDiamond")
        .setBlockCount(8)
        .setSpawnChance(1);
    ores.add(nether_diamond_ore);

    //end ores
    end_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    end_redstone_ore.setPickaxeHarvestLevel(redstoneHarvest).setSpawnType(SpawnType.ENDERMITE, 3)
        .registerSmeltingOutput(Items.REDSTONE)
        .registerOreDict("oreRedstone");
    BlockRegistry.registerBlock(end_redstone_ore, "end_redstone_ore", null);

    end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.ENDERMITE, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal");
    BlockRegistry.registerBlock(end_coal_ore, "end_coal_ore", null);

    end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.ENDERMITE, 5)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis");
    BlockRegistry.registerBlock(end_lapis_ore, "end_lapis_ore", null);

    end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald");
    BlockRegistry.registerBlock(end_emerald_ore, "end_emerald_ore", null);

    end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond");
    BlockRegistry.registerBlock(end_diamond_ore, "end_diamond_ore", null);

    end_gold_ore = new BlockDimensionOre(Items.GOLD_INGOT);
    end_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.GOLD_INGOT)
        .registerOreDict("oreGold");
    BlockRegistry.registerBlock(end_gold_ore, "end_gold_ore", null);

    end_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 16);//iron nugget

    end_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron");
    BlockRegistry.registerBlock(end_iron_ore, "end_iron_ore", null);

    GuideItem page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(nether_gold_ore), "world.netherore.title");
    page.addTextPage("world.netherore.guide");
    page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(end_redstone_ore), "world.endore.title");
    page.addTextPage("world.endore.guide");
  }
}
