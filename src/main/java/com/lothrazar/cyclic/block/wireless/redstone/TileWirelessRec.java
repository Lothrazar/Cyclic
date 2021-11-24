package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileWirelessRec extends TileBlockEntityCyclic {

  public TileWirelessRec(BlockPos pos, BlockState state) {
    super(TileRegistry.WIRELESS_RECEIVER.get(), pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
