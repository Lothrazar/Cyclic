package com.lothrazar.cyclic.base;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityBase extends TileEntity {

  public static final int FUEL_WEAK = 256;
  public static final int MENERGY = 64 * 1000;

  public TileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public boolean isPowered() {
    return this.getWorld().isBlockPowered(this.getPos());
  }
}
