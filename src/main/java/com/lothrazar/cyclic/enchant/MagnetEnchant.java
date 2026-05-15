package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.compat.botania.BotaniaWrapper;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class MagnetEnchant {

  private static final int ITEM_HRADIUS = 4;
  private static final int HRADIUS_PER_LEVEL = 4;
  private static final int ITEM_VRADIUS = 4;
  public static final String ID = "magnet";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) { return; }
    if (!(event.getEntity() instanceof LivingEntity entity)) { return; }
    if (entity instanceof Player p && p.isSpectator()) { return; }
    int level = EnchantUtil.getLevelAll(EnchantRegistry.holder(EnchantRegistry.MAGNET, entity), entity);
    if (level > 0 && !BotaniaWrapper.hasSolegnoliaAround(entity)) {
      EntityUtil.moveEntityItemsInRegion(entity.getCommandSenderWorld(), entity.blockPosition(), ITEM_HRADIUS + HRADIUS_PER_LEVEL * level, ITEM_VRADIUS);
    }
  }
}
