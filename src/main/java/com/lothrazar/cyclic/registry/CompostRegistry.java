package com.lothrazar.cyclic.registry;

import net.minecraft.world.level.block.ComposterBlock;

public class CompostRegistry {

  public static void setup() {
    // flowers
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_CYAN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_BROWN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_LIME.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_PURPLE.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.DIAMOND_CARROT_HEALTH.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.EMERALD_CARROT_JUMP.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.LAPIS_CARROT_VARIANT.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.REDSTONE_CARROT_SPEED.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.TOXIC_CARROT.get(), 0.3F);
  }
}
