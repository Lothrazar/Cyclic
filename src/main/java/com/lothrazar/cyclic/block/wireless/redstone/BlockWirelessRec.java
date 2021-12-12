package com.lothrazar.cyclic.block.wireless.redstone;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockWirelessRec extends BlockBase {

  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

  public BlockWirelessRec(Properties properties) {
    super(properties.hardnessAndResistance(1.8F));
    this.setDefaultState(this.stateContainer.getBaseState().with(POWERED, Boolean.FALSE));
  }

  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(POWERED) ? getPower(blockAccess, pos, side.getOpposite()) : 0;
  }

  private int getPower(IBlockReader world, BlockPos pos, Direction side) {
    if (world.getTileEntity(pos) instanceof TileWirelessRec) {
      return 15;
    }
    return 0;
  }

  @Override
  public boolean canProvidePower(BlockState state) {
    return true;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileWirelessRec();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(POWERED);
  }
}
