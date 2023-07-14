package com.lothrazar.cyclic.block.scaffolding;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class BlockScaffoldingResponsive extends BlockScaffolding {

  public BlockScaffoldingResponsive(Properties properties, boolean autobreak) {
    super(properties, autobreak);
  }

  @Override
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    if (blockIn == this) {
      worldIn.destroyBlock(pos, true);
    }
  }
}
