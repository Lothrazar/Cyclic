package com.lothrazar.cyclic.block.cable;

import com.lothrazar.cyclic.base.TileEntityBase;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

public abstract class TileCableBase extends TileEntityBase {

  protected final Map<Direction, EnumConnectType> connectTypeMap = new HashMap<>();

  public TileCableBase(TileEntityType<?> tileEntityTypeIn) {
    super(tileEntityTypeIn);
  }

  public abstract void setField(int field, int value);

  public abstract int getField(int field);

  public EnumConnectType getConnectionType(final Direction side) {
    return connectTypeMap.computeIfAbsent(side, k -> getBlockState().get(CableBase.FACING_TO_PROPERTY_MAP.get(k)));
  }

  public void updateConnection(final Direction side, final EnumConnectType connectType) {
    connectTypeMap.put(side, connectType);
  }
}
