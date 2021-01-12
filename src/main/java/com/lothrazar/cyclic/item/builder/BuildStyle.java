package com.lothrazar.cyclic.item.builder;

import java.awt.Color;

public enum BuildStyle {

  NORMAL, REPLACE, OFFSET;

  public boolean isReplaceable() {
    return this == REPLACE;
  }

  //NORMAL is blue, REPLACE is yellow, OFFSET is green
  public boolean isOffset() {
    return this == OFFSET;
  }

  public Color getColour() {
    switch (this) {
      case NORMAL:
        return Color.BLUE;
      case OFFSET:
        return Color.GREEN;
      case REPLACE:
        return Color.YELLOW;
    }
    return null;
  }
}
