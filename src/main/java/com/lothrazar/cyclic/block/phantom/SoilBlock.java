package com.lothrazar.cyclic.block.phantom;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SoilBlock extends BlockCyclic {

  public SoilBlock(Properties properties) {
    super(properties.strength(1));
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SoilTile(pos, state);
  }
}
