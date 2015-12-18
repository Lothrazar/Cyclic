package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class UtilSound {
	public static final String fizz = "random.fizz";
	public static final String orb = "random.orb";
	public static final String snow = "dig.snow";
	public static float volume = 1.0F;
	public static float pitch = 1.0F;

	public static void playSoundAt(Entity player, String sound) {
		player.worldObj.playSoundAtEntity(player, sound, volume,pitch);
	}

	public static void playSoundAt(World world, BlockPos pos, String sound) {
		world.playSoundEffect(pos.getX(), pos.getY(), pos.getZ(), sound, volume,pitch);
	}
}
