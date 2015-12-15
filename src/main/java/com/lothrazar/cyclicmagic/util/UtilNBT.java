package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class UtilNBT {

	public static String posToStringCSV(BlockPos position) {
		return position.getX() + "," + position.getY() + "," + position.getZ();
	}
	public static BlockPos stringCSVToBlockPos(String csv) {
		String[] spl = csv.split(",");
		// on server i got java.lang.ClassCastException: java.lang.String cannot
		// be cast to java.lang.Integer
		// ?? is it from this?
		BlockPos p = null;
		try {
			p = new BlockPos(Integer.parseInt(spl[0]), Integer.parseInt(spl[1]), Integer.parseInt(spl[2]));
		} catch (java.lang.ClassCastException e) {
			System.out.println(e.getMessage());
		}
		return p;
	}

	public static void incrementPlayerIntegerNBT(EntityPlayer player, String prop, int inc) {
		int prev = player.getEntityData().getInteger(prop);
		prev += inc;
		player.getEntityData().setInteger(prop, prev);
	}
}
