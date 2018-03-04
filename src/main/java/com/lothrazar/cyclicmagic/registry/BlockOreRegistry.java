package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.component.ore.BlockDimensionOre;
import com.lothrazar.cyclicmagic.component.ore.BlockDimensionOre.SpawnType;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.module.WorldModule;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideCategory;
import com.lothrazar.cyclicmagic.registry.GuideRegistry.GuideItem;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BlockOreRegistry {
  private static void addOre(BlockDimensionOre ore) {
    WorldModule.ores.add(ore);
  }
  public static void register() {
    String cateogry;
    final int coalHarvest = 0;
    final int ironHarvest = 1;
    final int lapisHarvest = ironHarvest;
    final int emeraldHarvest = 2;
    final int diamondHarvest = emeraldHarvest;
    final int goldHarvest = emeraldHarvest;
    final int redstoneHarvest = emeraldHarvest;
    boolean DEFAULT_MODORES_ENABLED = true;//TODO: false for release
    // mod ores
    //BORON
    cateogry = Const.ConfigCategory.worldGen + ".ore.boron";
    BlockDimensionOre boron_nether_ore = new BlockDimensionOre();
    boron_nether_ore.registerOreDict("oreBoron");
    boron_nether_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(cateogry + ".nether")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("boron_nether_ore");
    addOre(boron_nether_ore);
    BlockDimensionOre boron_end_ore = new BlockDimensionOre();
    boron_end_ore.registerOreDict("oreBoron");
    boron_end_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(cateogry + ".end")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("boron_end_ore");
    addOre(boron_end_ore);
    //aqua
    cateogry = Const.ConfigCategory.worldGen + ".ore.aquamarine";
    BlockDimensionOre aquamarine_nether_ore = new BlockDimensionOre();
    aquamarine_nether_ore.registerOreDict("oreAquamarine");
    aquamarine_nether_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(cateogry + ".nether")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("aquamarine_nether_ore");
    addOre(aquamarine_nether_ore);
    BlockDimensionOre aquamarine_end_ore = new BlockDimensionOre();
    aquamarine_end_ore.registerOreDict("oreAquamarine");
    aquamarine_end_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(cateogry + ".end")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("aquamarine_end_ore");
    addOre(aquamarine_end_ore);
    //bauxite
    cateogry = Const.ConfigCategory.worldGen + ".ore.bauxite";
    BlockDimensionOre bauxite_nether_ore = new BlockDimensionOre();
    bauxite_nether_ore.registerOreDict("oreBauxite");
    bauxite_nether_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(cateogry + ".nether")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("bauxite_nether_ore");
    addOre(bauxite_nether_ore);
    BlockDimensionOre bauxite_end_ore = new BlockDimensionOre();
    bauxite_end_ore.registerOreDict("oreBauxite");
    bauxite_end_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(cateogry + ".end")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("bauxite_end_ore");
    addOre(bauxite_end_ore);
    //COPPER TIME oh yeah
    cateogry = Const.ConfigCategory.worldGen + ".ore.copper";
    BlockDimensionOre copper_nether_ore = new BlockDimensionOre();
    copper_nether_ore.registerOreDict("oreCopper");
    copper_nether_ore.config.setDimension(Const.Dimension.nether)
        .setBlockToReplace("minecraft:netherrack")
        .setConfigCategory(cateogry + ".nether")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("copper_nether_ore");
    addOre(copper_nether_ore);
    BlockDimensionOre copper_end_ore = new BlockDimensionOre();
    copper_end_ore.registerOreDict("oreCopper");
    copper_end_ore.config.setDimension(Const.Dimension.end)
        .setBlockToReplace("minecraft:end_stone")
        .setConfigCategory(cateogry + ".end")
        .setBlockCountConfig("blockCount")
        .setSpawnChanceConfig("spawnChance")
        .setRegisteredDefault(DEFAULT_MODORES_ENABLED)
        .setBlockCountDefault(8).setHarvestLevelDefault(ironHarvest)
        .setSpawnChanceDefault(5).setBlockId("copper_end_ore");
    addOre(copper_end_ore);
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
}
