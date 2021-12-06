package com.lothrazar.cyclic.world;

import com.lothrazar.cyclic.registry.BlockRegistry;
import java.util.List;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
public class WorldGenFeatures {

  public static final ConfiguredFeature<?, ?> FEAT_FLOWER_CYAN = Feature.FLOWER.configured(new RandomPatchConfiguration(64, 4, 2, () -> {
    return Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
        BlockRegistry.FLOWER_CYAN.get().defaultBlockState(),
        List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState()),
        List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState())))).onlyWhenEmpty();
  }));
  public static final ConfiguredFeature<?, ?> FEAT_FLOWER_TULIP = Feature.FLOWER.configured(new RandomPatchConfiguration(64, 8, 6, () -> {
    return Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
        BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(),
        List.of(BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(), BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState()),
        List.of(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState())))).onlyWhenEmpty();
  }));
  public static final ConfiguredFeature<?, ?> FEAT_FLOWER_LIME = Feature.FLOWER.configured(new RandomPatchConfiguration(32, 8, 6, () -> {
    return Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
        BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(),
        List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState()),
        List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(), Blocks.ALLIUM.defaultBlockState())))).onlyWhenEmpty();
  }));
}
