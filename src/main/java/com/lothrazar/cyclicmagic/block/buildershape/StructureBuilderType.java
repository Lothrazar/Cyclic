package com.lothrazar.cyclicmagic.block.buildershape;
public enum StructureBuilderType {

  FACING, SQUARE, CIRCLE, SOLID, SPHERE, DIAGONAL, DOME, CUP, PYRAMID, TUNNEL;

  public static final StructureBuilderType[] SORTED = new StructureBuilderType[] { // NOW SAME AS 1.16+ 
      FACING, SQUARE, CIRCLE, SOLID, SPHERE, DIAGONAL,
      DOME, CUP, PYRAMID, TUNNEL
  };

  public static StructureBuilderType getNextType(StructureBuilderType btype) {
    int type = btype.ordinal();
    type++;
    if (type > SPHERE.ordinal()) {
      type = FACING.ordinal();
    }
    return StructureBuilderType.values()[type];
  }

  public boolean hasHeight() {
    if (this == SPHERE || this == DIAGONAL || this == DOME || this == CUP)
      return false;
    return true;
  }

  public String shortcode() {
    switch (this) {
      case CIRCLE:
        return "Ci";
      case DIAGONAL:
        return "Di";
      case FACING:
        return "Fa";
      case SOLID:
        return "So";
      case SPHERE:
        return "Sp";
      case SQUARE:
        return "Sq";
      case DOME:
        return "Do";
      case CUP:
        return "Cu";
      case PYRAMID:
        return "Py";
      case TUNNEL:
        return "Tu";
    }
    return "";
  }
}