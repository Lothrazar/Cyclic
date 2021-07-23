package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;

public enum PickupMode implements IStringSerializable {

  NOTHING, EVERYTHING, FILTER;

  public static final String NBT = "pickup_mode";

  @Override
  public String getString() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
