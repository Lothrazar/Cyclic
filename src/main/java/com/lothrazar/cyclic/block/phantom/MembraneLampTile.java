package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MembraneLampTile extends TileEntityBase  {

  public MembraneLampTile(BlockPos pos, BlockState state) {
    super(TileRegistry.LAMP.get(),pos,state);
  }

//  @Override
  public void tick() {
    int newPower = this.getRedstonePower();
    int previous = this.getBlockState().getValue(MembraneLamp.POWER);
    if (newPower != previous) {
      level.setBlockAndUpdate(this.getBlockPos(), getBlockState().setValue(MembraneLamp.POWER, newPower));
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
