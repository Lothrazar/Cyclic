package com.lothrazar.cyclic.block.hoppergold;

import com.lothrazar.cyclic.block.hopper.TileSimpleHopper;
import com.lothrazar.cyclic.registry.TileRegistry;

public class TileGoldHopper extends TileSimpleHopper {

  public TileGoldHopper() {
    super(TileRegistry.HOPPERGOLD.get());
    this.flow = 64;
  }
}
