package com.lothrazar.cyclic.block.cable;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum EnumConnectType implements StringRepresentable {

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
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
