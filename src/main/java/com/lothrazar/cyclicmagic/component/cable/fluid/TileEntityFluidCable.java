package com.lothrazar.cyclicmagic.component.cable.fluid;
import com.lothrazar.cyclicmagic.component.cable.TileEntityCableBase;

public class TileEntityFluidCable extends TileEntityCableBase {
  public TileEntityFluidCable() {
    super(0, 100, 0);// inventory, fluid
    this.setFluidTransport();
  }
}
