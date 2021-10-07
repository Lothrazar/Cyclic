package com.lothrazar.cyclic.block.terrasoil;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.entity.TickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

public class TileTerraPreta extends TileEntityBase implements TickableBlockEntity {

  public static final int TIMER_FULL = 100;
  public static final int HEIGHT = 16;

  public TileTerraPreta() {
    super(TileRegistry.terra_preta);
  }

  @Override
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
    BlockState bState = world.getBlockState(current);
    if (bState == null || bState.getBlock() == null) {
      return false;
    }
    Block block = bState.getBlock();
    if (!isValidGrow(world, current, bState)) {
      return false;
    }
    if (world instanceof ServerLevel) {
      try {
        ServerLevel sw = (ServerLevel) world;
        block.randomTick(bState, sw, current, world.random);
        if (world.random.nextDouble() < d) {
          block.randomTick(bState, sw, current, world.random);
        }
      }
      catch (Exception e) {
        return false;
      }
    }
    return true;
  }

  private static boolean isValidGrow(Level world, BlockPos current, BlockState bState) {
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
