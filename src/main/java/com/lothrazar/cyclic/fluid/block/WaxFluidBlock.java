package com.lothrazar.cyclic.fluid.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class WaxFluidBlock extends LiquidBlock {

  public WaxFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
  }

  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (!worldIn.isClientSide && entityIn instanceof LivingEntity ent) {
      ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 0));
    }
    super.entityInside(state, worldIn, pos, entityIn);
  }
}
