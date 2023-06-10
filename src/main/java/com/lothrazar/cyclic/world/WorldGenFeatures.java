package com.lothrazar.cyclic.world;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class WorldGenFeatures {

  public static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> buildFlower(String location, SimpleBlockConfiguration oc) {
    return FeatureUtils.register(ModCyclic.MODID + ":" + location, Feature.SIMPLE_BLOCK, oc);
  }

  public static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> CYAN = buildFlower("flower_cyan",
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_CYAN.get().defaultBlockState(),
          List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState()),
          List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState()))));
  public static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> TULIP = buildFlower("flower_tulip",
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(),
          List.of(BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(), BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState()),
          List.of(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState()))));
  public static Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> LIME = buildFlower("flower_lime",
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(),
          List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState()),
          List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(), Blocks.ALLIUM.defaultBlockState()))));
}
