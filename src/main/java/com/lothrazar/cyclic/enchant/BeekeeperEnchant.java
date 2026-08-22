package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class BeekeeperEnchant {

  public static final String ID = "beekeeper";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  private Holder<Enchantment> holder(LivingEntity entity) {
    return EnchantUtil.holder(EnchantRegistry.BEEKEEPER, entity);
  }

  @SubscribeEvent
  public void onLivingChangeTargetEvent(LivingChangeTargetEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (event.getNewAboutToBeSetTarget() instanceof Player target && event.getEntity().getType() == EntityType.BEE && event.getEntity() instanceof Bee bee) {
      int level = EnchantUtil.getCurrentArmorLevel(holder(target), target);
      if (level > 0) {
        event.setCanceled(true);
        bee.setAggressive(false);
        // 26.1: NeutralMob#setRemainingPersistentAngerTime(int) removed - stopBeingAngry() is the direct
        // "no longer angry" replacement (clears the end time AND the persistent anger target in one call)
        bee.stopBeingAngry();
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.LOWEST)
  public void onLivingDamageEvent(LivingDamageEvent.Pre event) {
    if (!isEnabled()) {
      return;
    }
    int level = EnchantUtil.getCurrentArmorLevel(holder(event.getEntity()), event.getEntity());
    if (level >= 1 && event.getSource() != null && event.getSource().getDirectEntity() != null) {
      Entity esrc = event.getSource().getDirectEntity();
      if (esrc.getType() == EntityType.BEE ||
          esrc.getType() == EntityType.BAT ||
          esrc.getType() == EntityType.LLAMA_SPIT) {
        event.setNewDamage(0);
      }
      if (level >= 2) {
        if (esrc.getType() == EntityType.PHANTOM) {
          event.setNewDamage(0);
        }
      }
    }
  }
}
