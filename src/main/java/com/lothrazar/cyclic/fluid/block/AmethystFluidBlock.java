package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.cyclic.config.ConfigRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class AmethystFluidBlock extends LiquidBlock {

  public AmethystFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier.get(), props);
  }

  @Override
  public boolean isRandomlyTicking(BlockState state) {
    return state.getFluidState().isSource();
  }

  @Override
  public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    int radius = ConfigRegistry.AMETHYST_FLUID_GROWTH_RADIUS.get();
    BlockPos.betweenClosed(pos.offset(-radius, -radius, -radius), pos.offset(radius, radius, radius))
        .forEach(nearby -> {
          BlockState nearbyState = level.getBlockState(nearby);
          if (nearbyState.getBlock() instanceof BuddingAmethystBlock buddingBlock) {
            buddingBlock.randomTick(nearbyState, level, nearby.immutable(), random); // needs accesstransformer.cfg
          }
        });
  }
}
