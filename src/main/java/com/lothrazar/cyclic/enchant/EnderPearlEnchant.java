package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class EnderPearlEnchant {

  public static final int COOLDOWN = 6 * Const.TICKS_PER_SEC;
  private static final float VELOCITY = 1.5F;
  private static final float INNACCURACY = 1F;
  public static final String ID = "ender";
  public static BooleanValue CFG;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
    if (!isEnabled()) { return; }
    Level world = event.getLevel();
    if (world.isClientSide() || event.isCanceled()) { return; }
    ItemStack stack = event.getItemStack();
    int level = EnchantUtil.getCurrentLevelTool(EnchantUtil.holder(EnchantRegistry.PEARL, world), stack);
    if (level <= 0) { return; }
    int adjustedCooldown = COOLDOWN / level;
    Player player = event.getEntity();
    if (player.getCooldowns().isOnCooldown(stack.getItem())) { return; }
    ThrownEnderpearl pearl = new ThrownEnderpearl(world, player);
    Vec3 lookVector = player.getLookAngle();
    pearl.shoot(lookVector.x(), lookVector.y(), lookVector.z(), VELOCITY, INNACCURACY);
    EntityUtil.setCooldownItem(player, stack.getItem(), adjustedCooldown);
    SoundUtil.playSound(player, SoundEvents.ENDER_PEARL_THROW, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
    world.addFreshEntity(pearl);
    event.setCancellationResult(InteractionResult.SUCCESS);
    event.setCanceled(true);
  }
}
