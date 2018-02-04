package com.lothrazar.cyclicmagic.component.cableitem;
import com.lothrazar.cyclicmagic.component.cable.TileEntityBaseCable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileEntityItemCable extends TileEntityBaseCable {
  private static final int TICKS_TEXT_CACHED = 7;
  private int labelTimer = 0;
  private String labelText = "";
  public TileEntityItemCable() {
    super(1, 0,0);
    this.setItemTransport();
    this.setSlotsForBoth();
  }
  public String getLabelText() {
    return labelText;
  }
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    labelText = compound.getString("label");
    labelTimer = compound.getInteger("labelt");
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    compound.setString("label", labelText);
    compound.setInteger("labelt", labelTimer);
    return compound;
  }
  @Override
  public void update() {
    this.tickLabelText();
    tickDownIncomingFluidFaces();
    super.update();
  }
  /**
   * with normal item movement it moves too fast for user to read cache the current item for a few ticks so full item pipes dont show empty or flashing fast text
   */
  private void tickLabelText() {
    this.labelTimer--;
    if (this.labelTimer <= 0) {
      this.labelTimer = 0;
      this.labelText = "";
      if (this.getStackInSlot(0).isEmpty() == false) {
        this.labelText = this.getStackInSlot(0).getDisplayName();
        this.labelTimer = TICKS_TEXT_CACHED;
      }
    }
  }
  public String getIncomingStrings() {
    String in = "";
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncomingItems.get(f) > 0)
        in += f.name().toLowerCase() + " ";
    }
    return in.trim();
  }
  @Override
  public int[] getSlotsForFace(EnumFacing side) {
    return new int[] { 0 };
  }
}
