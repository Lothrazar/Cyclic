package com.lothrazar.cyclic.block.spawntriggers;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.cache.ServerCacheHolder;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TilePeace extends TileBlockEntityCyclic {

  public TilePeace(BlockPos pos, BlockState state) {
    super(TileRegistry.PEACE_CANDLE.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TilePeace tile) {
    tile.timer--;
    if (tile.timer < 0) {
      tile.timer = 20 * 30; // ping cache and restart
      pingCache(level, blockPos, blockState);
    }
  }

  private static void pingCache(Level level, BlockPos blockPos, BlockState blockState) {
    if (blockState.getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.PEACE_CANDLE.load(level, blockPos);
    }
    else {
      ServerCacheHolder.PEACE_CANDLE.unload(level, blockPos);
    }
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TilePeace e) {
    //NOOP client ticker
    //TODO: particle randozo
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getBlockState().getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.PEACE_CANDLE.load(getLevel(), getBlockPos());
    }
  }

  @Override
  public void setRemoved() {
    ServerCacheHolder.PEACE_CANDLE.unload(getLevel(), getBlockPos());
    super.setRemoved();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
