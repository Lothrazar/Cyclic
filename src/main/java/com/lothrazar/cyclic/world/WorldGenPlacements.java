package com.lothrazar.cyclic.world;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.NoiseThresholdCountPlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WorldGenPlacements {

  public static final DeferredRegister<PlacedFeature> PLACED_FEATURES = DeferredRegister.create(Registries.PLACED_FEATURE, ModCyclic.MODID);

  private static PlacedFeature genPlaced(@NotNull ConfiguredFeature<SimpleBlockConfiguration, ?> configuredFeature, List<PlacementModifier> placementModifiers) {
    return new PlacedFeature(Holder.direct(configuredFeature), placementModifiers);
  }

  public static final int RARITY = 32;
  public static final int NOISEABOVE = 15;
  public static final int NOISEBELOW = 4;
  public static final double NOISELEVEL = -0.8D;
  public static final RegistryObject<PlacedFeature> PF_FLOWER_CYAN = PLACED_FEATURES.register("flower_cyan", () -> genPlaced(WorldGenFeatures.CYAN.get(),
      List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW), RarityFilter.onAverageOnceEvery(RARITY / 2), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
  //
  public static final RegistryObject<PlacedFeature> PF_FLOWER_TULIP = PLACED_FEATURES.register("flower_tulip", () -> genPlaced(WorldGenFeatures.TULIP.get(),
      List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW), RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
  //
  public static final RegistryObject<PlacedFeature> PF_FLOWER_LIME = PLACED_FEATURES.register("flower_lime", () -> genPlaced(WorldGenFeatures.LIME.get(),
      List.of(NoiseThresholdCountPlacement.of(NOISELEVEL, NOISEABOVE, NOISEBELOW),
          RarityFilter.onAverageOnceEvery(RARITY), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome())));
}
