package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class SoulstoneCharm extends ItemBaseToggle {

  public SoulstoneCharm(Properties properties) {
    super(properties);
  }

  @Override
  public Rarity getRarity(ItemStack stack) {
    return Rarity.UNCOMMON;
  }

  //from LivingEntity class
  public static boolean checkTotemDeathProtection(DamageSource damageSourceIn, Player player, ItemStack itemstack) {
    if (itemstack.getItem() != ItemRegistry.SOULSTONE.get()) {
      return false;
    }
    //    if (damageSourceIn.isBypassInvul()) {
    // /kill command and other OP sources can bypass
    //    return false;
    //    }
    //    else {
    player.setHealth(1.0F);
    player.removeAllEffects();
    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
    player.level().broadcastEntityEvent(player, (byte) 35);
    ItemStackUtil.damageItem(player, itemstack);
    return true;
    //    }
  }
}
