package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class GrowthUtil {

  public static boolean isValidGrow(World world, BlockPos current) {
    if (world.isAirBlock(current)) {
      return false;
    }
    BlockState bState = world.getBlockState(current);
    if (!bState.isIn(BlockTags.CROPS) && !bState.isIn(BlockTags.SAPLINGS)) {
      ModCyclic.LOGGER.info("terra-grow can only grow minecraft:crops | minecraft:saplings : " + bState.getBlock());
      return false;
    }
    if (bState.getBlock() instanceof IGrowable) {
      IGrowable crop = ((IGrowable) bState.getBlock());
      if (!crop.canGrow(world, current, bState, world.isRemote)) {
        ModCyclic.LOGGER.info("terra-grow crop cannot grow right now " + bState.getBlock());
        return false; //cant grow, or cant bonemeal. no
      }
      if (!crop.canUseBonemeal(world, world.rand, current, bState)) {
        ModCyclic.LOGGER.info("terra-grow canUseBonemeal is false  " + bState.getBlock());
        return false; //cant grow, or cant bonemeal. no
      }
    }
    return true;
  }

  /**
   * grow if valid
   */
  public static boolean tryGrow(World world, BlockPos current, double d) {
    if (!isValidGrow(world, current)) {
      return false;
    }
    if (d >= 1 || world.rand.nextDouble() < d) {
      BlockState bState = world.getBlockState(current);
      Block block = bState.getBlock();
      if (world instanceof ServerWorld) {
        try {
          grow(world, current, bState, block);
        }
        catch (Exception e) {
          return false;
        }
      }
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  private static void grow(World world, BlockPos current, BlockState bState, Block block) {
    ServerWorld sw = (ServerWorld) world;
    if (bState.getBlock() instanceof IGrowable) {
      IGrowable crop = ((IGrowable) bState.getBlock());
      crop.grow(sw, world.rand, current, bState);
    }
    else { // saplings, etc
      block.randomTick(bState, sw, current, world.rand);
      block.randomTick(bState, sw, current, world.rand);
      block.randomTick(bState, sw, current, world.rand);
    }
    ModCyclic.LOGGER.info("terra-grow Successful growth" + block);
  }
}
