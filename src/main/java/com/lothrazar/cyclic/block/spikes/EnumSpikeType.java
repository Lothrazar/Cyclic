package com.lothrazar.cyclic.block.spikes;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

public enum EnumSpikeType implements StringRepresentable {

  PLAIN, FIRE, CURSE, NONE;

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
