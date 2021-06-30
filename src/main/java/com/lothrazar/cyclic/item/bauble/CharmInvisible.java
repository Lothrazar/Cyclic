package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class CharmInvisible extends ItemBaseToggle {

  public CharmInvisible(Properties properties) {
    super(properties.maxDamage(256 * 256));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (!this.canUse(stack)) {
      return;
    }
    if (!this.isOn(stack)) {
      return;
    }
    if (worldIn.getGameTime() % 20 == 0) {
      LivingEntity living = (LivingEntity) entityIn;
      final int seconds = 6;
      living.addPotionEffect(new EffectInstance(Effects.INVISIBILITY, 20 * seconds, 0));
      if (worldIn.rand.nextDouble() < 0.1) {
        UtilItemStack.damageItem(living, stack);
      }
    }
  }
}
