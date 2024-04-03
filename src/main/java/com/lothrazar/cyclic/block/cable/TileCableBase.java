package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.base.TileEntityBase;
import net.minecraft.state.EnumProperty;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileCableBase extends TileEntityBase {

  public TileCableBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  @Override
  public abstract void setField(int field, int value);

  @Override
  public abstract int getField(int field);

  public EnumConnectType getConnectionType(final Direction side) {
    EnumProperty<EnumConnectType> property = CableBase.FACING_TO_PROPERTY_MAP.get(side);
    return getBlockState().get(property);
  }

  public void updateConnection(final Direction side, final EnumConnectType connectType) {}
}
