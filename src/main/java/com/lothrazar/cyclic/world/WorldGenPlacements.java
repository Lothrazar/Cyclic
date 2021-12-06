package com.lothrazar.cyclic.world;

import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
public class WorldGenPlacements {

  public static final int RARITY = 32;
  public static final int NOISEABOVE = 15;
  public static final int NOISEBELOW = 4;
  public static final double NOISELEVEL = -0.8D;
  public static final PlacedFeature PF_FLOWER_CYAN = PlacementUtils.register("flower_cyan", WorldGenFeatures.FEAT_FLOWER_CYAN.placed(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY / 2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
  public static final PlacedFeature PF_FLOWER_TULIP = PlacementUtils.register("flower_tulip", WorldGenFeatures.FEAT_FLOWER_TULIP.placed(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
  public static final PlacedFeature PF_FLOWER_LIME = PlacementUtils.register("flower_lime", WorldGenFeatures.FEAT_FLOWER_LIME.placed(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
}
