package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum RefillMode implements StringRepresentable {

  NOTHING, HOTBAR;

  public static final String NBT = "refill_mode";

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
