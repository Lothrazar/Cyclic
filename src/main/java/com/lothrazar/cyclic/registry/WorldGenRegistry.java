package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.event.WorldGenEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class WorldGenRegistry {

  /**
   * unlike blocks/items etc, no event hooks for this
   */
  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "cyan_flower"), WorldGenEvents.CYAN_FLOWER_FEATURE);
  }
}
