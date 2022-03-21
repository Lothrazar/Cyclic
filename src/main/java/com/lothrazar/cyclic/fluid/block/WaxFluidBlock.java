package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.cyclic.data.Const;
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

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof LivingEntity) {
      LivingEntity ent = (LivingEntity) entityIn;
      ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 4));
      //      ent.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 5));
      ent.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 40, 1));
      if (worldIn.random.nextDouble() < 0.01) { // TODO config
        entityIn.setRemainingFireTicks(entityIn.getRemainingFireTicks() + 2 * Const.TICKS_PER_SEC);
      }
    }
    super.entityInside(state, worldIn, pos, entityIn);
  }
}
