package com.lothrazar.cyclic.block.facade.light;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.block.facade.IBlockFacade;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockLightFacade extends BlockCyclic implements IBlockFacade {

  private static final VoxelShape THREE = Block.box(6, 6, 6,
      10, 10, 10);

  public BlockLightFacade(Properties properties) {
    super(properties.lightLevel(state -> 15).strength(1F).noOcclusion());
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    var facade = this.getFacadeShape(state, worldIn, pos, context);
    if (facade != null) {
      return facade;
    }
    return THREE; // super.getShape(state, worldIn, pos, context);
  }

  @Override
  public void registerClient() {}

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileLightFacade(pos, state);
  }
}
