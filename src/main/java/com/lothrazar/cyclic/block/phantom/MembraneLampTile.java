package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.world.level.block.entity.TickableBlockEntity;

public class MembraneLampTile extends TileEntityBase implements TickableBlockEntity {

  public MembraneLampTile() {
    super(TileRegistry.LAMP.get());
  }

  @Override
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
