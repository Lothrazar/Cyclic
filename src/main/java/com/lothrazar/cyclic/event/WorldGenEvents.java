package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {

  public static BlockClusterFeatureConfig CYAN_FLOWER = null;

  @SubscribeEvent
  public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    if (CYAN_FLOWER == null) {
      CYAN_FLOWER = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.flower_cyan.getDefaultState()),
          new SimpleBlockPlacer())).tries(64).xSpread(20).ySpread(128).zSpread(20).build();
    }
    //
    if (event.getCategory() == Biome.Category.FOREST
        || event.getCategory() == Biome.Category.PLAINS
        || event.getCategory() == Biome.Category.TAIGA
        || event.getCategory() == Biome.Category.EXTREME_HILLS) {
      ModCyclic.LOGGER.info(String.format("cyan_flower in BIOME=%s; category=%s", event.getName(), event.getCategory().getName()));
      //spawn 
      event.getGeneration().withFeature(
          GenerationStage.Decoration.VEGETAL_DECORATION,
          Feature.FLOWER.withConfiguration(CYAN_FLOWER));
    }
  }
}
