package com.lothrazar.cyclicmagic.registry;

import net.minecraftforge.fml.common.registry.GameRegistry;
import com.lothrazar.cyclicmagic.FuelHandler;


public class FuelRegistry{

	public static void register(){

		GameRegistry.registerFuelHandler(new FuelHandler());
	}
	 
}
