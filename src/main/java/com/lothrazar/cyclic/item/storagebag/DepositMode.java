package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum DepositMode implements StringRepresentable {

  NOTHING, DUMP, MERGE;

  public static final String NBT = "deposit_mode";

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
