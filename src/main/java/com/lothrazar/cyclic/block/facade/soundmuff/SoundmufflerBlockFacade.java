package com.lothrazar.cyclic.block.facade.soundmuff;

import com.lothrazar.cyclic.block.facade.IBlockFacade;
import com.lothrazar.cyclic.block.soundmuff.SoundmufflerBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoundmufflerBlockFacade extends SoundmufflerBlock implements IBlockFacade {

  private static final VoxelShape THREE = Block.box(6, 6, 6,
      10, 10, 10);

  public SoundmufflerBlockFacade(Properties properties) {
    super(properties.noOcclusion());
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
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new SoundmuffTileFacade(pos, state);
  }
}
