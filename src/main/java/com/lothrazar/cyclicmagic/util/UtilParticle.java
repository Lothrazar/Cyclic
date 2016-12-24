package com.lothrazar.cyclicmagic.util;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketParticleAtPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilParticle {
  private static final int count = 12;//if you just spawn one, its basically invisible. unless its over time like potions
  private static final double RANDOM_HORIZ = 0.8;
  private static final double RANDOM_VERT = 1.5;
  public static void spawnParticle(World world, EnumParticleTypes sparkle, BlockPos pos, int c) {
    spawnParticle(world, sparkle, pos.getX(), pos.getY(), pos.getZ(), c);
  }
  public static void spawnParticleNarrow(World world, EnumParticleTypes sparkle, BlockPos pos) {
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    for (int countparticles = 0; countparticles <= count; ++countparticles) {
      world.spawnParticle(sparkle,
          x + getHorizRandom(world, RANDOM_HORIZ / 4),
          y + getVertRandom(world, RANDOM_VERT / 3),
          z + getHorizRandom(world, RANDOM_HORIZ / 4),
          0.0D, 0.0D, 0.0D);
    }
  }
  public static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z, int count) {
    if (world.isRemote) {
      // client side
      // http://www.minecraftforge.net/forum/index.php?topic=9744.0
      for (int countparticles = 0; countparticles <= count; ++countparticles) {
        world.spawnParticle(sparkle,
            x + getHorizRandom(world, RANDOM_HORIZ),
            y + getVertRandom(world, RANDOM_VERT),
            z + getHorizRandom(world, RANDOM_HORIZ),
            0.0D, 0.0D, 0.0D);
      }
    }
    else {
      spawnParticlePacket(sparkle, new BlockPos(x, y, z));
    }
  }
  private static double getVertRandom(World world, double rando) {
    return world.rand.nextDouble() * (double) rando - (double) 0.1;
  }
  private static double getHorizRandom(World world, double rando) {
    return (world.rand.nextDouble() - 0.5D) * (double) rando;
  }
  public static void spawnParticle(World world, EnumParticleTypes sparkle, double x, double y, double z) {
    spawnParticle(world, sparkle, x, y, z, count);
  }
  public static void spawnParticle(World world, EnumParticleTypes sparkle, Entity entity) {
    spawnParticle(world, sparkle, entity.getPosition());
  }
  public static void spawnParticle(World world, EnumParticleTypes sparkle, BlockPos pos) {
    if (pos == null) {
      ModCyclic.logger.warn("Particle at position null");
      return;
    }
    spawnParticle(world, sparkle, pos.getX(), pos.getY(), pos.getZ());
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
  public static void spawnParticlePacket(EnumParticleTypes particle, BlockPos position) {
    // this. fires only on server side. so send packet for client to spawn
    // particles and so on
    ModCyclic.network.sendToAll(new PacketParticleAtPosition(position, particle.getParticleID(), count));
  }
}
