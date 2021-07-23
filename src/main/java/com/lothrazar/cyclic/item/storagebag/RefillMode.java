package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;

public enum RefillMode implements IStringSerializable {

  NOTHING, HOTBAR;

  public static final String NBT = "refill_mode";

  @Override
  public String getString() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
