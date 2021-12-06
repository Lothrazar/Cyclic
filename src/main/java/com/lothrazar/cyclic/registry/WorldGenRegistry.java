package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.event.WorldGenEvents;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.NoiseThresholdProvider;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class WorldGenRegistry {

  public static BooleanValue CYAN_GENERATES;
  public static BooleanValue PURPLE_GENERATES;
  public static BooleanValue BROWN_GENERATES;
  public static BooleanValue LIME_GENERATES;
  public static final ConfiguredFeature<?, ?> FLOWER_ALL =  Feature.FLOWER.configured(new RandomPatchConfiguration(64, 6, 2, () -> {
    return Feature.SIMPLE_BLOCK.configured(new SimpleBlockConfiguration(new NoiseThresholdProvider(2345L, new NormalNoise.NoiseParameters(0, 1.0D), 0.005F, -0.8F, 0.33333334F,
        BlockRegistry.FLOWER_CYAN.get().defaultBlockState(),
        List.of(BlockRegistry.FLOWER_ABSALON_TULIP.get().defaultBlockState(), BlockRegistry.FLOWER_PURPLE_TULIP.get().defaultBlockState()),
        List.of(BlockRegistry.FLOWER_LIME_CARNATION.get().defaultBlockState())))).onlyWhenEmpty();
  }));
  /**
   * unlike blocks/items etc, no event hooks for this
   */
  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_cyan"), FLOWER_ALL);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_purple_tulip"), WorldGenEvents.FLOWER_PURPLE_TULIP_FEATURE);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_lime_carnation"), WorldGenEvents.FLOWER_LIME_CARNATION_FEATURE);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_absalon_tulip"), WorldGenEvents.FLOWER_ABSALON_TULIP_FEATURE);
  }
}
