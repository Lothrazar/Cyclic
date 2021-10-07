package com.lothrazar.cyclic.block.detectmoon;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockMoon extends BlockBase {

  public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;

  public BlockMoon(Properties properties) {
    super(properties.strength(0.8F));
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(LEVEL) * 2;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(LEVEL) * 2;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileMoon();
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(LEVEL);
  }
}
