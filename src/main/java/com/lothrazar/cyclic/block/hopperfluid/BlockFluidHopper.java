package com.lothrazar.cyclic.block.hopperfluid;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.hopper.BlockSimpleHopper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockFluidHopper extends BlockBase {

  public static final DirectionProperty FACING = BlockStateProperties.FACING_EXCEPT_UP;

  public BlockFluidHopper(Properties properties) {
    super(properties.hardnessAndResistance(1.3F));
    this.setHasFluidInteract();
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    return calcRedstoneFromFluid(worldIn.getTileEntity(pos));
  }

  @Override
  public void registerClient() {
    //    RenderTypeLookup.setRenderLayer(this, RenderType.getTranslucent());
    //    ClientRegistry.bindTileEntityRenderer(TileRegistry.FLUIDHOPPER.get(), RenderHopperFluid::new);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(FACING);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    Direction direction = context.getFace().getOpposite();
    if (direction == Direction.UP) {
      direction = Direction.DOWN;
    }
    return this.getDefaultState().with(FACING, direction);
  }

  @Override
  public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return BlockSimpleHopper.getShapeHopper(state, worldIn, pos, context);
  }

  @Override
  public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return BlockSimpleHopper.getRaytraceShapeHopper(state, worldIn, pos);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileFluidHopper();
  }
}
