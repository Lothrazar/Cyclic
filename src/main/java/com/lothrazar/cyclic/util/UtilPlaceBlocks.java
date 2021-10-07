package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class UtilPlaceBlocks {

  public static boolean rotateBlockValidState(Level world, BlockPos pos, Direction side) {
    BlockState clicked = world.getBlockState(pos);
    if (clicked.getBlock() == null) {
      return false;
    }
    Block clickedBlock = clicked.getBlock();
    BlockState newState = null;
    if (clickedBlock.is(BlockTags.SLABS)) {
      final String key = "type"; //top or bottom
      final String valueDupe = "double"; //actually theres 3 but dont worry about it
      //      clicked.get(property)
      for (Property<?> prop : clicked.getProperties()) {
        //yes
        if (prop.getName().equals(key)) {
          //then cycle me 
          newState = clicked.cycle(prop); // cycle
          if (newState.getValue(prop).toString().equals(valueDupe)) {
            //haha just hack and skip. turns into length 2. dont worry about it
            newState = newState.cycle(prop);
          }
        }
      }
    }
    else if (clicked.hasProperty(RotatedPillarBlock.AXIS)) {
      //axis 
      Axis current = clicked.getValue(RotatedPillarBlock.AXIS);
      switch (current) {
        case X:
          newState = clicked.setValue(RotatedPillarBlock.AXIS, Axis.Y);
        break;
        case Y:
          newState = clicked.setValue(RotatedPillarBlock.AXIS, Axis.Z);
        break;
        case Z:
          newState = clicked.setValue(RotatedPillarBlock.AXIS, Axis.X);
        break;
        default:
        break;
        //
      }
      //clicked.rot 
    }
    else {
      //default whatever
      switch (side) {
        case DOWN:
          newState = clicked.rotate(world, pos, Rotation.CLOCKWISE_180);
        break;
        case UP:
          newState = clicked.rotate(world, pos, Rotation.CLOCKWISE_180);
        break;
        case EAST:
          newState = clicked.rotate(world, pos, Rotation.CLOCKWISE_90);
        break;
        case NORTH:
          newState = clicked.rotate(world, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        case SOUTH:
          newState = clicked.rotate(world, pos, Rotation.CLOCKWISE_90);
        break;
        case WEST:
          newState = clicked.rotate(world, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        default:
        break;
      }
    }
    boolean win = false;
    if (newState != null) {
      win = world.setBlockAndUpdate(pos, newState);
    }
    if (!win) {
      ModCyclic.LOGGER.error("Could not rotate " + clickedBlock);
    }
    return win;
  }

  public static boolean placeStateSafe(Level world, Player player,
      BlockPos placePos, BlockState placeState) {
    return placeStateSafe(world, player, placePos, placeState, false);
  }

  /**
   * This will return true only if world.setBlockState(..) returns true or if the block here is already identical
   *
   * @param world
   * @param player
   * @param placePos
   * @param placeState
   * @param playSound
   * @return
   */
  public static boolean placeStateSafe(Level world, Player player, BlockPos placePos, BlockState placeState, boolean playSound) {
    if (placePos == null) {
      return false;
    }
    BlockState stateHere = null;
    //    if (player != null && PermissionRegistry.hasPermissionHere(player, placePos) == false) {
    //      return false;
    //    }
    if (world.isEmptyBlock(placePos) == false) {
      // if there is a block here, we might have to stop
      stateHere = world.getBlockState(placePos);
      if (stateHere != null) {
        //        Block blockHere = stateHere.getBlock();
        //
        //        if (blockHere.isReplaceable(world, placePos) == false) {
        //          // for example, torches, and the top half of a slab if you click
        //          // in the empty space
        //          return false;
        //        }
        // ok its a soft (isReplaceable == true) block so try to break it first try to destroy it
        // unless it is liquid, don't try to destroy liquid
        //blockHere.getMaterial(stateHere)
        if (stateHere.getMaterial().isLiquid() == false) {
          boolean dropBlock = true;
          if (world.isClientSide == false) {
            world.destroyBlock(placePos, dropBlock);
          }
        }
      }
    }
    //    if (placeState.getBlock() instanceof BlockLeaves) { //dont let them decay
    //      placeState = placeState.withProperty(BlockLeaves.DECAYABLE, false);
    //    }
    boolean success = false;
    try {
      // flags specifies what to update, '3' means notify client & neighbors
      // isRemote to make sure we are in a server thread
      if (world.isClientSide == false) {
        success = world.setBlock(placePos, placeState, 3); // returns false when placement failed
      }
    }
    catch (Exception e) {
      // PR for context https://github.com/PrinceOfAmber/Cyclic/pull/577/files
      // and  https://github.com/PrinceOfAmber/Cyclic/pull/579/files
      // show exception from above, possibly failed placement
      ModCyclic.LOGGER.error("Error attempting to place block ", e);
    }
    // play sound to area when placement is a success
    if (success && playSound) {
      //      SoundType type = UtilSound.getSoundFromBlockstate(placeState, world, placePos);
      //      if (type != null && type.getPlaceSound() != null) {
      //        UtilSound.playSoundFromServer(type.getPlaceSound(), SoundCategory.BLOCKS, placePos, world.provider.getDimension(), UtilSound.RANGE_DEFAULT);
      //      }
    }
    return success;
  }

  public static boolean destroyBlock(Level world, BlockPos pos) {
    world.removeBlockEntity(pos);
    return world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); // world.destroyBlock(pos, false);
  }

  public static boolean placeTorchSafely(Level world, BlockPos blockPos, Direction solidBlockDirection) {
    BlockItem torch = (BlockItem) Items.TORCH;
    BlockPlaceContext context = new DirectionalPlaceContext(world, blockPos, solidBlockDirection, Items.TORCH.getDefaultInstance(), solidBlockDirection);
    return torch.place(context).consumesAction();
  }
}
