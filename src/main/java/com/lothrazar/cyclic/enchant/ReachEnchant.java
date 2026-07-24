package com.lothrazar.cyclic.enchant;

import java.util.UUID;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.AttributesUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class ReachEnchant {

  private static final String NBT_REACH_ON = "reachon";
  public static final String ID = "reach";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  final static int[] LEVELS = { 0, 4, 7, 11, 16, 19 };

  private int getBoost(int level) {
    if (level < 0) { return 0; }
    else if (level < LEVELS.length) { return LEVELS[level]; }
    int oldLevel = LEVELS.length - 1;
    return 4 * (level - oldLevel) + LEVELS[oldLevel];
  }

  public static final Identifier ENCHANTMENT_REACH_ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, ID);

  private void turnReachOff(Player player) {
    player.getPersistentData().putBoolean(NBT_REACH_ON, false);
    AttributesUtil.removePlayerReach(ENCHANTMENT_REACH_ID, player);
  }

  private void turnReachOn(Player player, int level) {
    player.getPersistentData().putBoolean(NBT_REACH_ON, true);
    AttributesUtil.setPlayerReach(ENCHANTMENT_REACH_ID, player, getBoost(level));
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) { return; }
    if (!(event.getEntity() instanceof Player player)) { return; }
    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.REACH, player);
    int level = EnchantUtil.getCurrentArmorLevel(h, player);
    if (level > 0) {
      turnReachOn(player, level);
    }
    else {
      if (player.getPersistentData().contains(NBT_REACH_ON) && player.getPersistentData().getBoolean(NBT_REACH_ON)) {
        turnReachOff(player);
      }
    }
  }
}
