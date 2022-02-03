package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;

public class TileTerraPreta extends TileBlockEntityCyclic {

  public static IntValue TIMER_FULL;
  public static final int HEIGHT = 16;
  public static DoubleValue CHANCE;

  public TileTerraPreta(BlockPos pos, BlockState state) {
    super(TileRegistry.TERRA_PRETA.get(), pos, state);
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
    timer = TIMER_FULL.get();
    for (int h = 0; h < HEIGHT; h++) {
      BlockPos current = this.getBlockPos().above(h);
      grow(level, current, CHANCE.get());
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
