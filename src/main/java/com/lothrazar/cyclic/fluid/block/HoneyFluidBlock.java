package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HoneyFluidBlock extends FlowingFluidBlock {

  public HoneyFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof LivingEntity) {
      LivingEntity ent = (LivingEntity) entityIn;
      ent.addPotionEffect(new EffectInstance(Effects.REGENERATION, 40, 1));
      ent.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 40, 1));
      ent.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 40, 5));
      ent.addPotionEffect(new EffectInstance(Effects.MINING_FATIGUE, 40, 5));
    }
    super.onEntityCollision(state, worldIn, pos, entityIn);
  }
}
