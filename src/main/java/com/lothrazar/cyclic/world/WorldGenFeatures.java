package com.lothrazar.cyclic.world;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenFeatures {

  public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES = DeferredRegister.create(Registries.CONFIGURED_FEATURE, ModCyclic.MODID);

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static ConfiguredFeature<SimpleBlockConfiguration, ?> buildFlower(SimpleBlockConfiguration oc) {
    return new ConfiguredFeature(Feature.SIMPLE_BLOCK, oc);
  }

  public static RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, ?>> CYAN = CONFIGURED_FEATURES.register("flower_cyan", () -> buildFlower(
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_CYAN.get().defaultBlockState(),
          List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState()),
          List.of(Blocks.POPPY.defaultBlockState(), BlockRegistry.FLOWER_CYAN.get().defaultBlockState())))));
  //
  public static RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, ?>> TULIP = CONFIGURED_FEATURES.register("flower_tulip", () -> buildFlower(
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(),
          List.of(BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState(), BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState(), Blocks.PINK_TULIP.defaultBlockState()),
          List.of(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState())))));
  //
  public static RegistryObject<ConfiguredFeature<SimpleBlockConfiguration, ?>> LIME = CONFIGURED_FEATURES.register("flower_lime", () -> buildFlower(
      new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
          BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(),
          List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState()),
          List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState(), Blocks.ALLIUM.defaultBlockState())))));
}
