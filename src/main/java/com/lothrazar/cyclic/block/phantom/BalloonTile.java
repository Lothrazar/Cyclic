package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;

public class BalloonTile extends TileEntityBase {

  public BalloonTile() {
    super(TileRegistry.BALLOON.get());
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
