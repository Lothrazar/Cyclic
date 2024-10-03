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
    if (world.isEmptyBlock(current)) { // isAir
      return false;
    }
    BlockState bState = world.getBlockState(current);
    if (!bState.is(BlockTags.CROPS) && !bState.is(BlockTags.SAPLINGS)) {
      //   ModCyclic.LOGGER.info("terra-grow can only grow minecraft:crops | minecraft:saplings : " + bState.getBlock());
      return false;
    }
    if (bState.getBlock() instanceof BonemealableBlock crop) {
      //      BonemealableBlock crop = ((BonemealableBlock) bState.getBlock());
      //      if (!crop.isValidBonemealTarget(world, current, bState, world.isClientSide)) { // canCrow
      //        ModCyclic.LOGGER.info("terra-grow crop cannot grow right now " + bState.getBlock());
      //        return false; //cant grow, or cant bonemeal. no
      //      }
      if (!crop.isValidBonemealTarget(world, current, bState, world.isClientSide)) {//canUseBonemeal // canGrow
        ModCyclic.LOGGER.info("terra-grow canUseBonemeal is false  " + bState.getBlock());
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
    if (d >= 1 || world.random.nextDouble() < d) {
      BlockState bState = world.getBlockState(current);
      Block block = bState.getBlock();
      if (world instanceof ServerLevel) {
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
  private static void grow(ServerLevel world, BlockPos current, BlockState bState, Block block) {
    if (bState.getBlock() instanceof BonemealableBlock crop) {
      crop.performBonemeal(world, world.random, current, bState); // .grow()
    }
    else { // saplings, etc
      block.randomTick(bState, world, current, world.random);
      block.randomTick(bState, world, current, world.random);
      block.randomTick(bState, world, current, world.random);
    }
    ModCyclic.LOGGER.info("terra-grow Successful growth: " + block);
  }
}
