package com.lothrazar.cyclic.block.glass;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DarkGlassConnectedBlock extends DarkGlassBlock {

  public static final BooleanProperty CONNECTED_DOWN = BooleanProperty.create("connected_down");
  public static final BooleanProperty CONNECTED_UP = BooleanProperty.create("connected_up");
  public static final BooleanProperty CONNECTED_NORTH = BooleanProperty.create("connected_north");
  public static final BooleanProperty CONNECTED_SOUTH = BooleanProperty.create("connected_south");
  public static final BooleanProperty CONNECTED_WEST = BooleanProperty.create("connected_west");
  public static final BooleanProperty CONNECTED_EAST = BooleanProperty.create("connected_east");

  public DarkGlassConnectedBlock(Properties properties) {
    super(properties);
    this.setDefaultState(stateContainer.getBaseState().with(CONNECTED_DOWN, Boolean.FALSE).with(CONNECTED_EAST, Boolean.FALSE).with(CONNECTED_NORTH, Boolean.FALSE).with(CONNECTED_SOUTH, Boolean.FALSE).with(CONNECTED_UP, Boolean.FALSE).with(CONNECTED_WEST, Boolean.FALSE));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
  }

  @SuppressWarnings("deprecation")
  @Override
  @OnlyIn(Dist.CLIENT)
  public boolean isSideInvisible(BlockState state, BlockState adjacentBlockState, Direction side) {
    return adjacentBlockState.isIn(this) || super.isSideInvisible(state, adjacentBlockState, side);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    IBlockReader world = context.getWorld();
    BlockPos pos = context.getPos();
    return super.getStateForPlacement(context)
        .with(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
        .with(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
        .with(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
        .with(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
        .with(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
        .with(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
  }

  @Override
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld world, BlockPos pos, BlockPos facingPos) {
    return stateIn.with(CONNECTED_DOWN, this.isSideConnectable(world, pos, Direction.DOWN))
        .with(CONNECTED_EAST, this.isSideConnectable(world, pos, Direction.EAST))
        .with(CONNECTED_NORTH, this.isSideConnectable(world, pos, Direction.NORTH))
        .with(CONNECTED_SOUTH, this.isSideConnectable(world, pos, Direction.SOUTH))
        .with(CONNECTED_UP, this.isSideConnectable(world, pos, Direction.UP))
        .with(CONNECTED_WEST, this.isSideConnectable(world, pos, Direction.WEST));
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
  }

  private boolean isSideConnectable(IBlockReader world, BlockPos pos, Direction side) {
    final BlockState stateConnection = world.getBlockState(pos.offset(side));
    return stateConnection != null && stateConnection.getBlock() == this;
  }
}
