package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.registry.ItemRegistry;
import net.minecraft.block.ComposterBlock;

public class CompostRegistry {

  public static void setup() {
    ComposterBlock.CHANCES.put(ItemRegistry.FLOWER_CYAN.get(), 0.3F);
  }
}
