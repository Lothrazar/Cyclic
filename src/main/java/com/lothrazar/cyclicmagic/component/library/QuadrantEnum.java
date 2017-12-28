package com.lothrazar.cyclicmagic.component.library;
import net.minecraft.util.EnumFacing;

public enum  QuadrantEnum {
  TL,TR,BL,BR;
  private static final float HALF = 0.5F;
  /**
   * using (x,y) in [0,1] determine quadrant of block hit
   * 
   * @param hitHoriz
   * @param hitVertic
   * @return
   */
  public static QuadrantEnum getFor(float hitHoriz, float hitVertic) {
    if (hitHoriz > HALF && hitVertic > HALF) {
      return TL;
    }
    else if (hitHoriz <= HALF && hitVertic > HALF) {
      return TR;
    }
    else if (hitHoriz > HALF && hitVertic < HALF) {
      return BL;
    }
    else {
      return BR;
    }
  }
  /**
   * based on facing side, convert either hitX or hitZ to hitHorizontal relative to player orientation
   * 
   * @param side
   * @param hitX
   * @param hitY
   * @param hitZ
   * @return
   */
  public static QuadrantEnum getForFace(EnumFacing side, float hitX, float hitY, float hitZ) {
    QuadrantEnum segment = null;
    switch (side) {
      case EAST:
        segment = QuadrantEnum.getFor(hitZ, hitY);
      break;
      case NORTH:
        segment = QuadrantEnum.getFor(hitX, hitY);
      break;
      case SOUTH:
        segment = QuadrantEnum.getFor(1 - hitX, hitY);
      break;
      case WEST:
        segment = QuadrantEnum.getFor(1 - hitZ, hitY);
      break;
      case UP:
      case DOWN:
      break;
    }
    return segment;
  }
}
