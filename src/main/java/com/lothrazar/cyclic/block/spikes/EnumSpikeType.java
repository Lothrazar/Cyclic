package com.lothrazar.cyclic.block.spikes;

import java.util.Locale;
import net.minecraft.util.IStringSerializable;

public enum EnumSpikeType implements IStringSerializable {

  PLAIN, FIRE, CURSE, NONE;

  @Override
  public String getString() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}