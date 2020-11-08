package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.ConfigRegistry;
import com.lothrazar.cyclic.base.ItemBase;

public class PeatItem extends ItemBase {

  public static enum PeatItemType {
    BIOMASS, NORM, ENRICHED;
  }

  public final PeatItemType type;

  public PeatItem(Properties properties, PeatItemType t) {
    super(properties);
    type = t;
  }

  public int getPeatFuelValue() {
    switch (type) {
      case BIOMASS:
        return 1;
      case NORM:
        return ConfigRegistry.PEATPOWER.get();
      case ENRICHED:
        return ConfigRegistry.PEATERICHPOWER.get();
      default:
        return 0;
    }
  }
}
