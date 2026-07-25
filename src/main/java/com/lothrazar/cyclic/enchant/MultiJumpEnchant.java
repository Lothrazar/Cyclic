package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import com.lothrazar.library.util.EntityUtil;
import com.lothrazar.library.util.ParticleUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class MultiJumpEnchant {

  public static final String ID = "launch";
  public static final int COOLDOWN = 7 * Const.TICKS_PER_SEC;
  public static final float POWER = 1.07F;
  public static final int ROTATIONPITCH = 68;
  public static BooleanValue CFG;
  private static final String NBT_USES = "launchuses";

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  @SubscribeEvent
  public void onEntityUpdate(EntityTickEvent.Pre event) {
    if (!(event.getEntity() instanceof Player p)) { return; }
    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.LAUNCH, p);
    ItemStack armorStack = EnchantUtil.getFirstArmorStackWithEnchant(h, p);
    if (armorStack.isEmpty()) { return; }
    if ((p.hasImpulse == false || p.onGround()) && armorStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt(NBT_USES) > 0) {
      armorStack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, d -> { CompoundTag t = d.copyTag(); t.putInt(NBT_USES, 0); return CustomData.of(t); });
    }
  }

  // Called from ClientInputEventHandler. Static because the caller can't go through the registry anymore
  // (in 1.21 EnchantRegistry.LAUNCH is a ResourceKey<Enchantment>, not a DeferredHolder<…, MultiJumpEnchant>).
  public static void onKeyInput(Player player) {
    if (player == null || player.getVehicle() instanceof Boat) { return; }
    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.LAUNCH, player);
    ItemStack feet = EnchantUtil.getFirstArmorStackWithEnchant(h, player);
    if (feet.isEmpty() || player.isCrouching()) { return; }
    int level = EnchantUtil.getCurrentLevelTool(h, feet);
    if (level <= 0) { return; }
    if (player.getCooldowns().isOnCooldown(feet)) { return; }
    if (Minecraft.getInstance().options.keyJump.isDown()
        && player.getY() < player.yOld && player.hasImpulse && !player.isInWater()) {
      int uses = feet.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBT_USES, 0);
      player.fallDistance = 0;
      float angle = (player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) ? 90 : ROTATIONPITCH;
      EntityUtil.launch(player, angle, POWER);
      ParticleUtil.spawnParticle(player.getCommandSenderWorld(), ParticleTypes.CRIT, player.blockPosition(), 7);
      uses++;
      if (uses >= level) {
        if (!feet.isEmpty()) EntityUtil.setCooldownItem(player, feet.getItem(), COOLDOWN);
        uses = 0;
      }
      final int finalUses = uses;
      feet.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, d -> { CompoundTag t = d.copyTag(); t.putInt(NBT_USES, finalUses); return CustomData.of(t); });
      player.fallDistance = 0;
      ClientPacketDistributor.sendToServer(new PacketPlayerFalldamage());
    }
  }
}
