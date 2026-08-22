package com.lothrazar.cyclic.item.builder;

public enum BuildStyle {

  NORMAL, REPLACE, OFFSET;

  public boolean isReplaceable() {
    return this == REPLACE;
  }

  //NORMAL is blue, REPLACE is yellow, OFFSET is green
  public boolean isOffset() {
    return this == OFFSET;
  }
}
