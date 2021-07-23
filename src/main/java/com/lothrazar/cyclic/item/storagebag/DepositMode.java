package com.lothrazar.cyclic.item.storagebag;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;

public enum DepositMode implements IStringSerializable {

  NOTHING, DUMP, MERGE;

  public static final String NBT = "deposit_mode";

  @Override
  public String getString() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
