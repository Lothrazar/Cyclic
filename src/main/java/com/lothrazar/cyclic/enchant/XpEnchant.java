package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;

public class XpEnchant {

  public static final String ID = "experience_boost";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void handleBlockBreakEvent(BlockDropsEvent event) {
    if (!isEnabled()) {return;}
    if (!(event.getBreaker() instanceof Player player)){ return;}
    int level = EnchantUtil.getCurrentLevelTool(
        EnchantUtil.holder(EnchantRegistry.EXPERIENCE_BOOST, player), player.getMainHandItem());
    if (level <= 0 || event.getDroppedExperience() <= 0){ return;}
    event.setDroppedExperience(event.getDroppedExperience() + getRandomExpAmount(level, player.level()));
  }

  @SubscribeEvent
  public void handleEntityDropEvent(LivingExperienceDropEvent event) {
    if (!isEnabled()) { return; }
    if (event.getAttackingPlayer() == null) { return; }
    int level = EnchantUtil.getCurrentLevelTool(
        EnchantUtil.holder(EnchantRegistry.EXPERIENCE_BOOST, event.getAttackingPlayer()), event.getAttackingPlayer().getMainHandItem());
    if (level <= 0) { return; }
    event.setDroppedExperience(event.getDroppedExperience() + getRandomExpAmount(level, event.getAttackingPlayer().level()));
  }

  private int getRandomExpAmount(int level, Level world) {
    return world.random.nextInt(3) * (level + 1); // max level is 3
  }
}
