package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MagmaFluidBlock extends FlowingFluidBlock {

  public static class Flowing extends ForgeFlowingFluid.Flowing {

    public Flowing(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
      return 2;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
      return 7;
    }
  }

  public static class Source extends ForgeFlowingFluid.Source {

    public Source(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(IWorldReader worldIn) {
      return 2;
    }

    @Override
    public int getLevelDecreasePerBlock(IWorldReader worldIn) {
      return 1;
    }
  }

  VoxelShape shapes[] = new VoxelShape[16];

  public MagmaFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
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

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof LivingEntity) {
      LivingEntity ent = (LivingEntity) entityIn;
      if (ent.isBurning() == false
          && ent.isImmuneToFire() == false) {
        int level = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FIRE_PROTECTION, ent);
        if (level < 4) {
          ent.setFire(MathHelper.floor(worldIn.rand.nextDouble() * 10));
        }
      }
    }
    super.onEntityCollision(state, worldIn, pos, entityIn);
  }
}
