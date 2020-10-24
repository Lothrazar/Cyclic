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

public class BiomassFluidBlock extends FlowingFluidBlock {

  public BiomassFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
    if (entityIn instanceof LivingEntity && worldIn.rand.nextDouble() < 000.1F) {
      LivingEntity ent = (LivingEntity) entityIn;
      ent.addPotionEffect(new EffectInstance(Effects.POISON, 40, 0));
    }
    super.onEntityCollision(state, worldIn, pos, entityIn);
  }
}
