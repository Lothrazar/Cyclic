/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.util;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketParticleAtPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class UtilParticle {

  private static final int count = 12;//if you just spawn one, its basically invisible. unless its over time like potions
  private static final double RANDOM_HORIZ = 0.8;
  private static final double RANDOM_VERT = 1.5;
  /**
   * for particles from server: how far away to players see it
   */
  private static final double RANGE = 32;

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
      //      ModCyclic.logger.warn("Particle at position null");
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

  public static void spawnParticlePacket(EnumParticleTypes particle, BlockPos pos, int dimension) {
    //dont use sendToAll: lag inducing. EX: 3 players in nether, none in overworld. overworled is chunk loaded.
    //particles are a waste in this case.
    //  ModCyclic.network.sendToAll(new PacketParticleAtPosition(pos, particle.getParticleID(), count));
    spawnParticlePacket(particle, dimension, pos.getX(), pos.getY(), pos.getZ());
  }

  public static void spawnParticlePacket(EnumParticleTypes particle, int dimension, double x, double y, double z) {
    ModCyclic.network.sendToAllAround(new PacketParticleAtPosition(new BlockPos(x, y, z), particle.getParticleID(), count), new TargetPoint(dimension, x, y, z, RANGE));
  }
}
