package com.lothrazar.cyclic.world;

import java.util.Random;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.blockstateprovider.BlockStateProviderType;

public class CyanFlowerBlockStateProvider extends BlockStateProvider {

  public static final Codec<CyanFlowerBlockStateProvider> CODEC;
  public static final CyanFlowerBlockStateProvider PROVIDER = new CyanFlowerBlockStateProvider();
  private static final BlockState[] FLOWERS = new BlockState[] { BlockRegistry.flower_cyan.getDefaultState() };
  //how do i make one? 
  //RegistryEvent.Register does not work on BlockStateProvider
  // register is private? what do i do? constructor is also private
  //  public static final BlockStateProviderType<CyanFlowerBlockStateProvider> MY_FLOWER_PROVIDER = BlockStateProviderType.register(ModCyclic.MODID + ":cyan_flower_provider", CyanFlowerBlockStateProvider.CODEC);

  @Override
  protected BlockStateProviderType<?> getProviderType() {
    return BlockStateProviderType.FOREST_FLOWER_PROVIDER; // should be MY_FLOWER_PROVIDER
  }

  @Override
  public BlockState getBlockState(Random randomIn, BlockPos blockPosIn) {
    double d0 = Biome.INFO_NOISE.noiseAt(blockPosIn.getX() / 200.0D, blockPosIn.getZ() / 200.0D, false);
    //this log DOES fire all the time, but no blocks spawned
    ModCyclic.LOGGER.info(blockPosIn + " CyanProvider doing the thing " + d0);
    return Util.getRandomObject(FLOWERS, randomIn);
  }

  static {
    CODEC = Codec.unit(() -> {
      return PROVIDER;
    });
  }
}
