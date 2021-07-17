package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class CharmInvisible extends ItemBaseToggle {

  final int seconds = 30;

  public CharmInvisible(Properties properties) {
    super(properties);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (worldIn.getGameTime() % 20 == 0 && entityIn instanceof LivingEntity) {
      LivingEntity living = (LivingEntity) entityIn;
      if (living.getActivePotionEffect(Effects.INVISIBILITY) == null) {
        EffectInstance eff = new EffectInstance(Effects.INVISIBILITY, 20 * seconds, 0);
        eff.showIcon = false;
        eff.showParticles = false;
        living.addPotionEffect(eff);
        UtilItemStack.damageItem(living, stack);
      }
    }
  }
}
