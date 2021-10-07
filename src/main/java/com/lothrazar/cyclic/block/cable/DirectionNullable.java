package com.lothrazar.cyclic.block.cable;

import java.util.Locale;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum DirectionNullable implements StringRepresentable {

  DOWN, UP, NORTH, SOUTH, WEST, EAST, NONE;

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }

  public DirectionNullable toggle(Direction d) {
    if (this == NONE) {
      return values()[d.ordinal()];
    }
    return NONE;
  }

  public static DirectionNullable fromDirection(Direction d) {
    if (d == null) {
      return NONE;
    }
    return values()[d.ordinal()];
  }

  public Direction direction() {
    if (this == NONE) {
      return null;
    }
    return Direction.values()[this.ordinal()];
  }
}
