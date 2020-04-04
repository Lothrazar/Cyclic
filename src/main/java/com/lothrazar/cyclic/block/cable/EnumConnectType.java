package com.lothrazar.cyclic.block.cable;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectType implements IStringSerializable {

  NONE, CABLE, INVENTORY, BLOCKED;

  //none = nothing
  //cable = will be for extractor
  //inventory normal connection, to another capability 
  //blocked = yellow block , NYI
  public boolean isHollow() {
    return this == NONE || this == BLOCKED;
  }

  @Override
  public String getName() {
    return name().toLowerCase();
  }
}