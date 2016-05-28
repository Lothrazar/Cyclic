package com.lothrazar.cyclicmagic.util;

import net.minecraft.world.World;

public class UtilWorld {

	public static boolean isNight(World world){
		long t = world.getWorldTime();
		
		int timeOfDay = (int) t % 24000;
		
		System.out.println(timeOfDay);
		
		return timeOfDay > 12000;
	}
}
