package com.lothrazar.cyclic.block.altar;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum AltarType implements StringRepresentable {

  EMPTY, TRADER, PHANTOM; // EXPLOSION //any more would be data entity card version

  @Override
  public String getSerializedName() {
    return getName();
  }

  public String getName() {
    return name().toLowerCase(Locale.ENGLISH);
  }

  public boolean isEmpty() {
    return this == EMPTY;
  }
}
