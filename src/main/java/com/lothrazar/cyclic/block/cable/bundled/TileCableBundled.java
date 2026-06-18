package com.lothrazar.cyclic.block.cable.bundled;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TileCableBundled extends TileCableBase {

  public TileCableBundled(BlockPos pos, BlockState state) {
    super(TileRegistry.BUNDLED_PIPE.get(), pos, state);
    this.setIsEnergy();
    this.setIsItem();
    this.setIsFluid();
  }

  public static void serverTick(Level level, BlockPos pos, BlockState state, TileCableBundled e) {
    e.tickBundled();
  }


  private void tickBundled() {
    tickEnergy();
    tickItem();
    tickFluid();
  }


}
