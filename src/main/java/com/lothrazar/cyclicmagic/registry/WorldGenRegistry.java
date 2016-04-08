package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.world.gen.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class WorldGenRegistry{
	
	public static boolean oceanEnabled;
	public static boolean netherOreEnabled;
	public static boolean endOreEnabled;

	public static void syncConfig(Configuration config){
		
		String category = Const.MODCONF + "World Generation";
		
		Property prop = config.get(category, "Classic Oceans", true,"Generate clay, sand, and dirt in the ocean instead of only gravel (like the old days)");
		prop.setRequiresWorldRestart(true);
		oceanEnabled = prop.getBoolean();
		
		prop = config.get(category, "Nether Ore", true,"Generate ore in netherrack (lapis, emerald, gold, coal).  The gold gives nuggets when mined");
		prop.setRequiresMcRestart(true);
		netherOreEnabled = prop.getBoolean();
		
		prop = config.get(category, "End Ore", true,"Generate ore in the end (lapis, emerald, redstone, coal)");
		prop.setRequiresMcRestart(true);
		endOreEnabled = prop.getBoolean();
	}
	
	final static int weight = 0;
	public static void register(){

		if(oceanEnabled){
			GameRegistry.registerWorldGenerator(new WorldGenOcean(), weight);
		}
		
		if(netherOreEnabled){
			GameRegistry.registerWorldGenerator(new WorldGenNetherOre(), weight);
		}
		
		if(endOreEnabled){
			GameRegistry.registerWorldGenerator(new WorldGenEndOre(), weight);
		}
	}
}
