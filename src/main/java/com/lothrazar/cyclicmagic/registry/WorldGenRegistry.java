package com.lothrazar.cyclicmagic.registry;

import com.lothrazar.cyclicmagic.world.gen.*;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class WorldGenRegistry{

	public static void syncConfig(Configuration config){

		
	}
	
	final static int weight = 0;
	public static void register(){

		GameRegistry.registerWorldGenerator(new WorldGenOcean(), weight);
		GameRegistry.registerWorldGenerator(new WorldGenNetherGold(), weight);
	}
}
