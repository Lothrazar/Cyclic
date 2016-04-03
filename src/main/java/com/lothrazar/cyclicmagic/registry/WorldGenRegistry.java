package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.world.gen.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class WorldGenRegistry{
	
	private static boolean oceanEnabled;
	private static boolean netherGoldEnabled;

	public static void syncConfig(Configuration config){
		
		String category = Const.MODCONF + "world_generation";
		
		config.setCategoryComment(category, "Tons of new recipes for existing blocks and items.  Bonemeal to undye wool; repeater and dispenser tweaks;  making player skulls out of the four mob heads...");

		Property prop = config.get(category, "oceanEnabled", true,"Generate clay, sand, and dirt in the ocean instead of only gravel (like the old days)");
		prop.setRequiresWorldRestart(true);
		oceanEnabled = prop.getBoolean();
		
		prop = config.get(category, "netherGoldEnabled", true,"Generate gold ore in netherrack");
		prop.setRequiresMcRestart(true);
		netherGoldEnabled = prop.getBoolean();
	}
	
	final static int weight = 0;
	public static void register(){

		if(oceanEnabled){
			GameRegistry.registerWorldGenerator(new WorldGenOcean(), weight);
		}
		
		if(netherGoldEnabled){
			GameRegistry.registerWorldGenerator(new WorldGenNetherGold(), weight);
		}
	}
}
