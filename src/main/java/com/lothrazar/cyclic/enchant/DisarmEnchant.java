package com.lothrazar.cyclic.enchant;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.StringParseUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

public class DisarmEnchant {

  //halves the desired base chance to activate because onEntityDamage gets called twice and it's currently a
  // "won't fix" situation for Forge https://github.com/MinecraftForge/MinecraftForge/issues/6556#issuecomment-596441220
  public static IntValue PERCENTPERLEVEL;
  public static BooleanValue CFG;
  public static final String ID = "disarm";

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  public double getChanceToDisarm(int level) {
    float baseChance = (PERCENTPERLEVEL == null ? 15 : PERCENTPERLEVEL.get()) / 100F;
    return baseChance + (baseChance * (level - 1));
  }

  @SubscribeEvent
  public void onAttackEntity(AttackEntityEvent event) {
    if (!isEnabled()) { return; }
    if (!(event.getTarget() instanceof LivingEntity livingTarget)) { return; }
    LivingEntity user = event.getEntity();
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.DISARM, user), user);
    if (level <= 0) { return; }
    if (!canDisarm(livingTarget)) { return; }
    List<ItemStack> toDisarm = new ArrayList<>();
    livingTarget.getHandSlots().forEach(itemStack -> {
      if (!(itemStack.getItem() instanceof SwordItem)) { return; }
      if (getChanceToDisarm(level) > user.level().random.nextDouble()) {
        toDisarm.add(itemStack);
      }
    });
    toDisarm.forEach(itemStack -> {
      boolean dropHeld = false;
      if (itemStack.equals(livingTarget.getMainHandItem())) {
        livingTarget.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      else if (itemStack.equals(livingTarget.getOffhandItem())) {
        livingTarget.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
        dropHeld = true;
      }
      if (dropHeld) {
        user.level().addFreshEntity(new ItemEntity(user.level(), livingTarget.getX(), livingTarget.getY(), livingTarget.getZ(), itemStack));
      }
    });
  }

  private boolean canDisarm(LivingEntity target) {
    String id = EntityType.getKey(target.getType()).toString();
    if (StringParseUtil.isInList(ConfigRegistry.getDisarmIgnoreList(), EntityType.getKey(target.getType()))) {
      ModCyclic.LOGGER.info("disenchant ignored by: CONFIG LIST" + id);
      return false;
    }
    return true;
  }
}
