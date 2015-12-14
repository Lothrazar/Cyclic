package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.Entity;

public class UtilSound {
	public static void playSoundAt(Entity player, String sound)
	{ 
		player.worldObj.playSoundAtEntity(player, sound, 1.0F, 1.0F);
	}
}
