package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEmeraldHeight;
import com.lothrazar.cyclicmagic.world.gen.WorldGenEndOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenGoldRiver;
import com.lothrazar.cyclicmagic.world.gen.WorldGenNetherOre;
import com.lothrazar.cyclicmagic.world.gen.WorldGenOreSingleton;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
  private static boolean emeraldHeight = true;
  private static boolean goldRiver;
  private static boolean oreSingletons;
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
    Property prop;
    prop = config.get(category, "Nether Ore", true, "Generate ore in the nether.");
    prop.setRequiresMcRestart(true);
    netherOreEnabled = prop.getBoolean();
    prop = config.get(category, "End Ore", true, "Generate ore in the end.");
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
    WorldGenNetherOre.Configs.spawnChanceDiamond = config.getInt("spawnChanceDiamond", category, 1, 0, 100, spawnChanceDesc);
    WorldGenNetherOre.Configs.spawnChanceEmerald = config.getInt("spawnChanceEmerald", category, 1, 0, 100, spawnChanceDesc);
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
    WorldGenEndOre.Configs.spawnChanceDiamond = config.getInt("spawnChanceDiamond", category, 1, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceEmerald = config.getInt("spawnChanceEmerald", category, 1, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceRedstone = config.getInt("spawnChanceRedstone", category, 18, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceLapis = config.getInt("spawnChanceLapis", category, 15, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceIron = config.getInt("spawnChanceIron", category, 12, 0, 100, spawnChanceDesc);
    WorldGenEndOre.Configs.spawnChanceGold = config.getInt("spawnChanceGold", category, 12, 0, 100, spawnChanceDesc);
  }
  @Override
  public void onPreInit() {
    if (netherOreEnabled || endOreEnabled) {
      registerDimensionOres();
    }
  }
  @Override
  public void onInit() {
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
      //then try spawning mob
      //EntityPlayer player = event.getPlayer();
      BlockPos pos = event.getPos();
      World world = event.getWorld();
      BlockDimensionOre block = (BlockDimensionOre) event.getState().getBlock();
      block.trySpawnTriggeredEntity(world, pos);
    }
  }
  //nether ores
  /*
   * ForgeHooks.class
   * 
   * Blocks.IRON_ORE.setHarvestLevel("pickaxe", 1); Blocks.IRON_BLOCK.setHarvestLevel("pickaxe", 1); Blocks.LAPIS_ORE.setHarvestLevel("pickaxe", 1); Blocks.LAPIS_BLOCK.setHarvestLevel("pickaxe", 1);
   * Blocks.QUARTZ_ORE.setHarvestLevel("pickaxe", 0);
   */
  int coalHarvest = 0;
  int ironHarvest = 1;
  int lapisHarvest = ironHarvest;
  int emeraldHarvest = 2;
  int diamondHarvest = emeraldHarvest;
  int goldHarvest = emeraldHarvest;
  int redstoneHarvest = emeraldHarvest;
  private void registerDimensionOres() {
    nether_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    nether_redstone_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_redstone_ore, "nether_redstone_ore", null);
    nether_redstone_ore.registerSmeltingOutput(Items.REDSTONE);
    nether_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 12);//iron nugget
    nether_redstone_ore.registerOre("oreRedstone");
    nether_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_iron_ore, "nether_iron_ore", null);
    nether_iron_ore.registerSmeltingOutput(Items.IRON_INGOT);
    nether_iron_ore.registerOre("oreIron");
    nether_gold_ore = new BlockDimensionOre(Items.GOLD_NUGGET, 0, 4);
    nether_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.SILVERFISH, 1);
    BlockRegistry.registerBlock(nether_gold_ore, "nether_gold_ore", null);
    nether_gold_ore.registerSmeltingOutput(Items.GOLD_INGOT);
    nether_gold_ore.registerOre("oreGold");
    nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.SILVERFISH, 1);
    BlockRegistry.registerBlock(nether_coal_ore, "nether_coal_ore", null);
    nether_coal_ore.registerSmeltingOutput(Items.COAL);
    nether_coal_ore.registerOre("oreCoal");
    nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.SILVERFISH, 2);
    BlockRegistry.registerBlock(nether_lapis_ore, "nether_lapis_ore", null);
    nether_lapis_ore.registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
    nether_lapis_ore.registerOre("oreLapis");
    nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.SILVERFISH, 5);
    BlockRegistry.registerBlock(nether_emerald_ore, "nether_emerald_ore", null);
    nether_emerald_ore.registerSmeltingOutput(Items.EMERALD);
    nether_emerald_ore.registerOre("oreEmerald");
    nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.SILVERFISH, 8);
    BlockRegistry.registerBlock(nether_diamond_ore, "nether_diamond_ore", null);
    nether_diamond_ore.registerSmeltingOutput(Items.DIAMOND);
    nether_diamond_ore.registerOre("oreDiamond");
    //end ores
    end_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    end_redstone_ore.setPickaxeHarvestLevel(redstoneHarvest).setSpawnType(SpawnType.ENDERMITE, 3);
    BlockRegistry.registerBlock(end_redstone_ore, "end_redstone_ore", null);
    end_redstone_ore.registerSmeltingOutput(Items.REDSTONE);
    end_redstone_ore.registerOre("oreRedstone");
    end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setPickaxeHarvestLevel(coalHarvest).setSpawnType(SpawnType.ENDERMITE, 1);
    BlockRegistry.registerBlock(end_coal_ore, "end_coal_ore", null);
    end_coal_ore.registerSmeltingOutput(Items.COAL);
    end_coal_ore.registerOre("oreCoal");
    end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setPickaxeHarvestLevel(lapisHarvest).setSpawnType(SpawnType.ENDERMITE, 5);
    BlockRegistry.registerBlock(end_lapis_ore, "end_lapis_ore", null);
    end_lapis_ore.registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()));
    end_lapis_ore.registerOre("oreLapis");
    end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setPickaxeHarvestLevel(emeraldHarvest).setSpawnType(SpawnType.ENDERMITE, 8);
    BlockRegistry.registerBlock(end_emerald_ore, "end_emerald_ore", null);
    end_emerald_ore.registerSmeltingOutput(Items.EMERALD);
    end_emerald_ore.registerOre("oreEmerald");
    end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setPickaxeHarvestLevel(diamondHarvest).setSpawnType(SpawnType.ENDERMITE, 8);
    BlockRegistry.registerBlock(end_diamond_ore, "end_diamond_ore", null);
    end_diamond_ore.registerSmeltingOutput(Items.DIAMOND);
    end_diamond_ore.registerOre("oreDiamond");
    end_gold_ore = new BlockDimensionOre(Items.GOLD_INGOT);
    end_gold_ore.setPickaxeHarvestLevel(goldHarvest).setSpawnType(SpawnType.ENDERMITE, 2);
    BlockRegistry.registerBlock(end_gold_ore, "end_gold_ore", null);
    end_gold_ore.registerSmeltingOutput(Items.GOLD_INGOT);
    end_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 16);//iron nugget
    end_gold_ore.registerOre("oreGold");
    end_iron_ore.setPickaxeHarvestLevel(ironHarvest).setSpawnType(SpawnType.ENDERMITE, 2);
    BlockRegistry.registerBlock(end_iron_ore, "end_iron_ore", null);
    end_iron_ore.registerSmeltingOutput(Items.IRON_INGOT);
    end_iron_ore.registerOre("oreIron");
    GuideItem page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(nether_gold_ore), "world.netherore.title");
    page.addTextPage("world.netherore.guide");
    page = GuideRegistry.register(GuideCategory.WORLD, Item.getItemFromBlock(end_redstone_ore), "world.endore.title");
    page.addTextPage("world.endore.guide");
  }
}
