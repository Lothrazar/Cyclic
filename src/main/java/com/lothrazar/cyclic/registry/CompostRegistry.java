package com.lothrazar.cyclic.registry;

import net.minecraft.world.level.block.ComposterBlock;

public class CompostRegistry {

  public static void setup() {
    // flowers
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_CYAN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_BROWN.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_LIME.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.FLOWER_PURPLE.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_DIAMOND.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_EMERALD.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_LAPIS.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_REDSTONE.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_TOXIC.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_QUARTZ.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_COPPER.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_NETHERITE.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.CARROT_PRISMARINE.get(), 0.3F);
  }
}
