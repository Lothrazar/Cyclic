package com.lothrazar.cyclic.world;

import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class WorldGenPlacements {

  private static Holder<PlacedFeature> genPlaced(String id, Holder<ConfiguredFeature<SimpleBlockConfiguration, ?>> ore, List<PlacementModifier> placementModifiers) {
    return PlacementUtils.register(ModCyclic.MODID + ":" + id, ore, placementModifiers);
  }

  public static final int RARITY = 32;
  public static final int NOISEABOVE = 15;
  public static final int NOISEBELOW = 4;
  public static final double NOISELEVEL = -0.8D;
  public static final Holder<PlacedFeature> PF_FLOWER_CYAN = genPlaced("flower_cyan", WorldGenFeatures.CYAN, List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY / 2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
  public static final Holder<PlacedFeature> PF_FLOWER_TULIP = genPlaced("flower_tulip", WorldGenFeatures.TULIP, List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
  public static final Holder<PlacedFeature> PF_FLOWER_LIME = genPlaced("flower_lime", WorldGenFeatures.LIME, List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
      RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
}
