package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.library.core.IBlockFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLightFacade extends BlockCyclic implements IBlockFacade {

  private static final VoxelShape THREE = Block.box(6, 6, 6,
      10, 10, 10);

  public BlockLightFacade(Properties properties) {
    super(properties.lightLevel(state -> 15).strength(1F).noOcclusion());
    registerDefaultState(defaultBlockState().setValue(IBlockFacade.HAS_FACADE, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(IBlockFacade.HAS_FACADE);
  }

  // 26.1: RenderShape shrank to just INVISIBLE/MODEL (ENTITYBLOCK_ANIMATED removed) - INVISIBLE is the
  // direct replacement (skip the static model, let the facade's BER draw the camouflaged look instead).
  @Override
  public RenderShape getRenderShape(BlockState state) {
    if (state.getValue(IBlockFacade.HAS_FACADE)) {
      return RenderShape.INVISIBLE;
    }
    return RenderShape.MODEL;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    var facade = this.getFacadeShape(state, worldIn, pos, context);
    if (facade != null) {
      return facade;
    }
    return THREE;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileLightFacade(pos, state);
  }
}
