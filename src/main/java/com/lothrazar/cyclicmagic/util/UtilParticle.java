package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.net.PacketParticleAtPosition;

import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilParticle {

	public static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z, int count) {

		if (world.isRemote) {
			// client side

			// http://www.minecraftforge.net/forum/index.php?topic=9744.0
			for (int countparticles = 0; countparticles <= count; ++countparticles) {
				world.spawnParticle(sparkle, x + (world.rand.nextDouble() - 0.5D) * (double) 0.8, y + world.rand.nextDouble() * (double) 1.5 - (double) 0.1, z + (world.rand.nextDouble() - 0.5D) * (double) 0.8, 0.0D, 0.0D, 0.0D);
			}
		}
		else {
			spawnParticlePacket(sparkle, new BlockPos(x, y, z), count);
		}
	}

	public static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z) {

		spawnParticle(world, sparkle, x, y, z, 12);// default spam count
	}

	public static void spawnParticle(World world, EnumParticleTypes sparkle, Entity entity) {

		spawnParticle(world, sparkle, entity.getPosition());
	}

	public static void spawnParticle(World world, EnumParticleTypes sparkle, BlockPos pos) {

		if (pos != null) {
			spawnParticle(world, sparkle, pos.getX(), pos.getY(), pos.getZ());
		}
	}

	public static void spawnParticleBeam(World world, EnumParticleTypes sparkle, BlockPos start, BlockPos end, int count) {

		// thanks to http://www.minecraftforge.net/forum/index.php?topic=30567.0
		// and http://mathforum.org/library/drmath/view/65721.html
		float dX = end.getX() - start.getX();
		float dY = end.getY() - start.getY();
		float dZ = end.getZ() - start.getZ();
		float t, x, y, z;
		for (t = 0.0F; t < 1.0F; t += 0.05F) {
			x = start.getX() + (dX * t);
			y = start.getY() + (dY * t);
			z = start.getZ() + (dZ * t);
			UtilParticle.spawnParticle(world, sparkle, x, y, z, count);
		}
	}

	private static void spawnParticlePacket(EnumParticleTypes particle, BlockPos position, int count) {

		// this. fires only on server side. so send packet for client to spawn
		// particles and so on
		ModMain.network.sendToAll(new PacketParticleAtPosition(position, particle.getParticleID(), count));
	}
}
