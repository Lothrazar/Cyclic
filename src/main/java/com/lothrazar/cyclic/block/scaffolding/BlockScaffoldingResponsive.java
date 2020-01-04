package com.lothrazar.cyclic.block.scaffolding;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockScaffoldingResponsive extends BlockScaffolding {

  public BlockScaffoldingResponsive(Properties properties, boolean autobreak) {
    super(properties, autobreak);
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    if (blockIn == this) {
      worldIn.destroyBlock(pos, true);
    }
  }
}
