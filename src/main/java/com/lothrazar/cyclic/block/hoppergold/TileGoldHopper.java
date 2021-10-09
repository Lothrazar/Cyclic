package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.TileSimpleHopper;
import com.lothrazar.cyclic.block.hopperfluid.TileFluidHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileGoldHopper extends TileSimpleHopper {

  public TileGoldHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPERGOLD.get(),pos,state);
  }
  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileGoldHopper e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileGoldHopper e) {
    e.tick();
  }
  @Override
  public int getFlow() {
    return 64;
  }
}
