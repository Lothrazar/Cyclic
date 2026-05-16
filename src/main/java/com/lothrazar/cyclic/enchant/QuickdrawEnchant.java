package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

public class QuickdrawEnchant {

  public static final String ID = "quickshot";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onPlayerUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) { return; }
    if (!(event.getEntity() instanceof Player player)) { return; }
    if (!player.isUsingItem()) { return; }
    InteractionHand hand = player.getUsedItemHand();
    if (hand == null) { return; }
    ItemStack heldItem = player.getItemInHand(hand);
    if (!(heldItem.getItem() instanceof BowItem)) { return; }
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.QUICKDRAW, player), heldItem);
    if (level <= 0) { return; }
    for (int i = 0; i < level; i++) {
      player.updatingUsingItem();
    }
  }
}
