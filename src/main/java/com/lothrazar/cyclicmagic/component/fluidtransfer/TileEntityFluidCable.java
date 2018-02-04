package com.lothrazar.cyclicmagic.component.fluidtransfer;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;

public class TileEntityFluidCable extends TileEntityBaseCable {
  public TileEntityFluidCable() {
    super(0, 100);// inventory, fluid
    this.setFluidTransport();
  }
}
