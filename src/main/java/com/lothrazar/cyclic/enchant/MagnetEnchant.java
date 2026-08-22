package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.compat.botania.BotaniaWrapper;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class MagnetEnchant {

  public static final String ID = "magnet";
  public static BooleanValue CFG;
  public static IntValue H_RADIUS_BASE;
  public static IntValue H_RADIUS_PER_LEVEL;
  public static IntValue V_RADIUS;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  private static int hRadiusBase() {
    return H_RADIUS_BASE == null ? 4 : H_RADIUS_BASE.get();
  }

  private static int hRadiusPerLevel() {
    return H_RADIUS_PER_LEVEL == null ? 4 : H_RADIUS_PER_LEVEL.get();
  }

  private static int vRadius() {
    return V_RADIUS == null ? 4 : V_RADIUS.get();
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) {
      return;
    }
    if (!(event.getEntity() instanceof LivingEntity entity)) {
      return;
    }
    if (entity instanceof Player p && p.isSpectator()) {
      return;
    }
    int level = EnchantUtil.getLevelAll(EnchantUtil.holder(EnchantRegistry.MAGNET, entity), entity);
    if (level > 0 && !BotaniaWrapper.hasSolegnoliaAround(entity)) {
      EntityUtil.moveEntityItemsInRegion(entity.level(), entity.blockPosition(), hRadiusBase() + hRadiusPerLevel() * level, vRadius());
    }
  }
}
