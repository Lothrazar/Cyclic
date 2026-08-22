package com.lothrazar.cyclic.block.conveyor;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum ConveyorType implements StringRepresentable {

  STRAIGHT, UP, DOWN, CORNER_LEFT, CORNER_RIGHT;

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }

  public boolean isCorner() {
    return this == CORNER_LEFT || this == CORNER_RIGHT;
  }

  public boolean isVertical() {
    return this == UP || this == DOWN;
  }
}
