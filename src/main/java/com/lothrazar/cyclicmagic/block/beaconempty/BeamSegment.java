package com.lothrazar.cyclicmagic.block.beaconempty;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BeamSegment {

  /** RGB (0 to 1.0) colors of this beam segment */
  private final float[] colors;
  private int height;

  public BeamSegment(float[] colorsIn) {
    this.colors = colorsIn;
    this.height = 1;
  }

  public void incrementHeight() {
    ++this.height;
  }

  /**
   * Returns RGB (0 to 1.0) colors of this beam segment
   */
  public float[] getColors() {
    return this.colors;
  }

  @SideOnly(Side.CLIENT)
  public int getHeight() {
    return this.height;
  }

}