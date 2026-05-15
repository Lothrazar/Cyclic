package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class VenomEnchant {

  public static final int TICKSPERLEVEL = 3 * Const.TICKS_PER_SEC;
  public static final String ID = "venom";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (!isEnabled()) { return; }
    if (!(event.getTarget() instanceof LivingEntity target)) { return; }
    Player attacker = event.getEntity();
    ItemStack main = attacker.getMainHandItem();
    ItemStack off = attacker.getOffhandItem();
    var h = EnchantRegistry.holder(EnchantRegistry.VENOM, attacker);
    int mainLevel = EnchantUtil.getCurrentLevelTool(h, main);
    int offLevel = EnchantUtil.getCurrentLevelTool(h, off);
    int level = Math.max(mainLevel, offLevel);
    if (level > 0) {
      EntityUtil.addOrMergePotionEffect(target, new MobEffectInstance(MobEffects.POISON, TICKSPERLEVEL * level, level - 1));
    }
  }
}
