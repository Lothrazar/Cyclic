package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.blockplacers.SimpleBlockPlacer;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.SimpleStateProvider;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {

  private static final RandomPatchConfiguration CYAN_FLOWER = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_CYAN.get().defaultBlockState()),
      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  public static final ConfiguredFeature<RandomPatchConfiguration, ?> CYAN_FLOWER_FEATURE = Feature.FLOWER.configured(CYAN_FLOWER);

  /**
   * Credit to pams https://github.com/MatrexsVigil/phc2crops/blob/e9790425f59c3094acef00feb2a1d0ea2b9e7e93/src/main/java/pam/pamhc2crops/worldgen/WindyGardenFeature.java
   * https://twitter.com/matrexsvigil/status/1317432186002427905
   */
  @SubscribeEvent
  public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    if (ConfigRegistry.CYAN_GENERATES.get() == false) {
      return;
    }
    //spawn depend on biome type
    if (event.getCategory() == Biome.BiomeCategory.FOREST
        || event.getCategory() == Biome.BiomeCategory.PLAINS
        || event.getCategory() == Biome.BiomeCategory.TAIGA
        || event.getCategory() == Biome.BiomeCategory.EXTREME_HILLS) {
      //      ModCyclic.LOGGER.info(String.format("cyan_flower in BIOME=%s; category=%s", event.getName(), event.getCategory().getName()));
      //spawn 
      event.getGeneration().addFeature(
          GenerationStep.Decoration.VEGETAL_DECORATION,
          CYAN_FLOWER_FEATURE);
    }
  }
}
