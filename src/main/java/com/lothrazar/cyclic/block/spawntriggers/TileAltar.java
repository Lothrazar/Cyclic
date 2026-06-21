package com.lothrazar.cyclic.block.spawntriggers;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.library.core.Const;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.cache.ServerCacheHolder;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileAltar extends TileBlockEntityCyclic {

  public TileAltar(BlockPos pos, BlockState state) {
    super(TileRegistry.ALTAR_SOLICITING.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAltar tile) {
    tile.timer--;
    if (tile.timer <= 0) {
      tile.timer = Const.TICKS_PER_SEC * 30; // ping cache and restart
      pingCache(level, blockPos, blockState);
    }
  }

  private static void pingCache(Level level, BlockPos blockPos, BlockState blockState) {
    if (blockState.getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.ALTAR_SOLICITING.load(level, blockPos);
    }
    else {
      ServerCacheHolder.ALTAR_SOLICITING.unload(level, blockPos);
    }
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getBlockState().getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.ALTAR_SOLICITING.load(getLevel(), getBlockPos());
    }
  }

  @Override
  public void setRemoved() {
    ServerCacheHolder.ALTAR_SOLICITING.unload(getLevel(), getBlockPos());
    super.setRemoved();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
