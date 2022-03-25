package com.lothrazar.cyclic.block.shapebuilder;
public enum BuildStructureType {

  FACING, SQUARE, CIRCLE, SOLID, SPHERE, DIAGONAL, DOME, CUP, PYRAMID, TUNNEL;

  public static BuildStructureType getNextType(BuildStructureType btype) {
    int type = btype.ordinal();
    type++;
    if (type > SPHERE.ordinal()) {
      type = FACING.ordinal();
    }
    return BuildStructureType.values()[type];
  }

  public boolean hasHeight() {
    if (this == SPHERE || this == DIAGONAL || this == DOME || this == CUP) {
      return false;
    }
    return true;
  }
}
