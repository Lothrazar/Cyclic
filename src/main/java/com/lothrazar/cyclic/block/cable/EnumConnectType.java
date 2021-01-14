package com.lothrazar.cyclic.block.cable;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectType implements IStringSerializable {

  NONE, CABLE, INVENTORY, BLOCKED;

  public boolean isHollow() {
    return this == NONE || this == BLOCKED;
  }

  public boolean isBlocked() {
    return this == BLOCKED;
  }

  public boolean isUnBlocked() {
    return this != BLOCKED;
  }

  @Override
  public String getString() {
    return name().toLowerCase();
  }
}
