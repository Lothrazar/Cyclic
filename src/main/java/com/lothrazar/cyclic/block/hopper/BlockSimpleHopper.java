package com.lothrazar.cyclic.block.hopper;

import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BlockSimpleHopper extends BlockCyclic {

  public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

  public BlockSimpleHopper(Properties properties) {
    // isRedstoneConductor(never) so chests/dispensers/cats below still work.
    super(properties.strength(2.0F, 3.0F).isRedstoneConductor(BlockCyclic::never));
  }

  @Override
  public boolean hasAnalogOutputSignal(BlockState bs) {
    return true;
  }

  @Override
  public int getAnalogOutputSignal(BlockState st, Level level, BlockPos pos) {
    return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    Direction direction = context.getClickedFace().getOpposite();
    if (direction == Direction.UP) {
      direction = Direction.DOWN;
    }
    return this.defaultBlockState().setValue(FACING, direction);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return BlockSimpleHopper.getShapeHopper(state, worldIn, pos, context);
  }

  @Override
  public VoxelShape getInteractionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return BlockSimpleHopper.getRaytraceShapeHopper(state, worldIn, pos);
  }

  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new TileSimpleHopper(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return createTickerHelper(type, TileRegistry.HOPPER.get(), world.isClientSide() ? null : TileSimpleHopper::serverTick);
  }

  private static final VoxelShape HOPPER_TOP = Block.box(0, 10, 0, 16, 16, 16);
  private static final VoxelShape HOPPER_FUNNEL = Block.box(4, 4, 4, 12, 10, 12);
  private static final VoxelShape HOPPER_MIDDLE = Shapes.join(HOPPER_TOP, HOPPER_FUNNEL, BooleanOp.OR);
  private static final VoxelShape SHAPE_DOWN = Shapes.join(HOPPER_MIDDLE, Block.box(6, 0, 6, 10, 4, 10), BooleanOp.OR);
  private static final VoxelShape SHAPE_NORTH = Shapes.join(HOPPER_MIDDLE, Block.box(6, 4, 0, 10, 8, 4), BooleanOp.OR);
  private static final VoxelShape SHAPE_SOUTH = Shapes.join(HOPPER_MIDDLE, Block.box(6, 4, 12, 10, 8, 16), BooleanOp.OR);
  private static final VoxelShape SHAPE_WEST = Shapes.join(HOPPER_MIDDLE, Block.box(0, 4, 6, 4, 8, 10), BooleanOp.OR);
  private static final VoxelShape SHAPE_EAST = Shapes.join(HOPPER_MIDDLE, Block.box(12, 4, 6, 16, 8, 10), BooleanOp.OR);
  private static final VoxelShape INTERACT_DOWN = Block.box(2, 11, 2, 14, 16, 14);
  private static final VoxelShape INTERACT_NORTH = Block.box(2, 11, 0, 14, 16, 14);
  private static final VoxelShape INTERACT_SOUTH = Block.box(2, 11, 2, 14, 16, 16);
  private static final VoxelShape INTERACT_WEST = Block.box(0, 11, 2, 14, 16, 14);
  private static final VoxelShape INTERACT_EAST = Block.box(2, 11, 2, 16, 16, 14);

  public static VoxelShape getShapeHopper(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return switch (state.getValue(FACING)) {
      case NORTH -> SHAPE_NORTH;
      case SOUTH -> SHAPE_SOUTH;
      case WEST -> SHAPE_WEST;
      case EAST -> SHAPE_EAST;
      default -> SHAPE_DOWN;
    };
  }

  public static VoxelShape getRaytraceShapeHopper(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return switch (state.getValue(FACING)) {
      case NORTH -> INTERACT_NORTH;
      case SOUTH -> INTERACT_SOUTH;
      case WEST -> INTERACT_WEST;
      case EAST -> INTERACT_EAST;
      default -> INTERACT_DOWN;
    };
  }
}
