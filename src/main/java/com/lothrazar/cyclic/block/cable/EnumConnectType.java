package com.lothrazar.cyclic.block.cable;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectType implements IStringSerializable {

  NONE, CABLE, INVENTORY, BLOCKED;

  //none = nothing
  //cable = UNUSED
  //inventory normal connection, to another capability 
  //blocked = yellow block , NYI
  public boolean isHollow() {
    return this == NONE || this == BLOCKED;
  }

  public boolean isBlocked() {
    return this == BLOCKED;
  }

  public boolean isUnBlocked() {
    return this != BLOCKED;
  }
  //
  //  public EnumConnectType toggleExtractor() {
  //    if (this == CABLE)
  //      return NONE;
  //    if (this == INVENTORY || this == NONE)
  //      return CABLE;
  //    return this;//otherwise dont toggle 
  //  }

  @Override
  public String getName() {
    return name().toLowerCase();
  }
}