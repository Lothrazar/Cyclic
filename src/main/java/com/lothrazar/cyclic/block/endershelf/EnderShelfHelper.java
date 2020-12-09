package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.BlockState;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EnderShelfHelper {

  public static final int MAX_ITERATIONS = 24;
  public static final ResourceLocation ENDER_SHELF_REGISTRY_NAME = new ResourceLocation(ModCyclic.MODID, "ender_shelf");
  public static final ResourceLocation ENDER_CONTROLLER_REGISTRY_NAME = new ResourceLocation(ModCyclic.MODID, "ender_controller");

  public static BlockPos findConnectedController(World world, BlockPos shelfPos) {
    return recursivelyFindConnectedController(world, shelfPos, new HashMap<BlockPos, Integer>(), 0);
  }

  private static BlockPos recursivelyFindConnectedController(World world, BlockPos pos, Map<BlockPos, Integer> visitedLocations, int iterations) {
    BlockState state = world.getBlockState(pos);
    if (iterations > MAX_ITERATIONS)
      return null; //We tried for too long, stop now before there's an infinite loop
    if (!(state.getBlock() instanceof BlockEnderShelf))
      return null; //We left the group of connected shelves, stop here.
    if (visitedLocations.containsKey(pos))
      return null; //We've already traveled here and didn't find anything, stop here.
    if (state.hasProperty(BlockEnderShelf.IS_CONTROLLER) && state.get(BlockEnderShelf.IS_CONTROLLER))
      return pos; //We found the Controller!
    visitedLocations.put(pos, iterations);
    BlockPos[] possibleControllers = new BlockPos[Direction.values().length];
    BlockPos returnController = null;
    int index = 0;
    iterations++;
    for (Direction direction : Direction.values()) {
      if (state.get(BlockStateProperties.HORIZONTAL_FACING) != direction)
        possibleControllers[index] = recursivelyFindConnectedController(world, pos.offset(direction), visitedLocations, iterations);
      if (possibleControllers[index] != null)
        returnController = possibleControllers[index];
    }
    return returnController;
  }

  public static Set<BlockPos> findConnectedShelves(World world, BlockPos controllerPos) {
    return recursivelyFindConnectedShelves(world, controllerPos, new HashSet<>(), new HashSet<>(), 0);
  }

  public static Set<BlockPos> recursivelyFindConnectedShelves(World world, BlockPos pos, Set<BlockPos> visitedLocations, Set<BlockPos> shelves, int iterations) {
    BlockState state = world.getBlockState(pos);
    if (visitedLocations.contains(pos))
      return shelves; //We've already traveled here and didn't find anything, stop here.
    visitedLocations.add(pos);
    if (iterations > MAX_ITERATIONS)
      return shelves; //We tried for too long, stop now before there's an infinite loop
    if (iterations > 0 && !state.getBlock().getRegistryName().equals(ENDER_SHELF_REGISTRY_NAME)) {
      return shelves; //We left the group of connected shelves, stop here.
    }
    //If we made it this far, we found a valid shelf.
    if (iterations > 0)
      shelves.add(pos); //add the shelf, but not on the first iteration because that's the controller
    iterations++;
    for (Direction direction : Direction.values()) {
      if (state.get(BlockStateProperties.HORIZONTAL_FACING) != direction)
        shelves.addAll(recursivelyFindConnectedShelves(world, pos.offset(direction), visitedLocations, shelves, iterations));
    }
    return shelves;
  }

  @Nullable
  public static EnderShelfItemHandler getShelfHandler(TileEntity te) {
    if (te != null &&
            te.getBlockState().getBlock().getRegistryName() != null &&
            te.getBlockState().getBlock().getRegistryName().equals(ENDER_SHELF_REGISTRY_NAME) &&
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() &&
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get() instanceof EnderShelfItemHandler)
      return (EnderShelfItemHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
    return null;
  }

  @Nullable
  public static EnderControllerItemHandler getControllerHandler(TileEntity te) {
    if (te != null &&
            te.getBlockState().getBlock().getRegistryName() != null &&
            te.getBlockState().getBlock().getRegistryName().equals(ENDER_CONTROLLER_REGISTRY_NAME) &&
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).isPresent() &&
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get() instanceof EnderControllerItemHandler)
      return (EnderControllerItemHandler) te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).resolve().get();
    return null;
  }

  public static boolean isController(BlockState state) {
    return state.getBlock().getRegistryName() != null && state.getBlock().getRegistryName().equals(EnderShelfHelper.ENDER_CONTROLLER_REGISTRY_NAME);
  }

  public static boolean isShelf(BlockState state) {
    return state.getBlock().getRegistryName() != null && state.getBlock().getRegistryName().equals(EnderShelfHelper.ENDER_SHELF_REGISTRY_NAME);
  }
}
