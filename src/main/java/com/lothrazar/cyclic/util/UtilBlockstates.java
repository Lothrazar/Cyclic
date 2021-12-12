package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilBlockstates {

  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getFacingFromVector(
        (float) (entity.lastTickPosX - clickedBlock.getX()),
        (float) (entity.lastTickPosY - clickedBlock.getY()),
        (float) (entity.lastTickPosZ - clickedBlock.getZ()));
  }

  public static Direction getFacingFromEntityHorizontal(BlockPos clickedBlock, LivingEntity entity) {
    Direction d = getFacingFromEntity(clickedBlock, entity);
    //if only horizontal is allowed
    if (d == Direction.UP || d == Direction.DOWN) {
      return entity.getHorizontalFacing().getOpposite();
    }
    return d;
  }

  public static ArrayList<BlockPos> findBlocks(World world, BlockPos start, Block blockHunt, final int radiusIn) {
    ArrayList<BlockPos> found = new ArrayList<>();
    int xMin = start.getX() - radiusIn;
    int xMax = start.getX() + radiusIn;
    int yMin = start.getY() - radiusIn;
    int yMax = start.getY() + radiusIn;
    int zMin = start.getZ() - radiusIn;
    int zMax = start.getZ() + radiusIn;
    BlockPos posCurrent = null;
    for (int xLoop = xMin; xLoop <= xMax; xLoop++) {
      for (int yLoop = yMin; yLoop <= yMax; yLoop++) {
        for (int zLoop = zMin; zLoop <= zMax; zLoop++) {
          posCurrent = new BlockPos(xLoop, yLoop, zLoop);
          if (world.getBlockState(posCurrent).getBlock().equals(blockHunt)) {
            found.add(posCurrent);
          }
        }
      }
    }
    return found;
  }
}
