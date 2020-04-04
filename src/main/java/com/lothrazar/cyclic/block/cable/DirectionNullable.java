package com.lothrazar.cyclic.block.cable;

import javax.annotation.Nullable;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;

public enum DirectionNullable implements IStringSerializable {

  //same order as 
  // net.minecraft.util.Direction
  DOWN, UP, NORTH, SOUTH, WEST, EAST, NONE;

  @Override
  public String getName() {
    return this.name().toLowerCase();
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

  @Nullable
  public Direction direction() {
    if (this == NONE) {
      return null;
    }
    return Direction.values()[this.ordinal()];
  }
}
