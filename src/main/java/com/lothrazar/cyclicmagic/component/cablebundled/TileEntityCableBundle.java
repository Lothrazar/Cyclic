package com.lothrazar.cyclicmagic.component.cablebundled;

import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class TileEntityCableBundle extends TileEntityBaseCable{
  
  public TileEntityCableBundle() {
    super(1, 100);
    this.setItemTransport();
    this.setFluidTransport();
    this.setSlotsForBoth();
  }
  
}
