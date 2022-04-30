package com.lothrazar.cyclic.block.generatorexpl;

import com.lothrazar.cyclic.block.BlockCyclic;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;

public class BlockDestruction extends BlockCyclic {

  public static IntValue HEIGHT;
  public static IntValue RADIUS;
  public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
  private static final double BOUNDS = 3;
  public static final VoxelShape AABB = Block.box(BOUNDS, 0, BOUNDS,
      16 - BOUNDS, 16 - BOUNDS, 16 - BOUNDS);

  public BlockDestruction(Properties properties) {
    super(properties.strength(1.2F).noOcclusion());
    this.registerDefaultState(defaultBlockState().setValue(WATERLOGGED, false));
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return AABB;
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
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(WATERLOGGED);
  }

  @Override
  public void registerClient() {
    ItemBlockRenderTypes.setRenderLayer(this, RenderType.cutoutMipped());
  }
}
