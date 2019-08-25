package com.lothrazar.cyclic.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IProperty;
import net.minecraft.tags.BlockTags;
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
    BlockState newState = null;
    if (clickedBlock.isIn(BlockTags.SLABS)) {
      final String key = "type";//top or bottom
      final String valueDupe = "double";//actually theres 3 but dont worry about it
      for (IProperty prop : clicked.getProperties()) {
        //yes
        if (prop.getName().equals(key)) {
          //then cycle me
          newState = clicked.cycle(prop);
          if (newState.get(prop).toString().equals(valueDupe)) {
            //haha just hack and skip. turns into length 2. dont worry about it
            //      ModCyclic.LOGGER.info("yes is double");
            newState = newState.cycle(prop);
          }
        }
      }
    }
    else {
      //default whatever
      switch (side) {
        case DOWN:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_180);
        break;
        case EAST:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
        break;
        case NORTH:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        case SOUTH:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
        break;
        case UP:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_180);
        break;
        case WEST:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        default:
        break;
      }
    }
    if (newState != null) {
      worldObj.setBlockState(pos, newState);
    }
  }
}
