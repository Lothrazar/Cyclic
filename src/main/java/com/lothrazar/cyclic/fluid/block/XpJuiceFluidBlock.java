package com.lothrazar.cyclic.fluid.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;

public class XpJuiceFluidBlock extends LiquidBlock {

  public XpJuiceFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);

  }
  //
  //  @SuppressWarnings("deprecation")
  //  @Override
  //  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
  //    if (entityIn instanceof LivingEntity && worldIn.random.nextDouble() < 000.1F) {
  //      LivingEntity ent = (LivingEntity) entityIn;
  //      ent.addEffect(new MobEffectInstance(MobEffects.POISON, 40, 0));
  //    }
  //    super.entityInside(state, worldIn, pos, entityIn);
  //  }
}
