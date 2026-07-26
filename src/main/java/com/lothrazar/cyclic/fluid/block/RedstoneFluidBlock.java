package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.library.fluid.GenericFluidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class RedstoneFluidBlock extends GenericFluidBlock {

  public RedstoneFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
    super.onPlace(state, level, pos, oldState, movedByPiston);
    level.updateNeighborsAt(pos, this);
    level.updateNeighborsAt(pos.above(), this);
  }

  @Override
  protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
    if (true) {
      level.updateNeighborsAt(pos, this);
      level.updateNeighborsAt(pos.above(), this);
    }
    super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
    return state.getFluidState().isSource() ? 15 : state.getFluidState().getAmount();
  }

  @Override
  public int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
    return getSignal(state, level, pos, dir);
  }
}
