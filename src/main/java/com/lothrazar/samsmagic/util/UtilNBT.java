package com.lothrazar.samsmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class UtilNBT {
	
	public static String posToStringCSV(BlockPos position) 
	{ 
		return position.getX() + ","+position.getY()+","+position.getZ();
	} 
	
	
	public static void incrementPlayerIntegerNBT(EntityPlayer player, String prop, int inc)
	{
		int prev = player.getEntityData().getInteger(prop);
		prev += inc; 
		player.getEntityData().setInteger(prop, prev);
	}
}
