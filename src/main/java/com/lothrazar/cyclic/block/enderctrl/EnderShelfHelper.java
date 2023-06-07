package com.lothrazar.cyclic.block.enderctrl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.lothrazar.cyclic.block.endershelf.EnderShelfItemHandler;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public class EnderShelfHelper {

  public static IntValue MAX_DIST;
  public static final int MAX_ITERATIONS = 6400; // kind of arbitrary

  public static BlockPos findConnectedController(Level world, BlockPos shelfPos) {
    return recursivelyFindConnectedController(world, shelfPos, new HashMap<BlockPos, Integer>(), 0);
  }

  private static BlockPos recursivelyFindConnectedController(Level world, BlockPos pos, Map<BlockPos, Integer> visitedLocations, int iterations) {
    BlockState state = world.getBlockState(pos);
    if (iterations > MAX_ITERATIONS) {
      return null; //We tried for too long, stop now before there's an infinite loop
    }
    if (EnderShelfHelper.isController(state)) {
      return pos;
    }
    if (!EnderShelfHelper.isShelf(state) || visitedLocations.containsKey(pos)) {
      return null; //not shelf. or shelf we have already seen
    }
    visitedLocations.put(pos, iterations);
    BlockPos[] possibleControllers = new BlockPos[Direction.values().length];
    BlockPos returnController = null;
    int index = 0;
    iterations++;
    for (Direction direction : Direction.values()) {
      if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) != direction) {
        possibleControllers[index] = recursivelyFindConnectedController(world, pos.relative(direction), visitedLocations, iterations);
      }
      if (possibleControllers[index] != null) {
        returnController = possibleControllers[index];
      }
    }
    return returnController;
  }

  public static Set<BlockPos> findConnectedShelves(Level world, BlockPos controllerPos, Direction facing) {
    return recursivelyFindConnectedShelves(controllerPos, world, controllerPos, new HashSet<>(), new HashSet<>(), 0);
  }

  public static Set<BlockPos> recursivelyFindConnectedShelves(final BlockPos controllerPos, Level world, BlockPos pos, Set<BlockPos> visitedLocations, Set<BlockPos> shelves, int iterations) {
    BlockState state = world.getBlockState(pos);
    if (visitedLocations.contains(pos)) {
      return shelves; //We've already traveled here and didn't find anything, stop here.
    }
    visitedLocations.add(pos);
    if (iterations > MAX_ITERATIONS) {
      return shelves; //We tried for too long, stop now before there's an infinite loop
    }
    //are we too far away
    if (pos.distManhattan(controllerPos) > MAX_DIST.get()) {
      return shelves;
    }
    if (iterations > 0 && !isShelf(state)) {
      return shelves; //We left the group of connected shelves, stop here.
    }
    //If we made it this far, we found a valid shelf.
    if (iterations > 0) {
      shelves.add(pos); //add the shelf, but not on the first iteration because that's the controller
    }
    iterations++;
    for (Direction direction : Direction.values()) {
      if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) != direction) {
        shelves.addAll(recursivelyFindConnectedShelves(controllerPos, world, pos.relative(direction), visitedLocations, shelves, iterations));
      }
    }
    return shelves;
  }

  public static EnderShelfItemHandler getShelfHandler(BlockEntity te) {
    if (te != null &&
        te.getBlockState().getBlock() == BlockRegistry.ENDER_SHELF.get() &&
        te.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent() &&
        te.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get() instanceof EnderShelfItemHandler) {
      return (EnderShelfItemHandler) te.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
    }
    return null;
  }

  public static EnderControllerItemHandler getControllerHandler(BlockEntity te) {
    if (te != null &&
        te.getBlockState().getBlock() == BlockRegistry.ENDER_CONTROLLER.get() &&
        te.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent() &&
        te.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get() instanceof EnderControllerItemHandler) {
      return (EnderControllerItemHandler) te.getCapability(ForgeCapabilities.ITEM_HANDLER).resolve().get();
    }
    return null;
  }

  public static boolean isController(BlockState state) {
    return state.getBlock() == BlockRegistry.ENDER_CONTROLLER.get();
  }

  public static boolean isShelf(BlockState state) {
    return state.getBlock() == BlockRegistry.ENDER_SHELF.get();
  }
}
