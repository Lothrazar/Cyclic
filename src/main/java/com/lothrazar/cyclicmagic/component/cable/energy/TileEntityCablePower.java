package com.lothrazar.cyclicmagic.component.cable.energy;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;

public class TileEntityCablePower extends TileEntityCableBase {
  public TileEntityCablePower() {
    super(0, 0, 1);
    this.setPowerTransport();
  }
}
