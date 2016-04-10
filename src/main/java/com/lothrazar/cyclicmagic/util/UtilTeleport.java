package com.lothrazar.cyclicmagic.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilTeleport {

	public static void teleportWallSafe(EntityLivingBase player, World world, BlockPos coords) {

		player.setPositionAndUpdate(coords.getX(), coords.getY(), coords.getZ());

		moveEntityWallSafe(player, world);
	}

	public static void moveEntityWallSafe(EntityLivingBase entity, World world) {

		while (world.collidesWithAnyBlock(entity.getEntityBoundingBox())) {
			entity.setPositionAndUpdate(entity.posX, entity.posY + 1.0D, entity.posZ);
		}
	}
}
