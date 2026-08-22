package com.lothrazar.cyclic.block.glass;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndLightGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;

public class GlassConnectedBlock extends BlockCyclic {

  public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
  public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
  public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
  public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
  public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
  public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");

  public GlassConnectedBlock(Properties properties) {
    super(properties.sound(SoundType.GLASS).noOcclusion());
    this.registerDefaultState(stateDefinition.any().setValue(CONNECTED_DOWN, Boolean.FALSE).setValue(CONNECTED_EAST, Boolean.FALSE).setValue(CONNECTED_NORTH, Boolean.FALSE).setValue(CONNECTED_SOUTH, Boolean.FALSE).setValue(CONNECTED_UP, Boolean.FALSE).setValue(CONNECTED_WEST, Boolean.FALSE));
  }

  @Override
  public float getShadeBrightness(BlockState p_48731_, BlockGetter p_48732_, BlockPos p_48733_) {
    return 1.0F;
  }

  @Override
  protected boolean propagatesSkylightDown(BlockState p_48740_) {
    return true;
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndLightGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    BlockGetter world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    return super.getStateForPlacement(context)
        .setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
        .setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
        .setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
        .setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
        .setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
        .setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
  }

  @Override
  public BlockState updateShape(BlockState stateIn, LevelReader world, ScheduledTickAccess ticks, BlockPos pos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource random) {
    return stateIn.setValue(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
        .setValue(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
        .setValue(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
        .setValue(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
        .setValue(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
        .setValue(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
  }

  private boolean isSideConnectable(BlockGetter world, BlockPos pos, Direction side) {
    final BlockState stateConnection = world.getBlockState(pos.relative(side));
    return stateConnection != null && stateConnection.getBlock() == this;
  }
}
