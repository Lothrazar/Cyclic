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

  private static final RandomPatchConfiguration FLOWER_CYAN = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_CYAN.get().defaultBlockState()),
      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_CYAN_FEATURE = Feature.FLOWER.configured(FLOWER_CYAN);
  //
  private static final RandomPatchConfiguration FLOWER_PURPLE_TULIP = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState()),
      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_PURPLE_TULIP_FEATURE = Feature.FLOWER.configured(FLOWER_PURPLE_TULIP);
  //
  private static final RandomPatchConfiguration FLOWER_ABSALON_TULIP = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState()),
      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_ABSALON_TULIP_FEATURE = Feature.FLOWER.configured(FLOWER_ABSALON_TULIP);
  //
  private static final RandomPatchConfiguration FLOWER_LIME_CARNATION = (new RandomPatchConfiguration.GrassConfigurationBuilder(new SimpleStateProvider(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState()),
      new SimpleBlockPlacer())).tries(64).xspread(20).yspread(128).zspread(20).build();
  public static final ConfiguredFeature<RandomPatchConfiguration, ?> FLOWER_LIME_CARNATION_FEATURE = Feature.FLOWER.configured(FLOWER_LIME_CARNATION);

  /**
   * Credit to pams https://github.com/MatrexsVigil/phc2crops/blob/e9790425f59c3094acef00feb2a1d0ea2b9e7e93/src/main/java/pam/pamhc2crops/worldgen/WindyGardenFeature.java
   * https://twitter.com/matrexsvigil/status/1317432186002427905
   */
  @SubscribeEvent
  public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    if (ConfigRegistry.CYAN_GENERATES.get()) {
      if (event.getCategory() == Biome.BiomeCategory.FOREST
          || event.getCategory() == Biome.BiomeCategory.PLAINS
          || event.getCategory() == Biome.BiomeCategory.TAIGA
          || event.getCategory() == Biome.BiomeCategory.EXTREME_HILLS) {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FLOWER_CYAN_FEATURE);
      }
    }
    if (ConfigRegistry.CYAN_GENERATES.get()) {
      if (event.getCategory() == Biome.BiomeCategory.RIVER
          || event.getCategory() == Biome.BiomeCategory.UNDERGROUND
          || event.getCategory() == Biome.BiomeCategory.MESA
          || event.getCategory() == Biome.BiomeCategory.FOREST
          || event.getCategory() == Biome.BiomeCategory.MUSHROOM) {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FLOWER_PURPLE_TULIP_FEATURE);
      }
    }
    if (ConfigRegistry.CYAN_GENERATES.get()) {
      if (event.getCategory() == Biome.BiomeCategory.RIVER
          || event.getCategory() == Biome.BiomeCategory.UNDERGROUND
          || event.getCategory() == Biome.BiomeCategory.ICY
          || event.getCategory() == Biome.BiomeCategory.JUNGLE) {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FLOWER_LIME_CARNATION_FEATURE);
      }
    }
    if (ConfigRegistry.CYAN_GENERATES.get()) {
      if (event.getCategory() == Biome.BiomeCategory.RIVER
          || event.getCategory() == Biome.BiomeCategory.PLAINS
          || event.getCategory() == Biome.BiomeCategory.TAIGA
          || event.getCategory() == Biome.BiomeCategory.UNDERGROUND
          || event.getCategory() == Biome.BiomeCategory.MESA
          || event.getCategory() == Biome.BiomeCategory.JUNGLE) {
        event.getGeneration().addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, FLOWER_ABSALON_TULIP_FEATURE);
      }
    }
  }
}
