package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.PlayerUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class LastStandEnchant {

  public static final String ID = "laststand";
  public static BooleanValue CFG;
  public static IntValue COST;
  public static IntValue ABS;
  public static IntValue COOLDOWN;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEntityUpdate(LivingDamageEvent.Pre event) {
    if (!isEnabled()) { return; }
    final int level = EnchantUtil.getCurrentArmorLevelSlot(
        EnchantUtil.holder(EnchantRegistry.STAND, event.getEntity()), event.getEntity(), EquipmentSlot.LEGS);
    if (level > 0 && event.getEntity().getHealth() - event.getOriginalDamage() <= 0 && event.getEntity() instanceof ServerPlayer player) {
      int cooldownTicks = COOLDOWN == null ? 20 : COOLDOWN.get();
      if (cooldownTicks > 0 && player.getCooldowns().isOnCooldown(player.getItemBySlot(EquipmentSlot.LEGS))) {
        return;
      }
      final int xpCost = Math.max(1, (COST == null ? 50 : COST.get()) / level);
      if (PlayerUtil.getExpTotal(player) < xpCost) { return; }
      float toSurvive = event.getEntity().getHealth() - 1;
      event.setNewDamage (toSurvive);
      player.giveExperiencePoints(-1 * xpCost);
      SoundUtil.playSoundFromServer(player, SoundRegistry.CHAOS_REAPER.get(), 1F, 0.4F);
      ChatUtil.sendStatusMessage(player, "enchantment." + ModCyclic.MODID + "." + ID + ".activated");
      int absTicks = ABS == null ? 600 : ABS.get();
      if (absTicks > 0) {
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, absTicks, level - 1));
      }
      if (cooldownTicks > 0) {
        player.getCooldowns().addCooldown(player.getItemBySlot(EquipmentSlot.LEGS), cooldownTicks);
      }
    }
  }
}
