package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.block.ore.BlockDimensionOre;
import com.lothrazar.cyclicmagic.block.ore.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.guide.GuideItem;
import com.lothrazar.cyclicmagic.guide.GuideRegistry;
import com.lothrazar.cyclicmagic.module.WorldModule;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOreRegistry {

  static boolean DEFAULT_MODORES_ENABLED = true;//TODO: false for release

  private static void addOre(BlockDimensionOre ore) {
    WorldModule.ores.add(ore);
  }

  private static BlockDimensionOre createOre(int dimension, String name, int harvestLevel, String... oreDict) {
    String block = null, dimName = null;
    if (dimension == Const.Dimension.nether) {
      dimName = "nether";
      block = "minecraft:netherrack";
    }
    else if (dimension == Const.Dimension.end) {
      dimName = "end";
      block = "minecraft:end_stone";
    }
    else if (dimension == Const.Dimension.overworld) {
      dimName = "world";
      block = "minecraft:stone";
    }
    String cateogry = name + "." + dimName;
    BlockDimensionOre ore = new BlockDimensionOre();
    ore.registerOreDict(oreDict);
    ore.config.setDimension(dimension)
        .setBlockToReplace(block)
        .setConfigCategory(cateogry)
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(harvestLevel)
        .setSpawnChanceDefault(5).setBlockId(name + "_" + dimName + "_ore");
    //    String temp = Character.toString(dimName.charAt(0)).toUpperCase() + dimName.substring(1);
    //    temp += " " + Character.toString(name.charAt(0)).toUpperCase() + name.substring(1);
    //    System.out.println("tile." + name + "_" + dimName + "_ore" + ".name=" + temp + " " + " Ore");
    addOre(ore);
    return ore;
  }

  private static void createNetherAndEndOres(String name, int harvestLevel, String oreDict) {
    createOre(Const.Dimension.nether, name, harvestLevel, "ore" + oreDict, "oreNether" + oreDict);
    createOre(Const.Dimension.end, name, harvestLevel, "ore" + oreDict, "oreEnd" + oreDict);
  }

  public static void register() {
    final int coalHarvest = 0;
    final int ironHarvest = 1;
    final int lapisHarvest = ironHarvest;
    final int emeraldHarvest = 2;
    final int diamondHarvest = emeraldHarvest;
    final int goldHarvest = emeraldHarvest;
    final int redstoneHarvest = emeraldHarvest;
    // mod ores
    createNetherAndEndOres("titanium", ironHarvest, "Titanium");// RUTLIE ORE
    createNetherAndEndOres("aquamarine", ironHarvest, "Aquamarine");
    createNetherAndEndOres("bauxite", ironHarvest, "Bauxite");
    createNetherAndEndOres("boron", ironHarvest, "Boron");
    createNetherAndEndOres("cobalt", ironHarvest, "Cobalt");
    createNetherAndEndOres("copper", ironHarvest, "Copper");
    createNetherAndEndOres("dilithium", ironHarvest, "Dilithium");
    createNetherAndEndOres("galena", ironHarvest, "Galena");
    createNetherAndEndOres("iridium", ironHarvest, "Iridium");
    createNetherAndEndOres("lead", ironHarvest, "Lead");
    createNetherAndEndOres("lithium", ironHarvest, "Lithium");
    createNetherAndEndOres("magnesium", ironHarvest, "Magnesium");
    createNetherAndEndOres("mithril", ironHarvest, "Mithril");
    createNetherAndEndOres("nickel", ironHarvest, "Nickel");
    createNetherAndEndOres("nitre", ironHarvest, "Saltpeter");
    createNetherAndEndOres("osmium", ironHarvest, "Osmium");
    createNetherAndEndOres("platinum", ironHarvest, "Platinum");
    createNetherAndEndOres("quartz_black", ironHarvest, "QuartzBlack");
    createNetherAndEndOres("quartz_certus", ironHarvest, "CertusQuartz");
    createNetherAndEndOres("quartz_certus_charged", ironHarvest, "ChargedCertusQuartz");
    createNetherAndEndOres("sapphire", ironHarvest, "Sapphire");
    createNetherAndEndOres("thorium", ironHarvest, "Thorium");
    createNetherAndEndOres("tungsten", ironHarvest, "Tungsten");
    createNetherAndEndOres("ruby", ironHarvest, "Ruby");
    createNetherAndEndOres("silver", ironHarvest, "Silver");
    createNetherAndEndOres("tin", ironHarvest, "Tin");
    createNetherAndEndOres("uranium", ironHarvest, "Uranium");
    createNetherAndEndOres("yellorite", ironHarvest, "Yellorium");
    //vanilla ores
    BlockDimensionOre nether_redstone_ore = new BlockDimensionOre(Items.REDSTONE);
    nether_redstone_ore
        .setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.REDSTONE)
        .registerOreDict("oreRedstone", "oreNetherRedstone");
    nether_redstone_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCountDefault(8).setVanilla()
        .setSpawnChanceDefault(8).setHarvestLevelDefault(ironHarvest)
        .setBlockId("nether_redstone_ore");
    addOre(nether_redstone_ore);
    BlockDimensionOre nether_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 12);//iron nugget
    nether_iron_ore.setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron", "oreNetherIron");
    nether_iron_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack").setVanilla()
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
        .registerOreDict("oreGold", "oreNetherGold");
    nether_gold_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountGold").setVanilla()
        .setSpawnChanceConfig("spawnChanceGold")
        .setBlockCountDefault(8).setHarvestLevelDefault(goldHarvest)
        .setSpawnChanceDefault(40).setBlockId("nether_gold_ore");
    addOre(nether_gold_ore);
    BlockDimensionOre nether_coal_ore = new BlockDimensionOre(Items.COAL);
    nether_coal_ore.setSpawnType(SpawnType.SILVERFISH, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal", "oreNetherCoal");
    nether_coal_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountCoal")
        .setSpawnChanceConfig("spawnChanceCoal")
        .setBlockCountDefault(8).setHarvestLevelDefault(coalHarvest)
        .setSpawnChanceDefault(20).setBlockId("nether_coal_ore");
    addOre(nether_coal_ore);
    BlockDimensionOre nether_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    nether_lapis_ore.setSpawnType(SpawnType.SILVERFISH, 2)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis", "oreNetherLapis");
    nether_lapis_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountLapis")
        .setSpawnChanceConfig("spawnChanceLapis")
        .setBlockCountDefault(8).setHarvestLevelDefault(lapisHarvest)
        .setSpawnChanceDefault(10).setBlockId("nether_lapis_ore");
    addOre(nether_lapis_ore);
    BlockDimensionOre nether_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    nether_emerald_ore.setSpawnType(SpawnType.SILVERFISH, 5)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald", "oreNetherEmerald");
    nether_emerald_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".netherorecustom")
        .setBlockCountConfig("blockCountEmerald")
        .setSpawnChanceConfig("spawnChanceEmerald")
        .setBlockCountDefault(8).setHarvestLevelDefault(emeraldHarvest)
        .setSpawnChanceDefault(1).setBlockId("nether_emerald_ore");
    addOre(nether_emerald_ore);
    BlockDimensionOre nether_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    nether_diamond_ore.setSpawnType(SpawnType.SILVERFISH, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond", "oreNetherDiamond");
    nether_diamond_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack").setVanilla()
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
        .registerOreDict("oreRedstone", "oreEndRedstone");
    end_redstone_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountRedstone")
        .setSpawnChanceConfig("spawnChanceRedstone")
        .setBlockCountDefault(8).setHarvestLevelDefault(redstoneHarvest)
        .setSpawnChanceDefault(15).setBlockId("end_redstone_ore");
    addOre(end_redstone_ore);
    BlockDimensionOre end_coal_ore = new BlockDimensionOre(Items.COAL);
    end_coal_ore.setSpawnType(SpawnType.ENDERMITE, 1)
        .registerSmeltingOutput(Items.COAL)
        .registerOreDict("oreCoal", "oreEndCoal");
    end_coal_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountCoal")
        .setSpawnChanceConfig("spawnChanceCoal")
        .setBlockCountDefault(8).setHarvestLevelDefault(coalHarvest)
        .setSpawnChanceDefault(10).setBlockId("end_coal_ore");
    addOre(end_coal_ore);
    BlockDimensionOre end_lapis_ore = new BlockDimensionOre(Items.DYE, EnumDyeColor.BLUE.getDyeDamage(), 3);
    end_lapis_ore.setSpawnType(SpawnType.ENDERMITE, 5)
        .registerSmeltingOutput(new ItemStack(Items.DYE, 1, EnumDyeColor.BLUE.getDyeDamage()))
        .registerOreDict("oreLapis", "oreEndLapis");
    end_lapis_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountLapis")
        .setSpawnChanceConfig("spawnChanceLapis")
        .setBlockCountDefault(8).setHarvestLevelDefault(lapisHarvest)
        .setSpawnChanceDefault(12).setBlockId("end_lapis_ore");
    addOre(end_lapis_ore);
    BlockDimensionOre end_emerald_ore = new BlockDimensionOre(Items.EMERALD);
    end_emerald_ore.setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.EMERALD)
        .registerOreDict("oreEmerald", "oreEndEmerald");
    end_emerald_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountEmerald").setVanilla()
        .setSpawnChanceConfig("spawnChanceEmerald")
        .setBlockCountDefault(8).setHarvestLevelDefault(emeraldHarvest)
        .setSpawnChanceDefault(1).setBlockId("end_emerald_ore");
    addOre(end_emerald_ore);
    BlockDimensionOre end_diamond_ore = new BlockDimensionOre(Items.DIAMOND);
    end_diamond_ore.setSpawnType(SpawnType.ENDERMITE, 8)
        .registerSmeltingOutput(Items.DIAMOND)
        .registerOreDict("oreDiamond", "oreEndDiamond");
    end_diamond_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountDiamond")
        .setSpawnChanceConfig("spawnChanceDiamond")
        .setBlockCountDefault(8).setHarvestLevelDefault(diamondHarvest)
        .setSpawnChanceDefault(1).setBlockId("end_diamond_ore");
    addOre(end_diamond_ore);
    BlockDimensionOre end_gold_ore = new BlockDimensionOre(Items.GOLD_INGOT);
    end_gold_ore.setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.GOLD_INGOT)
        .registerOreDict("oreGold", "oreEndGold");
    end_gold_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
        .setConfigCategory(Const.ConfigCategory.worldGen + ".endorecustom")
        .setBlockCountConfig("blockCountGold")
        .setSpawnChanceConfig("spawnChanceGold")
        .setBlockCountDefault(8).setHarvestLevelDefault(goldHarvest)
        .setSpawnChanceDefault(4).setBlockId("end_gold_ore");
    addOre(end_gold_ore);
    BlockDimensionOre end_iron_ore = new BlockDimensionOre(Items.IRON_NUGGET, 0, 16);//iron nugget
    end_iron_ore.setSpawnType(SpawnType.ENDERMITE, 2)
        .registerSmeltingOutput(Items.IRON_INGOT)
        .registerOreDict("oreIron", "oreEndIron");
    end_iron_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone").setVanilla()
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
}
