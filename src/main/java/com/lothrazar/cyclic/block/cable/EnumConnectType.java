package com.lothrazar.cyclic.block.cable;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectType implements IStringSerializable {

  NONE, CABLE, INVENTORY, BLOCKED;

  public boolean isHollow() {
    return this == NONE || this == BLOCKED;
  }

  @Override
  public String getName() {
    return name().toLowerCase();
  }
}