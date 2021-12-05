package com.lothrazar.cyclic.registry;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.event.WorldGenEvents;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class WorldGenRegistry {

  public static BooleanValue CYAN_GENERATES;
  public static BooleanValue PURPLE_GENERATES;
  public static BooleanValue BROWN_GENERATES;
  public static BooleanValue LIME_GENERATES;

  /**
   * unlike blocks/items etc, no event hooks for this
   */
  public static void setup() {
    Registry<ConfiguredFeature<?, ?>> r = BuiltinRegistries.CONFIGURED_FEATURE;
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_cyan"), WorldGenEvents.FLOWER_CYAN_FEATURE);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_purple_tulip"), WorldGenEvents.FLOWER_PURPLE_TULIP_FEATURE);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_lime_carnation"), WorldGenEvents.FLOWER_LIME_CARNATION_FEATURE);
//    Registry.register(r, new ResourceLocation(ModCyclic.MODID, "flower_absalon_tulip"), WorldGenEvents.FLOWER_ABSALON_TULIP_FEATURE);
  }
}
