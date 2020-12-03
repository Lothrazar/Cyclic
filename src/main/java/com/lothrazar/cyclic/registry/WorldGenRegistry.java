package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.event.WorldGenEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class WorldGenRegistry {

  /**
   * unlike blocks/items etc, no event hooks for this
   */
  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = WorldGenRegistries.CONFIGURED_FEATURE;
    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "cyan_flower"), WorldGenEvents.CYAN_FLOWER_FEATURE);
  }
}
