package com.lothrazar.cyclic.registry;

import net.minecraft.world.level.block.ComposterBlock;
public class CompostRegistry {

  public static void setup() {
    // flowers
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_CYAN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_BROWN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_LIME.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_PURPLE.get(), 0.3F);
  }
}
