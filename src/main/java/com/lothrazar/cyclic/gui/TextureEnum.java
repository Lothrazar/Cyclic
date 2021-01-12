package com.lothrazar.cyclic.gui;
public enum TextureEnum {

  REDSTONE_ON, REDSTONE_NEEDED, POWER_MOVING, POWER_STOP, RENDER_HIDE, RENDER_SHOW, CRAFT_EMPTY, CRAFT_BALANCE, CRAFT_MATCH;

  public int getX() {
    switch (this) {
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
}
