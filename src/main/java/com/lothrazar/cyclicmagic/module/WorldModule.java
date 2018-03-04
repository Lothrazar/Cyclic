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
import com.lothrazar.cyclicmagic.component.ore.WorldGenNewOre;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEmeraldHeight;
import com.lothrazar.cyclicmagic.world.gen.WorldGenGoldRiver;
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

  public static boolean netherOreEnabled;
  public static boolean endOreEnabled;
  public static boolean oreSpawns = true;
  public static boolean pigmenEnrage = true;
  private static boolean emeraldHeight = true;
  private static boolean goldRiver;
  private static boolean oreSingletons;
  public WorldModule() {
    registerDimensionOres();
  }
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

    for (BlockDimensionOre ore : WorldModule.ores) {
      category = ore.config.getConfigCategory();
      ore.config.setBlockCount(
          config.getInt(ore.config.getBlockCountConfig(), category, ore.config.getBlockCountDefault(), 0, 32, "Approximate ore vein size.  Zero means no spawns."));
      ore.config.setSpawnChance(
          config.getInt(ore.config.getSpawnChanceConfig(), category, ore.config.getSpawnChanceDefault(), 0, 100, "Chance of a vein to spawn.  Zero means no spawns."));
      ore.config.setRegistered(
          config.getBoolean(ore.config.getBlockId(), category, ore.config.isRegisteredDefault(), "Ore exists"));
      ore.config.setHarvestLevel(
          config.getInt(ore.config.getBlockId() + "_harvest_level", category,
              ore.config.getHarvestLevelDefault(), 0, 3, "Tool Harvest Level"));
    }
  }
  @Override
  public void onPreInit() {

    super.onPreInit();

    for (BlockDimensionOre ore : WorldModule.ores) {
      if (ore.config.isRegistered()) {
        BlockRegistry.registerBlock(ore, ore.config.getBlockId(), null);
        ore.setPickaxeHarvestLevel(ore.config.getHarvestLevel());
      }
    }
  }
  @Override
  public void onInit() {
    //syncConfig comes AFTER pre init then init. which is why the configs require a restart 

    GameRegistry.registerWorldGenerator(new WorldGenNewOre(), weightOre);

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
  /**
   * TODO: move this to BlockDimensionOre
   * 
   * @param event
   */
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
      if (pigmenEnrage &&
          event.getWorld().provider.getDimension() == Const.Dimension.nether &&
          world.rand.nextDouble() < 0.2) {
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
  private void registerDimensionOres() {
    final int coalHarvest = 0;
    final int ironHarvest = 1;
    final int lapisHarvest = ironHarvest;
    final int emeraldHarvest = 2;
    final int diamondHarvest = emeraldHarvest;
    final int goldHarvest = emeraldHarvest;
    final int redstoneHarvest = emeraldHarvest;
    
    
    boolean DEFAULT_MODORES_ENABLED = true;//TODO: false for release
    // mod ores

    //COPPER TIME oh yeah
    String cateogry = Const.ConfigCategory.worldGen + ".ore.copper";
    
    BlockDimensionOre nether_copper_ore = new BlockDimensionOre();
    nether_copper_ore.registerOreDict("oreCopper");
    nether_copper_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(cateogry + ".nether")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(70).setBlockId("copper_nether_ore");
    addOre(nether_copper_ore);
    BlockDimensionOre end_copper_ore = new BlockDimensionOre();
    end_copper_ore.registerOreDict("oreCopper");
    end_copper_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(cateogry + ".end")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(70).setBlockId("copper_end_ore");
    addOre(end_copper_ore);

    
    
    
    
    
    //vanilla ores
    //
    BlockDimensionOre nether_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    nether_redstone_ore
        .setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.REDSTONE)
        .registerOreDict("oreRedstone");
    nether_redstone_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCountDefault(8)
        .setSpawnChanceDefault(8).setHarvestLevelDefault(ironHarvest)
        .setBlockId("nether_redstone_ore");
    addOre(nether_redstone_ore);

    BlockDimensionOre nether_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 12);//iron nugget
    nether_iron_ore.setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron");
    nether_iron_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountIron")
        .setSpawnChanceConfig("spawnChanceIron")
        .setBlockCountDefault(12)
        .setSpawnChanceDefault(10).setHarvestLevelDefault(ironHarvest)
        .setBlockId("nether_iron_ore");
    addOre(nether_iron_ore);

    BlockDimensionOre nether_gold_ore = new BlockDimensionOre(Items.GOLD_NUGGET, 0, 4);
    nether_gold_ore.setSpawnType(SpawnType.SILVERFISH, 1)
        .registerSmeltingOutput(Items.GOLD_INGOT)
        .registerOreDict("oreGold");
    nether_gold_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountGold")
        .setSpawnChanceConfig("spawnChanceGold")
        .setBlockCountDefault(8).setHarvestLevelDefault(goldHarvest)
        .setSpawnChanceDefault(40).setBlockId("nether_gold_ore");
    addOre(nether_gold_ore);

    BlockDimensionOre nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setSpawnType(SpawnType.SILVERFISH, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal");
    nether_coal_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountCoal")
        .setSpawnChanceConfig("spawnChanceCoal")
        .setBlockCountDefault(8).setHarvestLevelDefault(coalHarvest)
        .setSpawnChanceDefault(20).setBlockId("nether_coal_ore");
    addOre(nether_coal_ore);

    BlockDimensionOre nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis");
    nether_lapis_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountLapis")
        .setSpawnChanceConfig("spawnChanceLapis")
        .setBlockCountDefault(8).setHarvestLevelDefault(lapisHarvest)
        .setSpawnChanceDefault(10).setBlockId("nether_lapis_ore");
    addOre(nether_lapis_ore);

    BlockDimensionOre nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setSpawnType(SpawnType.SILVERFISH, 5)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald");
    nether_emerald_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountEmerald")
        .setSpawnChanceConfig("spawnChanceEmerald")
        .setBlockCountDefault(8).setHarvestLevelDefault(emeraldHarvest)
        .setSpawnChanceDefault(1).setBlockId("nether_emerald_ore");
    addOre(nether_emerald_ore);

    BlockDimensionOre nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setSpawnType(SpawnType.SILVERFISH, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond");
    nether_diamond_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountDiamond")
        .setSpawnChanceConfig("spawnChanceDiamond")
        .setBlockCountDefault(8).setHarvestLevelDefault(diamondHarvest)
        .setSpawnChanceDefault(1).setBlockId("nether_diamond_ore");
    addOre(nether_diamond_ore);
    //end ores
    BlockDimensionOre end_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    end_redstone_ore.setSpawnType(SpawnType.ENDERMITE, 3)
        .registerSmeltingOutput(Items.REDSTONE)
        .registerOreDict("oreRedstone");
    end_redstone_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCountDefault(8).setHarvestLevelDefault(redstoneHarvest)
        .setSpawnChanceDefault(15).setBlockId("end_redstone_ore");
    addOre(end_redstone_ore);

    BlockDimensionOre end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setSpawnType(SpawnType.ENDERMITE, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal");
    end_coal_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCountDefault(8).setHarvestLevelDefault(coalHarvest)
        .setSpawnChanceDefault(10).setBlockId("end_coal_ore");
    addOre(end_coal_ore);

    BlockDimensionOre end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setSpawnType(SpawnType.ENDERMITE, 5)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis");
    end_lapis_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountLapis")
        .setSpawnChanceConfig("spawnChanceLapis")
        .setBlockCountDefault(8).setHarvestLevelDefault(lapisHarvest)
        .setSpawnChanceDefault(12).setBlockId("end_lapis_ore");
    addOre(end_lapis_ore);

    BlockDimensionOre end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald");
    end_emerald_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountEmerald")
        .setSpawnChanceConfig("spawnChanceEmerald")
        .setBlockCountDefault(8).setHarvestLevelDefault(emeraldHarvest)
        .setSpawnChanceDefault(1).setBlockId("end_emerald_ore");
    addOre(end_emerald_ore);

    BlockDimensionOre end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond");
    end_diamond_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountDiamond")
        .setSpawnChanceConfig("spawnChanceDiamond")
        .setBlockCountDefault(8).setHarvestLevelDefault(diamondHarvest)
        .setSpawnChanceDefault(1).setBlockId("end_diamond_ore");
    addOre(end_diamond_ore);
    BlockDimensionOre end_gold_ore = new BlockDimensionOre(Items.GOLD_INGOT);

    end_gold_ore.setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.GOLD_INGOT)
        .registerOreDict("oreGold");

    end_gold_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountGold")
        .setSpawnChanceConfig("spawnChanceGold")
        .setBlockCountDefault(8).setHarvestLevelDefault(goldHarvest)
        .setSpawnChanceDefault(4).setBlockId("end_gold_ore");
    addOre(end_gold_ore);

    BlockDimensionOre end_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 16);//iron nugget
    end_iron_ore.setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron");
    end_iron_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountIron")
        .setSpawnChanceConfig("spawnChanceIron")
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(4).setBlockId("end_iron_ore");
    addOre(end_iron_ore);
    
    GuideItem page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(nether_gold_ore), "world.netherore.title");
    page.addTextPage("world.netherore.guide");
    page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(end_redstone_ore), "world.endore.title");
    page.addTextPage("world.endore.guide");

  }
  private void addOre(BlockDimensionOre ore) {
    ores.add(ore);
  }
}
