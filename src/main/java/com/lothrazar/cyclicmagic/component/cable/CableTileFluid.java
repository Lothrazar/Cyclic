package com.lothrazar.cyclicmagic.component.cable;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class CableTileFluid extends TileEntityBaseCable {
  public CableTileFluid() {
    super(0, 100,0);// inventory, fluid
    this.setFluidTransport();
  }
}
