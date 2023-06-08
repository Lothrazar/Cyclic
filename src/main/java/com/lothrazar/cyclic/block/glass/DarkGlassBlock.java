package com.lothrazar.cyclic.block.glass;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DarkGlassBlock extends BlockCyclic {

  public DarkGlassBlock(Properties properties) {
    super(properties.strength(0.5F, 3600000.0F).sound(SoundType.GLASS).noOcclusion());
  }

  @Override
  @Deprecated
  public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 255; //zero is transparent fullyworld.getMaxLightLevel();
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return false;
  }

  @Override
  @Deprecated
  @OnlyIn(Dist.CLIENT)
  public float getShadeBrightness(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return 1.0F;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }
}
