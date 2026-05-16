package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ElytraItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class TravellerEnchant {

  public static final String ID = "traveler";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEnderTeleportEvent(EntityTeleportEvent.EnderPearl event) {
    if (!isEnabled()) { return; }
    if (event.getEntity() instanceof LivingEntity living) {
      int level = EnchantUtil.getCurrentArmorLevelSlot(EnchantUtil.holder(EnchantRegistry.TRAVELLER, living), living, EquipmentSlot.LEGS);
      if (level > 0) {
        event.setAttackDamage(0F);
      }
    }
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent.Pre event) {
    if (!isEnabled()) { return; }
    LivingEntity entity = event.getEntity();
    int level = EnchantUtil.getCurrentArmorLevelSlot(EnchantUtil.holder(EnchantRegistry.TRAVELLER, entity), entity, EquipmentSlot.LEGS);
    DamageSource source = event.getSource();
    DamageSources sourcesList = entity.level().damageSources();
    if (level > 0 && (source == sourcesList.cactus()
        || source == sourcesList.flyIntoWall()
        || source == sourcesList.sweetBerryBush()
        || source == sourcesList.sting(null))) {
      event.setNewDamage (0.1F);
    }
    if (level > 0 && source == sourcesList.fall()) {
      if (entity.fallDistance <= 8) {
        event.setNewDamage (0.1F);
      }
      else if (entity.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof ElytraItem) {
        if (event.getOriginalDamage()  > entity.getHealth() - 0.5F) {
          event.setNewDamage (entity.getHealth() - 1F);
          ParticleUtil.spawnParticle(entity.level(), ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, entity.blockPosition(), 4);
        }
      }
    }
  }
}
