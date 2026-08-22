package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.AttributesUtil;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class StepEnchant {

  private static final String NBT_ON = ModCyclic.MODID + "_stepenchant";
  public static final String ID = "step";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) {
      return;
    }
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.STEP, player);
    int level = EnchantUtil.getCurrentArmorLevelSlot(h, player, EquipmentSlot.LEGS);
    if (level > 0) {
      player.getPersistentData().putBoolean(NBT_ON, true);
      AttributesUtil.enableStepHeight(player);
    }
    else {
      if (player.getPersistentData().getBooleanOr(NBT_ON, false)) {
        AttributesUtil.disableStepHeight(player);
        player.getPersistentData().putBoolean(NBT_ON, false);
      }
    }
  }
}
