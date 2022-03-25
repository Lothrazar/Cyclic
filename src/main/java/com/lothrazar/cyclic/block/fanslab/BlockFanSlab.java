package com.lothrazar.cyclic.block.fanslab;

import com.lothrazar.cyclic.base.BlockBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

public class BlockFanSlab extends BlockBase implements IWaterLoggable {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<AttachFace> FACE = BlockStateProperties.FACE;
  //
  protected static final VoxelShape AABB_CEILING_X_ON = Block.makeCuboidShape(0.0D, 15.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_Z_ON = Block.makeCuboidShape(0.0D, 15.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_X_ON = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_Z_ON = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AABB_NORTH_ON = Block.makeCuboidShape(0.0D, 0.0D, 15.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_SOUTH_ON = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
  protected static final VoxelShape AABB_WEST_ON = Block.makeCuboidShape(15.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_EAST_ON = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
      1.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_X_OFF = Block.makeCuboidShape(0.0D, 14.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_Z_OFF = Block.makeCuboidShape(5.0D, 14.0D, 6.0D,
      11.0D, 16.0D, 10.0D);
  protected static final VoxelShape AABB_FLOOR_X_OFF = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
      16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_Z_OFF = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
      16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AABB_NORTH_OFF = Block.makeCuboidShape(0.0D, 0.0D, 14.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_SOUTH_OFF = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 2.0D);
  protected static final VoxelShape AABB_WEST_OFF = Block.makeCuboidShape(14.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_EAST_OFF = Block.makeCuboidShape(0.0D, 0.0D, 0.0D,
      2.0D, 16.0D, 16.0D);

  public BlockFanSlab(Properties properties) {
    super(properties.hardnessAndResistance(0.8F).notSolid());
    this.setDefaultState(this.stateContainer.getBaseState().with(WATERLOGGED, false).with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, Boolean.valueOf(false)).with(FACE, AttachFace.WALL));
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.get(WATERLOGGED)) {
      worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
    }
    return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    Direction direction = state.get(HORIZONTAL_FACING);
    boolean powered = state.get(POWERED);
    switch (state.get(FACE)) {
      case FLOOR:
        if (direction.getAxis() == Direction.Axis.X) {
          return powered ? AABB_FLOOR_X_ON : AABB_FLOOR_X_OFF;
        }
        return powered ? AABB_FLOOR_Z_ON : AABB_FLOOR_Z_OFF;
      case WALL:
        switch (direction) {
          case EAST:
            return powered ? AABB_EAST_ON : AABB_EAST_OFF;
          case WEST:
            return powered ? AABB_WEST_ON : AABB_WEST_OFF;
          case SOUTH:
            return powered ? AABB_SOUTH_ON : AABB_SOUTH_OFF;
          case NORTH:
          default:
            return powered ? AABB_NORTH_ON : AABB_NORTH_OFF;
        }
      case CEILING:
      default:
        if (direction.getAxis() == Direction.Axis.X) {
          return powered ? AABB_CEILING_X_ON : AABB_CEILING_X_OFF;
        }
        else {
          return powered ? AABB_CEILING_Z_ON : AABB_CEILING_Z_OFF;
        }
    }
  }

  @Override
  public boolean shouldDisplayFluidOverlay(BlockState state, IBlockDisplayReader world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    RenderTypeLookup.setRenderLayer(this, RenderType.getCutoutMipped());
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFanSlab();
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    for (Direction direction : context.getNearestLookingDirections()) {
      BlockState blockstate;
      if (direction.getAxis() == Direction.Axis.Y) {
        blockstate = this.getDefaultState().with(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).with(HORIZONTAL_FACING, context.getPlacementHorizontalFacing());
      }
      else {
        blockstate = this.getDefaultState().with(FACE, AttachFace.WALL).with(HORIZONTAL_FACING, direction.getOpposite());
      }
      if (blockstate.isValidPosition(context.getWorld(), context.getPos())) {
        return blockstate.with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
      }
    }
    return null;
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    super.fillStateContainer(builder);
    builder.add(HORIZONTAL_FACING).add(POWERED).add(FACE).add(WATERLOGGED);
  }
}
