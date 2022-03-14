package com.lothrazar.cyclic.registry;

import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class WorldGenRegistry {

  public static BooleanValue GENERATES;

  @Deprecated
  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
    //    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_cyan"), WorldGenFeatures.CYAN);
    //    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_tulip"), WorldGenFeatures.FEAT_FLOWER_TULIP);
    //    BuiltinRegistries.register(r, new ResourceLocation(ModCyclic.MODID, "flower_lime"), WorldGenFeatures.FEAT_FLOWER_LIME);
  }
}
