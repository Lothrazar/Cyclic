package com.lothrazar.cyclic.fluid.block;

import com.lothrazar.library.fluid.PartialHeightFluidBlock;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

public class MagmaFluidBlock extends PartialHeightFluidBlock {

  public MagmaFluidBlock(java.util.function.Supplier<? extends FlowingFluid> supplier, Properties props) {
    super(supplier, props);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn, net.minecraft.world.entity.InsideBlockEffectApplier effectApplier, boolean isPrecise) {
    if (entityIn instanceof LivingEntity ent && !ent.isOnFire() && !ent.fireImmune()) {
      int level = EnchantUtil.getCurrentArmorLevel(EnchantUtil.holder(Enchantments.FIRE_PROTECTION, ent), ent);
      if (level < 4) {
        ent.igniteForSeconds(Mth.floor(worldIn.getRandom().nextDouble() * 10));
      }
    }
    super.entityInside(state, worldIn, pos, entityIn, effectApplier, isPrecise);
  }
}
