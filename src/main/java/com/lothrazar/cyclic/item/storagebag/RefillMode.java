package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum RefillMode implements StringRepresentable {

  NOTHING, HOTBAR;

  public static final String NBT = "refill_mode";

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
