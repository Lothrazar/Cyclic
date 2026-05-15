package com.lothrazar.cyclic.item.food;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;


public class AppleChocolate extends ItemBaseCyclic {

  public AppleChocolate(Properties properties) {
    super(properties);
  }

  @Override
  public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
    //    if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
    curePotionEffects(entityLiving, stack);
    return super.finishUsingItem(stack, worldIn, entityLiving);
  }

  private boolean curePotionEffects(LivingEntity entityLiving, ItemStack curativeItem) {
    boolean ret = false;
    java.util.List<Holder<MobEffect>> toRemove = new java.util.ArrayList<>();
    for (MobEffectInstance effect : entityLiving.getActiveEffects()) {
      if (!effect.getEffect().value().isInstantenous()) { // approximation for not beneficial since isBeneficial is gone or changed
        toRemove.add(effect.getEffect());
      }
    }
    for (Holder<MobEffect> e : toRemove) {
      entityLiving.removeEffect(e);
      ret = true;
    }
    return ret;
  }
}
