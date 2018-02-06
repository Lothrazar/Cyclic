package com.lothrazar.cyclicmagic.component.cable.energy;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class TileEntityCablePower extends TileEntityBaseCable {
  public TileEntityCablePower() {
    super(0, 0, 1);
    this.setPowerTransport();
  }
}
