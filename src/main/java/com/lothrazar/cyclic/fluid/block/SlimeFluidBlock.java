package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SlimeFluidBlock extends FlowingFluidBlock {

  public static class Flowing extends ForgeFlowingFluid.Flowing {

    public Flowing(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
      return 1;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
      return 2;
    }
  }

  public static class Source extends ForgeFlowingFluid.Source {

    public Source(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
      return 1;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
      return 6;
    }
  }

  VoxelShape shapes[] = new VoxelShape[16];

  public SlimeFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
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
  public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
    if (entityIn.isSuppressingBounce()) {
      super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    }
    else {
      entityIn.onLivingFall(fallDistance, 0.0F);
    }
  }

  @Override
  public void onLanded(IBlockReader worldIn, Entity entityIn) {
    if (entityIn.isSuppressingBounce()) {
      super.onLanded(worldIn, entityIn);
    }
    else {
      this.func_226946_a_(entityIn);
    }
  }

  /**
   * From SlimeBlock.java
   * 
   * @param entity
   */
  private void func_226946_a_(Entity entity) {
    Vec3d vec3d = entity.getMotion();
    if (vec3d.y < 0.0D) {
      double d0 = entity instanceof LivingEntity ? 1.0D : 0.8D;
      entity.setMotion(vec3d.x, -vec3d.y * d0, vec3d.z);
    }
  }

  @Override
  public int tickRate(IWorldReader worldIn) {
    return super.tickRate(worldIn) * 3;
  }
}
