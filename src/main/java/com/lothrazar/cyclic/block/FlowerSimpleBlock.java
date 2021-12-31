package com.lothrazar.cyclic.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;

public class FlowerSimpleBlock extends BlockSimple implements IPlantable {

  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  protected static final VoxelShape SHAPE = Block.box(5.0D, 0.0D, 5.0D, 11.0D, 10.0D, 11.0D);

  public FlowerSimpleBlock(Properties properties) {
    super(properties.noOcclusion().noCollission().sound(SoundType.GRASS).strength(0));
    registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(WATERLOGGED);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return super.getStateForPlacement(context)
        .setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER);
  }

  @Override
  @SuppressWarnings("deprecation")
  public FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    Vec3 vec3d = state.getOffset(worldIn, pos);
    return SHAPE.move(vec3d.x, vec3d.y, vec3d.z);
  }

  protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
    Block block = state.getBlock();
    return block == Blocks.GRASS_BLOCK || block == Blocks.DIRT || block == Blocks.COARSE_DIRT || block == Blocks.PODZOL || block == Blocks.FARMLAND;
  }

  @Override
  @SuppressWarnings("deprecation")
  public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
    if (!stateIn.canSurvive(worldIn, currentPos)) {
      return Blocks.AIR.defaultBlockState();
    }
    //    if (stateIn.getValue(WATERLOGGED)) {
    //      worldIn.getLiquidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(worldIn));
    //    }
    return super.updateShape(stateIn, facing, facingState, worldIn, currentPos, facingPos);
  }

  @Override
  public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
    BlockPos blockpos = pos.below();
    if (state.getBlock() == this) { //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
      return worldIn.getBlockState(blockpos).canSustainPlant(worldIn, blockpos, Direction.UP, this);
    }
    return this.isValidGround(worldIn.getBlockState(blockpos), worldIn, blockpos);
  }

  @Override
  public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
    return true;
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
    return type == PathComputationType.AIR ? true : super.isPathfindable(state, worldIn, pos, type);
  }

  @Override
  public BlockState getPlant(BlockGetter world, BlockPos pos) {
    BlockState state = world.getBlockState(pos);
    if (state.getBlock() != this) {
      return defaultBlockState();
    }
    return state;
  }
}
