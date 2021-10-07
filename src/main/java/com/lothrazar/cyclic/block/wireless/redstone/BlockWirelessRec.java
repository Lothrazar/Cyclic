package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockWirelessRec extends BlockBase {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockWirelessRec(Properties properties) {
    super(properties.strength(1.8F));
    this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  @Override
  public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
    return blockState.getValue(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  private int getPower(BlockGetter world, BlockPos pos, Direction side) {
    if (world.getBlockEntity(pos) instanceof TileWirelessRec) {
      return 15;
    }
    return 0;
  }

  @Override
  public boolean isSignalSource(BlockState state) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileWirelessRec();
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
