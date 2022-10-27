package com.lothrazar.cyclic.block.spawntriggers;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.world.cache.ServerCacheHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileAltar extends TileBlockEntityCyclic {

  public TileAltar(BlockPos pos, BlockState state) {
    super(TileRegistry.NO_SOLICITING.get(), pos, state);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileAltar tile) {
    tile.timer--;
    if (tile.timer <= 0) {
      tile.timer = 20 * 30; // ping cache and restart
      pingCache(level, blockPos, blockState);
    }
  }

  private static void pingCache(Level level, BlockPos blockPos, BlockState blockState) {
    if (blockState.getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.NO_SOLICITING.load(level, blockPos);
    }
    else {
      ServerCacheHolder.NO_SOLICITING.unload(level, blockPos);
    }
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileAltar e) {
    //NOOP client ticker
    //TODO: particle randozo
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (getBlockState().getValue(BlockCyclic.LIT)) {
      ServerCacheHolder.NO_SOLICITING.load(getLevel(), getBlockPos());
    }
  }

  @Override
  public void setRemoved() {
    ServerCacheHolder.NO_SOLICITING.unload(getLevel(), getBlockPos());
    super.setRemoved();
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
