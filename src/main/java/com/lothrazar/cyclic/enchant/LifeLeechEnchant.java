package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class LifeLeechEnchant {

  public static final String ID = "life_leech";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEntityKill(LivingDeathEvent event) {
    if (!isEnabled()) { return; }
    if (!(event.getSource().getEntity() instanceof Player attacker)) { return; }
    if (!(event.getEntity() instanceof LivingEntity target)) { return; }
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.LIFELEECH, attacker), attacker);
    if (level > 0) {
      int restore = (int) Math.max(Math.ceil(target.getMaxHealth() / 5), 4);
      int min = level;
      restore = attacker.level().getRandom().nextInt(restore + 1) + min;
      if (restore > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
        attacker.heal(restore);
      }
    }
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (!isEnabled()) { return; }
    Player attacker = event.getEntity();
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.LIFELEECH, attacker), attacker);
    if (level > 0 && attacker.getHealth() < attacker.getMaxHealth()) {
      attacker.heal(level);
    }
  }
}
