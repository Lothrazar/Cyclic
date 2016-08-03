package com.lothrazar.cyclicmagic.util;
import java.util.Random;
import net.minecraft.dispenser.IPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class UtilWorld {
  public static boolean isNight(World world) {
    long t = world.getWorldTime();
    int timeOfDay = (int) t % 24000;
    return timeOfDay > 12000;
  }

  public static BlockPos convertIposToBlockpos(IPosition here) {
    return new BlockPos(here.getX(),here.getY(),here.getZ());
  }
  public static BlockPos getRandomPos(Random rand,BlockPos here, int hRadius) {
    int x = here.getX();
    int z = here.getZ();
    // search in a square
    int xMin = x - hRadius;
    int xMax = x + hRadius;
    int zMin = z - hRadius;
    int zMax = z + hRadius;

    int posX = MathHelper.getRandomIntegerInRange(rand, xMin, xMax);
    int posZ = MathHelper.getRandomIntegerInRange(rand, zMin, zMax);
    
    return new BlockPos(posX,here.getY(),posZ);
  }
}
