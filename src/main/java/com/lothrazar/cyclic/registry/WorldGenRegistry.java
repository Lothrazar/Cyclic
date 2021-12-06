package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.world.WorldGenFeatures;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class WorldGenRegistry {

  public static BooleanValue GENERATES;

  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_cyan"), WorldGenFeatures.FEAT_FLOWER_CYAN);
    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_tulip"), WorldGenFeatures.FEAT_FLOWER_TULIP);
    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_lime"), WorldGenFeatures.FEAT_FLOWER_LIME);
  }
}
