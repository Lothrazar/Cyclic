package com.lothrazar.cyclic.block.detectmoon;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BlockMoon extends BlockBase {

  public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_0_8;

  public BlockMoon(Properties properties) {
    super(properties.hardnessAndResistance(0.8F));
  }

  @Override
  public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(LEVEL) * 2;
  }

  @Override
  public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
    return blockState.get(LEVEL) * 2;
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileMoon();
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(LEVEL);
  }
}
