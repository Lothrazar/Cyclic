package com.lothrazar.cyclic.block;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;

public enum EnumSpikeType implements IStringSerializable {

  PLAIN, FIRE, CURSE;

  @Override
  public String getString() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}