package com.lothrazar.cyclic.item.builder;
public enum BuildStyle {

  NORMAL, REPLACE, OFFSET;

  public boolean isReplaceable() {
    return this == REPLACE;
  }

  public boolean isOffset() {
    return this == OFFSET;
  }
}