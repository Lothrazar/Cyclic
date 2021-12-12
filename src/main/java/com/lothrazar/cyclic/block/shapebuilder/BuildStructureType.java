package com.lothrazar.cyclic.block.shapebuilder;
public enum BuildStructureType {

  FACING, SQUARE, CIRCLE, SOLID, SPHERE, DIAGONAL, DOME, CUP, PYRAMID;

  public static BuildStructureType getNextType(BuildStructureType btype) {
    int type = btype.ordinal();
    type++;
    if (type > SPHERE.ordinal()) {
      type = FACING.ordinal();
    }
    return BuildStructureType.values()[type];
  }

  public boolean hasHeight() {
    return this != SPHERE && this != DIAGONAL && this != DOME && this != CUP;
  }

  public String shortcode() {
    switch (this) {
      case CIRCLE:
        return "CI";
      case DIAGONAL:
        return "DI";
      case FACING:
        return "FA";
      case SOLID:
        return "SO";
      case SPHERE:
        return "SP";
      case SQUARE:
        return "SQ";
      case DOME:
        return "DO";
      case CUP:
        return "CU";
      case PYRAMID:
        return "PY";
    }
    return "";
  }
}
