package com.lothrazar.cyclicmagic.component.cable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

/**
 * 
 * @author insomniaKitten
 *
 */
public final class CableTile extends TileEntityBaseCable {
  public CableTile() {
    super(1, 0, 0);
    this.setItemTransport();
    this.setSlotsForBoth();
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0 };
  }
}
