package com.lothrazar.cyclic.block.hopper;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.BlockCyclic;
import com.lothrazar.cyclic.registry.TileRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@SuppressWarnings("deprecation")
public class BlockSimpleHopper extends BlockCyclic {

  public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

  public BlockSimpleHopper(Properties properties) {
    super(properties.strength(2.0F, 3.0F));
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
    return createTickerHelper(type, TileRegistry.HOPPER.get(), world.isClientSide ? TileSimpleHopper::clientTick : TileSimpleHopper::serverTick);
  }

  public static VoxelShape getShapeHopper(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    try {
      return Blocks.HOPPER.getShape(state, worldIn, pos, context);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("An unknown has broken the vanilla hopper, causing compatibility issues", e);
      return Shapes.block();
    }
  }

  public static VoxelShape getRaytraceShapeHopper(BlockState state, BlockGetter worldIn, BlockPos pos) {
    try {
      return Blocks.HOPPER.getInteractionShape(state, worldIn, pos);
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("An unknown has broken the vanilla hopper, causing compatibility issues", e);
      return Shapes.block();
    }
  }
}
