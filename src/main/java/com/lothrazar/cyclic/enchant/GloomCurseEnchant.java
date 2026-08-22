package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.FakePlayerUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.Collections;
import java.util.List;

public class GloomCurseEnchant {

  public static final double BASE_ACTIVATION_CHANCE = 0.1;
  public static final double BASE_APPLY_CHANCE = 0.3;
  public static final double MIN_EFFECTS = 1;
  public static final double MAX_EFFECTS = 3;
  public static final int EFFECT_DURATION = Const.TICKS_PER_SEC * 5;

  public static BooleanValue CFG;
  public static final String ID = "curse";

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void doPostHurt(LivingDamageEvent.Post event) {
    LivingEntity user = event.getEntity();
    Entity attacker = event.getSource().getEntity();
    if (!isEnabled() || user.level().isClientSide() || !(attacker instanceof LivingEntity livingAttacker)
        || FakePlayerUtil.isFakePlayer(attacker)) {
      return;
    }
    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.CURSE, user);
    int totalLevels = EnchantUtil.getCurrentArmorLevelSlot(h, user, EquipmentSlot.HEAD)
        + EnchantUtil.getCurrentArmorLevelSlot(h, user, EquipmentSlot.CHEST)
        + EnchantUtil.getCurrentArmorLevelSlot(h, user, EquipmentSlot.LEGS)
        + EnchantUtil.getCurrentArmorLevelSlot(h, user, EquipmentSlot.FEET);
    if (totalLevels <= 0) {
      return;
    }
    double adjustedActivationChance = BASE_ACTIVATION_CHANCE / totalLevels;
    if (adjustedActivationChance > user.level().getRandom().nextDouble()) {
      List<MobEffect> negativeEffects = EnchantUtil.getNegativeEffects();
      Collections.shuffle(negativeEffects);
      int appliedEffects = 0;
      for (MobEffect effect : negativeEffects) {
        if (effect == null) {
          continue;
        }
        Identifier effectKey = BuiltInRegistries.MOB_EFFECT.getKey(effect);
        if (StringParseUtil.isInList(ConfigRegistry.getGloomIgnoreList(), effectKey)) {
          ModCyclic.LOGGER.debug("Gloom(curse) effect cannot apply " + effectKey);
          continue;
        }
        if (appliedEffects < MIN_EFFECTS || BASE_APPLY_CHANCE > user.level().getRandom().nextDouble()) {
          livingAttacker.addEffect(new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), EFFECT_DURATION));
          appliedEffects++;
          if (appliedEffects >= MAX_EFFECTS) {
            break;
          }
        }
      }
    }
  }
}
