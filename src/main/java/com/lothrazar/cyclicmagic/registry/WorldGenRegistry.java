package com.lothrazar.cyclicmagic.registry;

import java.util.Arrays;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.world.gen.*;

import net.minecraft.block.BlockCrops;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class WorldGenRegistry {

	public static boolean	oceanEnabled;
	public static boolean	netherOreEnabled;
	public static boolean	endOreEnabled;
	public static boolean	oreSpawns = true;
	private static boolean emeraldHeight=true;
	private static boolean goldRiver;
	private static boolean oreSingletons;

	public static void syncConfig(Configuration config) {

		String category = Const.MODCONF + "World Generation";
		
		config.setCategoryComment(category, "Control any blocks that get generated in new chunks & new worlds");

		Property prop = config.get(category, "Classic Oceans", true, "Generate clay, sand, and dirt in the ocean instead of only gravel (like the old days)");
		prop.setRequiresWorldRestart(true);
		oceanEnabled = prop.getBoolean();

		prop = config.get(category, "Nether Ore", true, "Generate ore in netherrack (lapis, emerald, gold, coal).  The gold gives nuggets when mined");
		prop.setRequiresMcRestart(true);
		netherOreEnabled = prop.getBoolean();

		prop = config.get(category, "End Ore", true, "Generate ore in the end (lapis, emerald, redstone, coal)");
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
		
	}

	final static int weight = 0;

	public static void register() {

		if (oceanEnabled) {
			GameRegistry.registerWorldGenerator(new WorldGenOcean(), weight);
		}

		if (netherOreEnabled) {
			GameRegistry.registerWorldGenerator(new WorldGenNetherOre(), weight);
		}

		if (endOreEnabled) {
			GameRegistry.registerWorldGenerator(new WorldGenEndOre(), weight);
		}

		if(emeraldHeight){
			GameRegistry.registerWorldGenerator(new WorldGenEmeraldHeight(), weight);
		}

		if(goldRiver){
			GameRegistry.registerWorldGenerator(new WorldGenGoldRiver(), weight);
		}
		 
		if(oreSingletons){
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.coal_ore, 132), weight);
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.iron_ore, 68), weight);
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.gold_ore, 34), weight);
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.lapis_ore, 34), weight);
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.redstone_ore, 16), weight);
			GameRegistry.registerWorldGenerator(new WorldGenOreSingleton(Blocks.diamond_ore, 16), weight);
		}
		
		
/* * world generate growing plants - random patches of wheat & beetroomt in certain biomes
		 * maybe:
		 * wheat - plains
		 * beetroot - forest
		 * potato - taiga
		 * carrot - extreme hills*/
		
		//carrot/potato/wheat/beetroot are crops
	
		//TODO: i guess take array list
		GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops)Blocks.carrots, Arrays.asList(Biomes.extremeHills)), weight);
		
		GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops)Blocks.wheat,Arrays.asList( Biomes.plains)), weight);
		GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops)Blocks.beetroots, Arrays.asList(Biomes.forest, Biomes.birchForest)), weight);

		GameRegistry.registerWorldGenerator(new WorldGenPlantBiome((BlockCrops)Blocks.potatoes,Arrays.asList( Biomes.taiga)), weight);
	}
}
