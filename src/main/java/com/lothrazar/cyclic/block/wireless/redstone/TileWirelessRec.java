package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileWirelessRec extends TileEntityBase {

  public TileWirelessRec(BlockPos pos, BlockState state) {
    super(TileRegistry.wireless_receiver, pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
