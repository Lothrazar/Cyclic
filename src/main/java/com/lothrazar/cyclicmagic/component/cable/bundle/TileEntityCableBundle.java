package com.lothrazar.cyclicmagic.component.cable.bundle;

import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class TileEntityCableBundle extends TileEntityBaseCable {
  
  public TileEntityCableBundle() {
    super(1, 100,1000);
    this.setItemTransport();
    this.setFluidTransport();
    this.setPowerTransport();
    this.setSlotsForBoth();
  }
  
}
