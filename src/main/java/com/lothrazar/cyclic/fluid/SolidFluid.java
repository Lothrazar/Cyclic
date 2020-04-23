package com.lothrazar.cyclic.fluid;

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

public class SolidFluid extends FlowingFluidBlock {

  VoxelShape shapes[] = new VoxelShape[16];

  public SolidFluid(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
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
}
