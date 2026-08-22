package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum DepositMode implements StringRepresentable {

  NOTHING, DUMP, MERGE;

  public static final String NBT = "deposit_mode";

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
