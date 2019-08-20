package com.lothrazar.cyclic.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityBase extends TileEntity {

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }
}
