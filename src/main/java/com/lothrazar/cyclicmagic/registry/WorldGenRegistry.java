package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.world.gen.*;
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
	}
}
