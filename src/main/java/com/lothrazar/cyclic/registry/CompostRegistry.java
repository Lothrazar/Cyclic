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
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.QUARTZ_CARROT_STEP.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.COPPER_CARROT_RADAR.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.NETHERITE_CARROT_FIRE.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(ItemRegistry.PRISMARINE_CARROT_WATER.get(), 0.3F);
  }
}
