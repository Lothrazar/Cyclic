package com.lothrazar.cyclic.block.lightcompr;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.facade.IBlockFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLightCamo extends BlockCyclic implements IBlockFacade {

  private static final double BOUNDS = 7;
  private static final VoxelShape AABB = Block.box(BOUNDS, BOUNDS, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);
  public BlockLightCamo(Properties properties) {
    super(properties.lightLevel(state -> 15).strength(1F).noOcclusion());
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    var facade = this.getFacadeShape(state, worldIn, pos, context);
    if (facade != null) {
      return facade;
    }
    return AABB; // super.getShape(state, worldIn, pos, context);
  }

  @Override
  public void registerClient() {}

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileLightCamo(pos, state);
  }
}
