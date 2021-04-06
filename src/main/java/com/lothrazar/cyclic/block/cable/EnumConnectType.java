package com.lothrazar.cyclic.block.cable;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectType implements IStringSerializable {

  // CABLE MEANS EXTRACT
  NONE, CABLE, INVENTORY, BLOCKED;

  public boolean isHollow() {
    return this == NONE || this == BLOCKED;
  }

  public boolean isConnected() {
    return isExtraction() || this == INVENTORY;
  }

  public boolean isExtraction() {
    return this == CABLE;
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
