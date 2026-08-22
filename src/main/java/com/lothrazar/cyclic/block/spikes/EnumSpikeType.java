package com.lothrazar.cyclic.block.spikes;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum EnumSpikeType implements StringRepresentable {

  PLAIN, FIRE, CURSE, NONE;

  @Override
  public String getSerializedName() {
    return this.name().toLowerCase(Locale.ENGLISH);
  }
}
