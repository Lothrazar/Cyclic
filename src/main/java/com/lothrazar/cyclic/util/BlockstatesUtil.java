package com.lothrazar.cyclic.util;

import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class BlockstatesUtil {

  public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity) {
    return Direction.getNearest(
        (float) (entity.xOld - clickedBlock.getX()),
        (float) (entity.yOld - clickedBlock.getY()),
        (float) (entity.zOld - clickedBlock.getZ()));
  }

  public static Direction getFacingFromEntityHorizontal(BlockPos clickedBlock, LivingEntity entity) {
    Direction d = getFacingFromEntity(clickedBlock, entity);
    //if only horizontal is allowed
    if (d == Direction.UP || d == Direction.DOWN) {
      return entity.getDirection().getOpposite();
    }
    return d;
  }

  public static ArrayList<BlockPos> findBlocks(Level world, BlockPos start, Block blockHunt, final int radiusIn, boolean poweredState) {
    ArrayList<BlockPos> found = new ArrayList<BlockPos>();
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
            boolean isRedstonePowered = world.hasNeighborSignal(posCurrent);
            //actual powered state matches requested powered state
            if (isRedstonePowered == poweredState) {
              found.add(posCurrent);
            }
          }
        }
      }
    }
    return found;
  }
}
