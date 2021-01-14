package com.lothrazar.cyclic.block.battery;

import net.minecraft.util.IStringSerializable;

public enum EnumBatteryPercent implements IStringSerializable {

  ZERO, TWENTY, FOURTY, SIXTY, EIGHTY, NINETY, ONEHUNDRED;

  @Override
  public String getString() {
    if (this.name().equals(ONEHUNDRED.name())) {
      return String.valueOf(100);
    }
    else if (this.name().equals(NINETY.name())) {
      return String.valueOf(90);
    }
    else if (this.name().equals(EIGHTY.name())) {
      return String.valueOf(80);
    }
    else if (this.name().equals(SIXTY.name())) {
      return String.valueOf(60);
    }
    else if (this.name().equals(FOURTY.name())) {
      return String.valueOf(40);
    }
    else if (this.name().equals(TWENTY.name())) {
      return String.valueOf(20);
    }
    return String.valueOf(0);
  }
}
