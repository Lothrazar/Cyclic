package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;

public class TileWirelessRec extends TileEntityBase {

  public TileWirelessRec() {
    super(TileRegistry.wireless_receiver);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
