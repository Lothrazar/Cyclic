package com.lothrazar.cyclic.gui;

public enum TextureEnum {

  REDSTONE_ON, REDSTONE_NEEDED, POWER_MOVING, POWER_STOP, RENDER_HIDE, RENDER_SHOW, CRAFT_EMPTY, CRAFT_BALANCE, CRAFT_MATCH, DIR_DOWN, DIR_UPWARDS;

  public int getX() {
    switch (this) {
      case DIR_DOWN:
        return 47;
      case DIR_UPWARDS:
        return 24;
      case REDSTONE_NEEDED:
        return 62;
      case REDSTONE_ON:
        return 78;
      case POWER_MOVING:
        return 1;
      case POWER_STOP:
        return 49;
      case RENDER_HIDE:
        return 622;
      case RENDER_SHOW:
        return 622;
      case CRAFT_BALANCE:
        return 545;
      case CRAFT_EMPTY:
        return 609;
      case CRAFT_MATCH:
        return 593;
    }
    return 0;
  }

  public int getY() {
    switch (this) {
      case DIR_DOWN:
        return 176;
      case DIR_UPWARDS:
        return 176;
      case REDSTONE_NEEDED:
        return 478;
      case REDSTONE_ON:
        return 478;
      case POWER_MOVING:
        return 113;
      case POWER_STOP:
        return 113;
      case RENDER_HIDE:
        return 126;
      case RENDER_SHOW:
        return 110;
      case CRAFT_BALANCE:
        return 129;
      case CRAFT_EMPTY:
        return 129;
      case CRAFT_MATCH:
        return 129;
    }
    return 0;
  }

  public int getWidth() {
    switch (this) {
      case POWER_MOVING:
      case POWER_STOP:
      case CRAFT_EMPTY:
      case CRAFT_BALANCE:
      case CRAFT_MATCH:
        return 14;
      case DIR_DOWN:
      case DIR_UPWARDS:
        return 18;
      default:
        return 20;
    }
  }

  public int getHeight() {
    switch (this) {
      case POWER_MOVING:
      case POWER_STOP:
      case CRAFT_EMPTY:
      case CRAFT_BALANCE:
      case CRAFT_MATCH:
        return 14;
      case DIR_DOWN:
      case DIR_UPWARDS:
        return 18;
      default:
        return 20;
    }
  }

  public int getOffsetX() {
    switch (this) {
      case DIR_DOWN:
      case DIR_UPWARDS:
        return 1;
      default:
        return 0;
    }
  }

  public int getOffsetY() {
    switch (this) {
      case DIR_DOWN:
      case DIR_UPWARDS:
        return 1;
      default:
        return 0;
    }
  }
}
