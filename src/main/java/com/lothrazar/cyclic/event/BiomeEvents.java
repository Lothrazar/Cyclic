package com.lothrazar.cyclic.event;

import net.minecraft.world.level.levelgen.GenerationStep;

/**
 * https://github.com/MatrexsVigil/phc2crops/blob/e9790425f59c3094acef00feb2a1d0ea2b9e7e93/src/main/java/pam/pamhc2crops/worldgen/WindyGardenFeature.java
 * https://twitter.com/matrexsvigil/status/1317432186002427905
 */
public class BiomeEvents {

  private GenerationStep.Decoration step = GenerationStep.Decoration.VEGETAL_DECORATION;
  //  @SubscribeEvent
  //  public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
  //    if (!ConfigRegistry.GENERATE_FLOWERS.get()) {
  //      return;
  //    }
  //    BiomeGenerationSettingsBuilder generation = event.getGeneration();
  //    Biome.BiomeCategory category = event.getCategory();
  //    if (category == Biome.BiomeCategory.FOREST || category == Biome.BiomeCategory.SWAMP) {
  //      generation.addFeature(step, WorldGenPlacements.PF_FLOWER_CYAN);
  //    }
  //    if (category == Biome.BiomeCategory.SAVANNA || category == Biome.BiomeCategory.MESA) {
  //      generation.addFeature(step, WorldGenPlacements.PF_FLOWER_TULIP); // brown absalon and purple
  //    }
  //    if (category == Biome.BiomeCategory.ICY
  //        || category == Biome.BiomeCategory.PLAINS
  //        || category == Biome.BiomeCategory.OCEAN
  //        || category == Biome.BiomeCategory.UNDERGROUND
  //        || category == Biome.BiomeCategory.JUNGLE) {
  //      generation.addFeature(step, WorldGenPlacements.PF_FLOWER_LIME);
  //    }
  //  }
}
