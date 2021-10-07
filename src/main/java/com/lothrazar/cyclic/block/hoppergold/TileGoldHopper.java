package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.TileSimpleHopper;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileGoldHopper extends TileSimpleHopper {

  public TileGoldHopper(BlockPos pos, BlockState state) {
    super(TileRegistry.HOPPERGOLD.get(),pos,state);
  }

  @Override
  public int getFlow() {
    return 64;
  }
}
