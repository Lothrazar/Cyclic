package com.lothrazar.cyclicmagic.gui.password;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityPassword;
import com.lothrazar.cyclicmagic.gui.ContainerBase;

public class ContainerPassword extends ContainerBase {
  TileEntityPassword tile;
  public ContainerPassword(TileEntityPassword te) {
    super();
    tile = te;
  }
}
