package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {

  private static final BlockClusterFeatureConfig CYAN_FLOWER = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.FLOWER_CYAN.get().getDefaultState()),
      new SimpleBlockPlacer())).tries(64).xSpread(20).ySpread(128).zSpread(20).build();
  public static final ConfiguredFeature<BlockClusterFeatureConfig, ?> CYAN_FLOWER_FEATURE = Feature.FLOWER.withConfiguration(CYAN_FLOWER);

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
    if (event.getCategory() == Biome.Category.FOREST
        || event.getCategory() == Biome.Category.PLAINS
        || event.getCategory() == Biome.Category.TAIGA
        || event.getCategory() == Biome.Category.EXTREME_HILLS) {
      event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, CYAN_FLOWER_FEATURE);
      ModCyclic.LOGGER.info(String.format("CYAN_GENERATES in BIOME=%s; category=%s", event.getName(), event.getCategory().getName()));
    }
  }
}
