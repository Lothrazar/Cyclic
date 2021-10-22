package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileTerraPreta extends TileEntityBase {

  public static final int TIMER_FULL = 100;
  public static final int HEIGHT = 16;

  public TileTerraPreta(BlockPos pos, BlockState state) {
    super(TileRegistry.terra_preta, pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileTerraPreta e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileTerraPreta e) {
    e.tick();
  }

  public void tick() {
    //sprinkler to ONLY whats directly above/below
    timer--;
    if (timer > 0) {
      return;
    }
    timer = TIMER_FULL;
    for (int h = 0; h < HEIGHT; h++) {
      BlockPos current = this.getBlockPos().above(h);
      grow(level, current, 0.5);
    }
  }

  @SuppressWarnings("deprecation")
  public static boolean grow(Level world, BlockPos current, double d) {
    if (!isValidGrow(world, current)) {
      return false;
    }
    BlockState bState = world.getBlockState(current);
    Block block = bState.getBlock();
    if (world instanceof ServerLevel) {
      try {
        ServerLevel sw = (ServerLevel) world;
        if (d >= 1 || world.random.nextDouble() < d) {
          block.randomTick(bState, sw, current, world.random);
          block.randomTick(bState, sw, current, world.random);
          block.randomTick(bState, sw, current, world.random);
        }
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }

  public static boolean isValidGrow(Level world, BlockPos current) {
    BlockState bState = world.getBlockState(current);
    if (bState.getBlock() instanceof BonemealableBlock) {
      BonemealableBlock crop = ((BonemealableBlock) bState.getBlock());
      if (!crop.isValidBonemealTarget(world, current, bState, world.isClientSide) || !crop.isBonemealSuccess(world, world.random, current, bState)) {
        return false; //cant grow, or cant bonemeal. no
      }
    }
    //check tags
    return bState.is(BlockTags.CROPS) || bState.is(BlockTags.SAPLINGS);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
