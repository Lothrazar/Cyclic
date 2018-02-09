package com.lothrazar.cyclicmagic.component.cable.bundle;

import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;

public class TileEntityCableBundle extends TileEntityCableBase {
  
  public TileEntityCableBundle() {
    super(1, 100,1000);
    this.setItemTransport();
    this.setFluidTransport();
    this.setPowerTransport();
    this.setSlotsForBoth();
  }
  
}
