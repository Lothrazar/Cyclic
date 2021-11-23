package com.lothrazar.cyclic.block.fanslab;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockFanSlab extends BlockCyclic implements SimpleWaterloggedBlock {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
  public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
  //
  protected static final VoxelShape AABB_CEILING_X_ON = Block.box(0.0D, 15.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_Z_ON = Block.box(0.0D, 15.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_X_ON = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_Z_ON = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
  protected static final VoxelShape AABB_NORTH_ON = Block.box(0.0D, 0.0D, 15.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_SOUTH_ON = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
  protected static final VoxelShape AABB_WEST_ON = Block.box(15.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_EAST_ON = Block.box(0.0D, 0.0D, 0.0D,
      1.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_X_OFF = Block.box(0.0D, 14.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_CEILING_Z_OFF = Block.box(5.0D, 14.0D, 6.0D,
      11.0D, 16.0D, 10.0D);
  protected static final VoxelShape AABB_FLOOR_X_OFF = Block.box(0.0D, 0.0D, 0.0D,
      16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AABB_FLOOR_Z_OFF = Block.box(0.0D, 0.0D, 0.0D,
      16.0D, 2.0D, 16.0D);
  protected static final VoxelShape AABB_NORTH_OFF = Block.box(0.0D, 0.0D, 14.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_SOUTH_OFF = Block.box(0.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 2.0D);
  protected static final VoxelShape AABB_WEST_OFF = Block.box(14.0D, 0.0D, 0.0D,
      16.0D, 16.0D, 16.0D);
  protected static final VoxelShape AABB_EAST_OFF = Block.box(0.0D, 0.0D, 0.0D,
      2.0D, 16.0D, 16.0D);

  public BlockFanSlab(Properties properties) {
    super(properties.strength(0.8F).noOcclusion());
    this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false).setValue(HORIZONTAL_FACING, Direction.NORTH).setValue(POWERED, Boolean.valueOf(false)).setValue(FACE, AttachFace.WALL));
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (stateIn.getValue(WATERLOGGED)) {
      worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    }
    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    Direction direction = state.getValue(HORIZONTAL_FACING);
    boolean powered = state.getValue(POWERED);
    switch (state.getValue(FACE)) {
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
  public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter world, BlockPos pos, FluidState fluidState) {
    return true;
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileFanSlab(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.FANSLAB.get(), world.isClientSide ? TileFanSlab::clientTick : TileFanSlab::serverTick);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    for (Direction direction : context.getNearestLookingDirections()) {
      BlockState blockstate;
      if (direction.getAxis() == Direction.Axis.Y) {
        blockstate = this.defaultBlockState().setValue(FACE, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR).setValue(HORIZONTAL_FACING, context.getHorizontalDirection());
      }
      else {
        blockstate = this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(HORIZONTAL_FACING, direction.getOpposite());
      }
      if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
        return blockstate.setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
      }
    }
    return null;
  }
  //  @Override
  //  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
  //    if (entity != null) {
  //      world.setBlockState(pos, state.with(HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntity(pos, entity)), 2);
  //    }
  //  } 
  //  @Override
  //  public BlockState getStateForPlacement(BlockItemUseContext context) {
  //    return super.getStateForPlacement(context)
  //        .with(WATERLOGGED, context.getWorld().getFluidState(context.getPos()).getFluid() == Fluids.WATER);
  //  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(HORIZONTAL_FACING).add(POWERED).add(FACE).add(WATERLOGGED);
  }
}
