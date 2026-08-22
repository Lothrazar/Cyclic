package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.util.GrowthUtil;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.ShapeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Collections;
import java.util.List;

public class GrowthEnchant {

  public static final String ID = "growth";
  public static BooleanValue CFG;
  public static BooleanValue PLAYER_ONLY;
  public static IntValue RADIUS_FACTOR;
  public static IntValue LIMIT_FACTOR;
  public static IntValue HEIGHT;
  public static IntValue ODDS;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }


  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!isEnabled()) {
      return;
    }
    if (!(event.getEntity() instanceof LivingEntity entity)) {
      return;
    }
    if (event.getEntity() instanceof Player player) {
      if (player.isSpectator() || !player.isAlive()) {
        return; // no dead players
      }
    }
    else {
      //entity is NOT a player
      if (PLAYER_ONLY.get()) {
        return; // config says only players are allowed and we are not a player
      }
    }
    //Ticking
    ItemStack held = entity.getItemInHand(InteractionHand.MAIN_HAND);
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.GROWTH, entity.level()), held);
    if (level > 0 && entity.level() instanceof ServerLevel sw) {
      final int growthLimit = level * LIMIT_FACTOR.get() + (entity.level().isRaining() ? 4 : 0); //more when raining too
      final int radius = 1 + level * RADIUS_FACTOR.get();
      final double odds = ((double) ODDS.get()) / 100.0;
      int grown = 0;
      List<BlockPos> shape = ShapeUtil.squareHorizontalFull(entity.blockPosition().below(), radius);
      shape = ShapeUtil.repeatShapeByHeight(shape, HEIGHT.get());
      Collections.shuffle(shape);
      for (int i = 0; i < shape.size(); i++) {
        if (grown >= growthLimit) {
          break;
        }
        if (GrowthUtil.tryGrow(sw, shape.get(i), odds)) {
          grown++;
        }
      }
      if (grown > 0) {
        ItemStackUtil.damageItem(entity, entity.getItemInHand(InteractionHand.MAIN_HAND));
      }
    }
  }
}
