package com.lothrazar.cyclicmagic.util;
import net.minecraft.dispenser.IPosition;
import net.minecraft.util.math.BlockPos;
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
}
