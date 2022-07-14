package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CharmInvisible extends ItemBaseToggle {

  final int seconds = 30;

  public CharmInvisible(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (worldIn.getGameTime() % 20 == 0 && entityIn instanceof LivingEntity) {
      LivingEntity living = (LivingEntity) entityIn;
      if (living.getEffect(MobEffects.INVISIBILITY) == null) {
        MobEffectInstance eff = new MobEffectInstance(MobEffects.INVISIBILITY, 20 * seconds, 0);
        eff.showIcon = false;
        eff.visible = false;
        living.addEffect(eff);
        ItemStackUtil.damageItem(living, stack);
      }
    }
  }
}
