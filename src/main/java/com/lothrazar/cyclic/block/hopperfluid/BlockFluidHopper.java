package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockFluidHopper extends BlockBase {

  public static final DirectionProperty FACING = BlockStateProperties.FACING_HOPPER;

  public BlockFluidHopper(Properties properties) {
    super(properties.strength(1.3F));
    this.setHasFluidInteract();
  }

  @Override
  public void registerClient() {
    //    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
    //    ClientRegistry.bindTileEntityRenderer(TileRegistry.FLUIDHOPPER.get(), RenderHopperFluid::new);
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
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
    return new TileFluidHopper();
  }
}
