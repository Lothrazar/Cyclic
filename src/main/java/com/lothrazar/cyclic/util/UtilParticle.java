package com.lothrazar.cyclic.util;

import net.minecraft.client.Minecraft;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class UtilParticle {

  public static void spawnParticleBeam(World world, IParticleData sparkle, BlockPos start, BlockPos end, int count) {
    // thanks to http://www.minecraftforge.net/forum/index.php?topic=30567.0
    // and http://mathforum.org/library/drmath/view/65721.html
    float dX = end.getX() - start.getX();
    float dY = end.getY() - start.getY();
    float dZ = end.getZ() - start.getZ();
    float t, x, y, z;
    for (t = 0.0F; t < 1.0F; t += 0.09F) {
      x = start.getX() + (dX * t);
      y = start.getY() + (dY * t);
      z = start.getZ() + (dZ * t);
      UtilParticle.spawnParticle(world, sparkle, x, y, z, count);
    }
  }

  private static final double RANDOM_HORIZ = 0.8;
  private static final double RANDOM_VERT = 1.5;

  private static double getVertRandom(World world, double rando) {
    return world.rand.nextDouble() * rando - 0.1;
  }

  private static double getHorizRandom(World world, double rando) {
    return (world.rand.nextDouble() - 0.5D) * rando;
  }

  public static void spawnParticle(World world, IParticleData sparkle, BlockPos pos, int count) {
    if (world.isRemote) {
      spawnParticle(world, sparkle, pos.getX() + .5F, pos.getY() + .5F, pos.getZ() + .5F, count);
    }
  }

  /**
   * always check IS CLIENTSIDE before this
   * 
   * @param world
   * @param sparkle
   * @param x
   * @param y
   * @param z
   * @param count
   */
  @OnlyIn(Dist.CLIENT)
  private static void spawnParticle(World world, IParticleData sparkle, float x, float y, float z, int count) {
    for (int countparticles = 0; countparticles <= count; ++countparticles) {
      Minecraft.getInstance().particles.addParticle(sparkle,
          x + getHorizRandom(world, RANDOM_HORIZ),
          y + getVertRandom(world, RANDOM_VERT),
          z + getHorizRandom(world, RANDOM_HORIZ),
          0.0D, 0.0D, 0.0D);
    }
  }
}
