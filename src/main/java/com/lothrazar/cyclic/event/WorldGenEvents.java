package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.registry.WorldGenRegistry;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {
  //


  public static final PlacedFeature FLOWER_PLAINS = PlacementUtils.register("flower_plains",
      WorldGenRegistry.FLOWER_ALL.placed(NoiseThresholdCountPlacement.of(-0.8D, 15, 4),
          RarityFilter.onAverageOnceEvery(32), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));

  //  //
  //  private static final RandomPatchConfiguration FLOWER_PURPLE_TULIP = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState()),
  //      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  //  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_PURPLE_TULIP_FEATURE = Feature.FLOWER.configured(FLOWER_PURPLE_TULIP);
  //  //
  //  private static final RandomPatchConfiguration FLOWER_ABSALON_TULIP = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState()),
  //      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  //  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_ABSALON_TULIP_FEATURE = Feature.FLOWER.configured(FLOWER_ABSALON_TULIP);
  //  //
  //  private static final RandomPatchConfiguration FLOWER_LIME_CARNATION = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState()),
  //      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  //  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_LIME_CARNATION_FEATURE = Feature.FLOWER.configured(FLOWER_LIME_CARNATION);
  //
  //  /**
  //   * Credit to pams https://github.com/MatrexsVigil/phc2crops/blob/e9790425f59c3094acef00feb2a1d0ea2b9e7e93/src/main/java/pam/pamhc2crops/worldgen/WindyGardenFeature.java
  //   * https://twitter.com/matrexsvigil/status/1317432186002427905
  //   */
  @SubscribeEvent
  public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    Biome.BiomeCategory category = event.getCategory();
    GenerationStep.Decoration step = GenerationStep.Decoration.VEGETAL_DECORATION;
    BiomeGenerationSettingsBuilder generation = event.getGeneration();
    if (WorldGenRegistry.CYAN_GENERATES.get()) {
      if (category == Biome.BiomeCategory.FOREST
          || category == Biome.BiomeCategory.PLAINS
          || category == Biome.BiomeCategory.TAIGA
          || category == Biome.BiomeCategory.EXTREME_HILLS) {
       // generation.addFeature(step, FLOWER_PLAINS);
      }
      //for testing also just do
      generation.addFeature(step, FLOWER_PLAINS);
    }
  }
  //    if (WorldGenRegistry.CYAN_GENERATES.get()) {
  //      if (category == Biome.BiomeCategory.FOREST
  //          || category == Biome.BiomeCategory.PLAINS
  //          || category == Biome.BiomeCategory.TAIGA
  //          || category == Biome.BiomeCategory.EXTREME_HILLS) {
  //        event.getGeneration().addFeature(step, FLOWER_CYAN_FEATURE);
  //      }
  //    }
  //    if (WorldGenRegistry.PURPLE_GENERATES.get()) {
  //      if (category == Biome.BiomeCategory.RIVER
  //          || category == Biome.BiomeCategory.UNDERGROUND
  //          || category == Biome.BiomeCategory.MESA
  //          || category == Biome.BiomeCategory.SAVANNA
  //          || category == Biome.BiomeCategory.FOREST
  //          || category == Biome.BiomeCategory.MUSHROOM) {
  //        event.getGeneration().addFeature(step, FLOWER_PURPLE_TULIP_FEATURE);
  //      }
  //    }
  //    if (WorldGenRegistry.LIME_GENERATES.get()) {
  //      if (category == Biome.BiomeCategory.RIVER
  //          || category == Biome.BiomeCategory.ICY
  //          || category == Biome.BiomeCategory.BEACH
  //          || category == Biome.BiomeCategory.OCEAN
  //          || category == Biome.BiomeCategory.EXTREME_HILLS
  //          || category == Biome.BiomeCategory.JUNGLE) {
  //        event.getGeneration().addFeature(step, FLOWER_LIME_CARNATION_FEATURE);
  //      }
  //    }
  //    if (WorldGenRegistry.BROWN_GENERATES.get()) {
  //      if (category == Biome.BiomeCategory.RIVER
  //          || category == Biome.BiomeCategory.PLAINS
  //          || category == Biome.BiomeCategory.TAIGA
  //          || category == Biome.BiomeCategory.UNDERGROUND
  //          || category == Biome.BiomeCategory.MESA
  //          || category == Biome.BiomeCategory.JUNGLE) {
  //        event.getGeneration().addFeature(step, FLOWER_ABSALON_TULIP_FEATURE);
  //      }
  //    }
  //  }
}
