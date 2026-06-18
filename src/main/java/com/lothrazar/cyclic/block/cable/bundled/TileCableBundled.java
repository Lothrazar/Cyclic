package com.lothrazar.cyclic.block.cable.bundled;

import com.lothrazar.cyclic.block.cable.TileCableBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TileCableBundled extends TileCableBase {
  public TileCableBundled( BlockPos pos, BlockState state) {
    super(TileRegistry.BUDNLED_PIPE.get(), pos, state);
    // this cable has all three capabilities at once
    this.setIsEnergy();
    this.setIsItem();
    this.setIsFluid();
  }

  @Override
  public void setField(int field, int value) {

  }

  @Override
  public int getField(int field) {
    return 0;
  }
}
