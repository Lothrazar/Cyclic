package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.tileentity.ITickableTileEntity;

public class MembraneLampTile extends TileEntityBase implements ITickableTileEntity {

  public MembraneLampTile() {
    super(TileRegistry.LAMP.get());
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    int newPower = this.getRedstonePower();
    int previous = this.getBlockState().get(MembraneLamp.POWER);
    if (newPower != previous) {
      world.setBlockState(this.getPos(), getBlockState().with(MembraneLamp.POWER, newPower));
    }
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
