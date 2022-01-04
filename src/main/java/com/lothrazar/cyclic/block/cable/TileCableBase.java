package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileCableBase extends TileEntityBase {
  public TileCableBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public abstract void setField(int field, int value);

  public abstract int getField(int field);

  public abstract EnumConnectType getConnectionType(final Direction side);

  public abstract void updateConnection(final Direction side, final EnumConnectType connectType);
}
