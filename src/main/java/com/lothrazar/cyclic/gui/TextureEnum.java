package com.lothrazar.cyclic.gui;
public enum TextureEnum {

  REDSTONE_ON, REDSTONE_NEEDED, POWER_MOVING, POWER_STOP;

  public int getX() {
    switch (this) {
      case REDSTONE_NEEDED:
        return 62;
      case REDSTONE_ON:
        return 78;
      case POWER_MOVING:
        return -2;
      case POWER_STOP:
        return 46;
    }
    return 0;
  }

  public int getY() {
    switch (this) {
      case REDSTONE_NEEDED:
        return 478;
      case REDSTONE_ON:
        return 478;
      case POWER_MOVING:
        return 110;
      case POWER_STOP:
        return 110;
    }
    return 0;
  }
}