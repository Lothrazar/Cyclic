package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class SolidFluidBlock extends FlowingFluidBlock {

  VoxelShape shapes[] = new VoxelShape[16];

  public SolidFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
    int max = 15; //max of the property LEVEL.getAllowedValues()
    float offset = 0.875F;
    for (int i = 0; i <= max; i++) { //x and z go from [0,1] 
      shapes[i] = VoxelShapes.create(new AxisAlignedBB(0, 0, 0, 1, offset - i / 8F, 1));
    }
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
    return shapes[state.get(LEVEL).intValue()];
  }

  @Override
  @Deprecated
  public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
    return shapes[state.get(LEVEL).intValue()];
  }

  @Override
  public int tickRate(IWorldReader worldIn) {
    return super.tickRate(worldIn) / 2 + 1;
  }
}
