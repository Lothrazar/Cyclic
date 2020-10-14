package com.lothrazar.cyclic.base;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntityType;

public abstract class SidedTileEntityBase extends TileEntityBase implements ISidedInventory {

  public SidedTileEntityBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }
}
