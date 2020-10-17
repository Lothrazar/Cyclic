package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {

  public static BlockClusterFeatureConfig CYAN_FLOWER = null;

  @SubscribeEvent
  public static void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    //  if (CYAN_FLOWER == null) {
    CYAN_FLOWER = (new BlockClusterFeatureConfig.Builder(new SimpleBlockStateProvider(BlockRegistry.flower_cyan.getDefaultState()),
        new SimpleBlockPlacer())).tries(64 * 64).xSpread(20).ySpread(128).zSpread(30).build();
    // }
    //
    ModCyclic.LOGGER.info(String.format("BIOME=%s, temperature= %s, category=%s", event.getName(), event.getClimate().temperature, event.getCategory().getName()));
    //so the getName is something like  minecraft:modified_wooded_badlands_plateau
    //    Biome.Category.FOREST
    event.getGeneration().withFeature(
        GenerationStage.Decoration.VEGETAL_DECORATION,
        Feature.FLOWER.withConfiguration(CYAN_FLOWER));
  }
}
