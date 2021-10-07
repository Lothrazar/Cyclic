package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum PickupMode implements StringRepresentable {

  NOTHING, EVERYTHING, FILTER;

  public static final String NBT = "pickup_mode";

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
