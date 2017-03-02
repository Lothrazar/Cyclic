package com.lothrazar.cyclicmagic.module;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEmeraldHeight;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEndOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenGoldRiver;
import com.lothrazar.cyclicmagic.world.gen.WorldGenNetherOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenOcean;
import com.lothrazar.cyclicmagic.world.gen.WorldGenOreSingleton;
import com.lothrazar.cyclicmagic.world.gen.WorldGenPlantBiome;
import net.minecraft.block.BlockCrops;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenModule extends BaseEventModule implements IHasConfig {
  private final static int spawnsCarrot = 15;
  private final static int spawnsWheat = 8;
  private final static int spawnsBeetroot = 18;
  private final static int spawnsPotatoes = 10;
  final static int weightOre = 0;
  final static int weightPlants = 2;
  public static boolean oceanEnabled;
  public static boolean netherOreEnabled;
  public static boolean endOreEnabled;
  public static boolean oreSpawns = true;
  private static boolean emeraldHeight = true;
  private static boolean goldRiver;
  private static boolean oreSingletons;
  private static boolean biomeCrops;
  public static BlockDimensionOre nether_gold_ore;
  public static BlockDimensionOre nether_coal_ore;
  public static BlockDimensionOre nether_lapis_ore;
  public static BlockDimensionOre nether_emerald_ore;
  public static BlockDimensionOre nether_diamond_ore;
  public static BlockDimensionOre nether_iron_ore;
  public static BlockDimensionOre end_redstone_ore;
  public static BlockDimensionOre end_coal_ore;
  public static BlockDimensionOre end_lapis_ore;
  public static BlockDimensionOre end_iron_ore;
  public static BlockDimensionOre end_emerald_ore;
  public static BlockDimensionOre end_diamond_ore;
  public static BlockDimensionOre end_gold_ore;
  public static BlockDimensionOre nether_redstone_ore;
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.worldGen;
    config.setCategoryComment(category, "Control any blocks that get generated in new chunks & new worlds");
    Property prop = config.get(category, "Classic Oceans", true, "Generate clay, sand, and dirt in the ocean instead of only gravel (like the old days)");
    prop.setRequiresWorldRestart(true);
    oceanEnabled = prop.getBoolean();
    prop = config.get(category, "Nether Ore", true, "Generate ore in netherrack (lapis, emerald, gold, coal, diamond).  The gold gives nuggets when mined");
    prop.setRequiresMcRestart(true);
    netherOreEnabled = prop.getBoolean();
    prop = config.get(category, "End Ore", true, "Generate ore in the end (lapis, emerald, redstone, coal, diamond)");
    prop.setRequiresMcRestart(true);
    endOreEnabled = prop.getBoolean();
    prop = config.get(category, "Infested Ores", true, "These dimension ores (nether and end) have a chance to spawn endermites or silverfish");
    oreSpawns = prop.getBoolean();
    prop = config.get(category, "Emerald Ore Boost", true, "Vanilla emerald ore now can spawn at any height, not only below the ground [still only in the Extreme Hills biomes as normal]");
    prop.setRequiresMcRestart(true);
    emeraldHeight = prop.getBoolean();
    prop = config.get(category, "Gold Rivers", true, "Vanilla gold ore can spawn in and river biomes at any height");
    prop.setRequiresMcRestart(true);
    goldRiver = prop.getBoolean();
    prop = config.get(category, "Ore Singletons", true, "Vanilla ores of all kinds can rarely spawn at all world heights, but only in veins of size one.  Great for amplified terrain.");
    prop.setRequiresMcRestart(true);
    oreSingletons = prop.getBoolean();
    prop = config.get(category, "Biome Crops", true, "Crops spawn randomly with nature.  Carrots in extreme hills, wheat in plains, beetroot in forests, potatoes in taiga.");
    prop.setRequiresMcRestart(true);
    biomeCrops = prop.getBoolean();
    WorldGenOcean.syncConfig(config);
    category = Const.ConfigCategory.worldGen + ".netherorecustom";
    String blockCountDesc = "Approximate ore vein size.  Zero means no spawns.";
    String spawnChanceDesc = "Chance of a vein to spawn.  Zero means no spawns.";
    WorldGenNetherOre.Configs.blockCountCoal = config.getInt("blockCountCoal", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountDiamond = config.getInt("blockCountDiamond", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountEmerald = config.getInt("blockCountEmerald", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountIron = config.getInt("blockCountIron", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountGold = config.getInt("blockCountGold", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountLapis = config.getInt("blockCountLapis", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.blockCountRedstone = config.getInt("blockCountRedstone", category, 8, 0, 32, blockCountDesc);
    WorldGenNetherOre.Configs.spawnChanceCoal = config.getInt("spawnChanceCoal", category, 25, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceDiamond = config.getInt("spawnChanceDiamond", category, 6, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceEmerald = config.getInt("spawnChanceEmerald", category, 5, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceGold = config.getInt("spawnChanceGold", category, 45, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceLapis = config.getInt("spawnChanceLapis", category, 10, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceIron = config.getInt("spawnChanceIron", category, 15, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceRedstone = config.getInt("spawnChanceRedstone", category, 8, 0, 100, spawnChanceDesc);
    category = Const.ConfigCategory.worldGen + ".endorecustom";
    WorldGenEndOre.Configs.blockCountCoal = config.getInt("blockCountCoal", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountDiamond = config.getInt("blockCountDiamond", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountEmerald = config.getInt("blockCountEmerald", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountRedstone = config.getInt("blockCountRedstone", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountLapis = config.getInt("blockCountLapis", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountIron = config.getInt("blockCountIron", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.blockCountGold = config.getInt("blockCountGold", category, 8, 0, 32, blockCountDesc);
    WorldGenEndOre.Configs.spawnChanceCoal = config.getInt("spawnChanceCoal", category, 20, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceDiamond = config.getInt("spawnChanceDiamond", category, 10, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceEmerald = config.getInt("spawnChanceEmerald", category, 10, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceRedstone = config.getInt("spawnChanceRedstone", category, 18, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceLapis = config.getInt("spawnChanceLapis", category, 15, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceIron = config.getInt("spawnChanceIron", category, 12, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceGold = config.getInt("spawnChanceGold", category, 12, 0, 100, spawnChanceDesc);
  }
  @Override
  public void onInit() {
    if (oceanEnabled) {
      GameRegistry.registerWorldGenerator(new WorldGenOcean(), weightOre);
    }
    if (netherOreEnabled || endOreEnabled) {
      registerDimensionOres();
    }
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
    if (biomeCrops) {
      GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops) Blocks.CARROTS, Arrays.asList(Biomes.EXTREME_HILLS), spawnsCarrot), weightPlants);
      GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops) Blocks.WHEAT, Arrays.asList(Biomes.PLAINS), spawnsWheat), weightPlants);
      GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops) Blocks.BEETROOTS, Arrays.asList(Biomes.FOREST, Biomes.BIRCH_FOREST), spawnsBeetroot), weightPlants);
      GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops) Blocks.POTATOES, Arrays.asList(Biomes.TAIGA), spawnsPotatoes), weightPlants);
    }
  }
  @SubscribeEvent
  public void onHarvestDropsEvent(HarvestDropsEvent event) {
    if (event.getState() != null && event.getState().getBlock() instanceof BlockDimensionOre) {
      //then try spawning mob
      //EntityPlayer player = event.getPlayer();
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      BlockDimensionOre block = (BlockDimensionOre) event.getState().getBlock();
      block.trySpawnTriggeredEntity(world, pos);
    }
  }
  private void registerDimensionOres() {
    //nether ores
    /*
     * ForgeHooks.class
     * 
     * Blocks.IRON_ORE.setHarvestLevel("pickaxe", 1);
     * Blocks.IRON_BLOCK.setHarvestLevel("pickaxe", 1);
     * Blocks.LAPIS_ORE.setHarvestLevel("pickaxe", 1);
     * Blocks.LAPIS_BLOCK.setHarvestLevel("pickaxe", 1);
     * Blocks.QUARTZ_ORE.setHarvestLevel("pickaxe", 0);
     */
    //ALL BELOW ARE 2
    /*
     * Block[] oreBlocks = new Block[] { Blocks.EMERALD_ORE,
     * Blocks.EMERALD_BLOCK, Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK,
     * Blocks.GOLD_ORE, Blocks.GOLD_BLOCK, Blocks.REDSTONE_ORE,
     * Blocks.LIT_REDSTONE_ORE }; for (Block block : oreBlocks) {
     * block.setHarvestLevel("pickaxe", 2); }
     */
    int coalHarvest = 0;
    int ironHarvest = 1;
    int lapisHarvest = ironHarvest;
    int emeraldHarvest = 2;
    int diamondHarvest = emeraldHarvest;
    int goldHarvest = emeraldHarvest;
    int redstoneHarvest = emeraldHarvest;

    nether_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    nether_redstone_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_redstone_ore, "nether_redstone_ore");
    nether_redstone_ore.registerSmeltingOutput(Items.REDSTONE);
    
    nether_iron_ore = new BlockDimensionOre(Items.IRON_INGOT);
    nether_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_iron_ore, "nether_iron_ore");
    nether_iron_ore.registerSmeltingOutput(Items.IRON_INGOT);
    
    nether_gold_ore = new BlockDimensionOre(Items.GOLD_NUGGET,0,4);
    nether_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.SILVERFISH, 1);
    BlockRegistry.registerBlock(nether_gold_ore, "nether_gold_ore");
    nether_gold_ore.registerSmeltingOutput(Items.GOLD_INGOT);
    
    nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.SILVERFISH, 1);
    BlockRegistry.registerBlock(nether_coal_ore, "nether_coal_ore");
    nether_coal_ore.registerSmeltingOutput(Items.COAL);
    nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_lapis_ore, "nether_lapis_ore");
    nether_lapis_ore.registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
    nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.SILVERFISH, 5);
    BlockRegistry.registerBlock(nether_emerald_ore, "nether_emerald_ore");
    nether_emerald_ore.registerSmeltingOutput(Items.EMERALD);
    nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.SILVERFISH, 8);
    BlockRegistry.registerBlock(nether_diamond_ore, "nether_diamond_ore");
    nether_diamond_ore.registerSmeltingOutput(Items.DIAMOND);
    //end ores
    end_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    end_redstone_ore.setPickaxeHarvestLevel(redstoneHarvest).setSpawnType(SpawnType.ENDERMITE, 3);
    BlockRegistry.registerBlock(end_redstone_ore, "end_redstone_ore");
    end_redstone_ore.registerSmeltingOutput(Items.REDSTONE);
    end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.ENDERMITE, 1);
    BlockRegistry.registerBlock(end_coal_ore, "end_coal_ore");
    end_coal_ore.registerSmeltingOutput(Items.COAL);
    end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.ENDERMITE, 5);
    BlockRegistry.registerBlock(end_lapis_ore, "end_lapis_ore");
    end_lapis_ore.registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
    end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.ENDERMITE, 8);
    BlockRegistry.registerBlock(end_emerald_ore, "end_emerald_ore");
    end_emerald_ore.registerSmeltingOutput(Items.EMERALD);
    end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.ENDERMITE, 8);
    BlockRegistry.registerBlock(end_diamond_ore, "end_diamond_ore");
    end_diamond_ore.registerSmeltingOutput(Items.DIAMOND);
    

    end_gold_ore = new BlockDimensionOre(Items.GOLD_INGOT);
    end_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.ENDERMITE, 2);
    BlockRegistry.registerBlock(end_gold_ore, "end_gold_ore");
    end_gold_ore.registerSmeltingOutput(Items.GOLD_INGOT);
    

    end_iron_ore = new BlockDimensionOre(Items.IRON_INGOT);
    end_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.ENDERMITE, 2);
    BlockRegistry.registerBlock(end_iron_ore, "end_iron_ore");
    end_iron_ore.registerSmeltingOutput(Items.IRON_INGOT);
  }
}
