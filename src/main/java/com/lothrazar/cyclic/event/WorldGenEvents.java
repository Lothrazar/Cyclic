package com.lothrazar.cyclic.event;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.world.CyanFlowerBlockStateProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class WorldGenEvents {

  @SubscribeEvent
  public void onBiomeLoadingEvent(BiomeLoadingEvent event) {
    ModCyclic.LOGGER.info(String.format("BIOME=%s, temperature= %s, category=%s", event.getName(), event.getClimate().temperature, event.getCategory().getName()));
    //so the getName is something like  minecraft:modified_wooded_badlands_plateau
    event.getGeneration().withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Feature.FLOWER.withConfiguration(
        (new BlockClusterFeatureConfig.Builder(CyanFlowerBlockStateProvider.PROVIDER, SimpleBlockPlacer.PLACER)).tries(64 * 8).build())
    //builder has fired
    );
  }
}
