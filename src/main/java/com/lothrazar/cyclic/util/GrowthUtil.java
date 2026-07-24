package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

public class GrowthUtil {

  public static boolean isValidGrow(Level world, BlockPos current) {
    if (world.isEmptyBlock(current)) {
      return false;
    }
    BlockState bState = world.getBlockState(current);
    if (!bState.is(BlockTags.CROPS) && !bState.is(BlockTags.SAPLINGS)) {
      return false;
    }
    if (bState.getBlock() instanceof BonemealableBlock crop) {
      if (!crop.isValidBonemealTarget(world, current, bState)) {
        return false; //cant grow, or cant bonemeal. no
      }
    }
    return true;
  }

  /**
   * grow if valid
   */
  public static boolean tryGrow(ServerLevel world, BlockPos current, double d) {
    if (!isValidGrow(world, current)) {
      return false;
    }
    if (d >= 1 || world.getRandom().nextDouble() < d) {
      BlockState bState = world.getBlockState(current);
      Block block = bState.getBlock();
      try {
        grow(world, current, bState, block);
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("deprecation")
  private static void grow(ServerLevel world, BlockPos current, BlockState bState, Block block) {
    if (bState.getBlock() instanceof BonemealableBlock crop) {
      crop.performBonemeal(world, world.getRandom(), current, bState);
    }
    else {
      // randomTick is protected in 1.21; use tickSelf if available, or bonemeal approach
      // Workaround: use BonemealableBlock interface if block implements it
      if (block instanceof BonemealableBlock bm) {
        bm.performBonemeal(world, world.getRandom(), current, bState);
      }
    }
  }
}
