package com.lothrazar.cyclic.block.facade;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface IBlockFacade {

  default VoxelShape getFacadeShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
    BlockState tfs = getFacadeState(state, level, pos);
    if (tfs != null) {
      return ctx == null ? tfs.getShape(level, pos) : tfs.getShape(level, pos, ctx);
    }
    return null;
  }

  default BlockState getFacadeState(BlockState state, BlockGetter level, BlockPos pos) {
    if (level == null) {
      return null;
    }
    ITileFacade tile = this.getTileFacade(level, pos);
    if (tile != null && level instanceof Level realLevel) {
      return tile.getFacadeState(realLevel);
    }
    return null;
  }

  default ITileFacade getTileFacade(BlockGetter level, BlockPos pos) {
    if (level == null) {
      return null;
    }
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof ITileFacade tf) {
      return tf;
    }
    return null;
  }
}
