package com.lothrazar.cyclic.block.glass;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class DarkGlassBlock extends BlockCyclic {

  public DarkGlassBlock(Properties properties) {
    super(properties.strength(0.5F, 3600000.0F).sound(SoundType.GLASS).noOcclusion());
  }

  @Override
  public int getLightDampening(BlockState state) {
    return 255; //zero is transparent fullyworld.getMaxLightLevel();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state) {
    return false;
  }

  @Override
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0F;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndLightGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
  }
}
