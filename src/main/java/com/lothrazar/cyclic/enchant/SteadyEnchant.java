package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingKnockBackEvent;

public class SteadyEnchant {

  public static final String ID = "steady";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onLivingKnockBackEvent(LivingKnockBackEvent event) {
    if (!isEnabled()) { return; }
    int level = EnchantUtil.getCurrentArmorLevel(EnchantRegistry.holder(EnchantRegistry.STEADY, event.getEntity()), event.getEntity());
    if (level > 0) {
      event.setCanceled(true);
    }
  }
}
