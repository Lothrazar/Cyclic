package com.lothrazar.cyclic.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {

  public static void rotateBlockValidState(World worldObj, BlockPos pos, Direction side) {
    BlockState clicked = worldObj.getBlockState(pos);
    if (clicked.getBlock() == null) {
      return;
    }
    Block clickedBlock = clicked.getBlock();
    //    clickedBlock.rotate
    switch (side) {
      case DOWN:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
      break;
      case EAST:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
      break;
      case NORTH:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
      break;
      case SOUTH:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
      break;
      case UP:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
      break;
      case WEST:
        clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
      break;
      default:
      break;
    }
    clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
  }
}
