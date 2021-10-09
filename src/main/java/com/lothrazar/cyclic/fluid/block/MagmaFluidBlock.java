package com.lothrazar.cyclic.fluid.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class MagmaFluidBlock extends LiquidBlock {

  public static class Flowing extends ForgeFlowingFluid.Flowing {

    public Flowing(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(LevelReader worldIn) {
      return 2;
    }

    @Override
    public int getDropOff(LevelReader worldIn) {
      return 7;
    }
  }

  public static class Source extends ForgeFlowingFluid.Source {

    public Source(Properties properties) {
      super(properties);
    }

    @Override
    public int getSlopeFindDistance(LevelReader worldIn) {
      return 2;
    }

    @Override
    public int getDropOff(LevelReader worldIn) {
      return 1;
    }
  }

  VoxelShape shapes[] = new VoxelShape[16];

  public MagmaFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
    int max = 15; //max of the property LEVEL.getAllowedValues()
    float offset = 0.875F;
    for (int i = 0; i <= max; i++) { //x and z go from [0,1] 
      shapes[i] = Shapes.create(new AABB(0, 0, 0, 1, offset - i / 8F, 1));
    }
  }

  @Override
  @Deprecated
  public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @Override
  @Deprecated
  public VoxelShape getOcclusionShape(BlockState state, BlockGetter worldIn, BlockPos pos) {
    return shapes[state.getValue(LEVEL).intValue()];
  }

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof LivingEntity) {
      LivingEntity ent = (LivingEntity) entityIn;
      if (ent.isOnFire() == false
          && ent.fireImmune() == false) {
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, ent);
        if (level < 4) {
          ent.setSecondsOnFire(Mth.floor(worldIn.random.nextDouble() * 10));
        }
      }
    }
    super.entityInside(state, worldIn, pos, entityIn);
  }
}
