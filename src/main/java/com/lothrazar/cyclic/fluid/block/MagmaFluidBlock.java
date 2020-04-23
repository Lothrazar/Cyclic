package com.lothrazar.cyclic.fluid.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MagmaFluidBlock extends SolidFluidBlock {

  public MagmaFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Block.Properties props) {
    super(supplier, props);
  }

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
