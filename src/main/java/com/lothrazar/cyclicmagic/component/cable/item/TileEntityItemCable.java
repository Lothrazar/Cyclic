package com.lothrazar.cyclicmagic.component.cable.item;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityItemCable extends TileEntityBaseCable {

  public TileEntityItemCable() {
    super(1, 0,0);
    this.setItemTransport();
    this.setSlotsForBoth();
  }
  
 
  
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0 };
  }
}
