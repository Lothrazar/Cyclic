package com.lothrazar.cyclic.fluid.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class BiomassFluidBlock extends LiquidBlock {

  public BiomassFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (!worldIn.isClientSide && entityIn instanceof LivingEntity ent && worldIn.random.nextDouble() < 000.1F) {
      ent.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0));
    }
    super.entityInside(state, worldIn, pos, entityIn);
  }
}
