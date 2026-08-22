package com.lothrazar.cyclic.item.bauble;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class SoulstoneCharm extends ItemBaseToggle {

  private static final int REGEN_TICKS_DEFAULT = 900;
  private static final int ABSORPTION_TICKS_DEFAULT = 100;
  private static final int FIRERES_TICKS_DEFAULT = 800;
  public static IntValue REGEN_TICKS;
  public static IntValue ABSORPTION_TICKS;
  public static IntValue FIRERES_TICKS;

  public SoulstoneCharm(Properties properties) {
    super(properties);
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
    int regen = REGEN_TICKS == null ? REGEN_TICKS_DEFAULT : REGEN_TICKS.get();
    int abs = ABSORPTION_TICKS == null ? ABSORPTION_TICKS_DEFAULT : ABSORPTION_TICKS.get();
    int fr = FIRERES_TICKS == null ? FIRERES_TICKS_DEFAULT : FIRERES_TICKS.get();
    player.setHealth(1.0F);
    player.removeAllEffects();
    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, regen, 1, false, false, false));
    player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, abs, 1, false, false, false));
    player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, fr, 0, false, false, false));
    player.level().broadcastEntityEvent(player, (byte) 35);
    ItemStackUtil.damageItem(player, itemstack);
    return true;
    //    }
  }
}
