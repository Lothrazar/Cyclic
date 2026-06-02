package com.lothrazar.cyclic.enchant;

import com.lothrazar.cyclic.registry.EnchantRegistry;
import com.lothrazar.library.core.Const;
import com.lothrazar.library.util.EnchantUtil;
import com.lothrazar.library.packet.PacketPlayerFalldamage;
import com.lothrazar.library.util.EntityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static com.lothrazar.cyclic.registry.AttachmentRegistry.LAUNCH_USES;

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

    if (p.onGround()) {
      p.setData(LAUNCH_USES, 0);
    }
  }

  // Called from ClientInputEventHandler. Static because the caller can't go through the registry anymore
  // (in 1.21 EnchantRegistry.LAUNCH is a ResourceKey<Enchantment>, not a DeferredHolder<…, MultiJumpEnchant>).
  public static void onKeyInput(Player player) {
    if (player == null || player.getVehicle() instanceof Boat) { return; }
    if (player.onGround() || player.isCrouching() || player.isInWater()) return;
    if (Minecraft.getInstance().screen != null) return;

    Holder<Enchantment> h = EnchantUtil.holder(EnchantRegistry.LAUNCH, player);
    ItemStack feet = EnchantUtil.getFirstArmorStackWithEnchant(h, player);
    int level = feet.getEnchantmentLevel(h);
    if (level <= 0 || player.getCooldowns().isOnCooldown(feet.getItem())) return;

    int jumpsUsed = player.getData(LAUNCH_USES) + 1;
    if (jumpsUsed >= level) {
      if (!feet.isEmpty()) EntityUtil.setCooldownItem(player, feet.getItem(), COOLDOWN);
    }

    float angle = (player.getDeltaMovement().x == 0 && player.getDeltaMovement().z == 0) ? 90 : ROTATIONPITCH;
    EntityUtil.launch(player, angle, POWER);
    player.setData(LAUNCH_USES, jumpsUsed);

    player.fallDistance = 0;
    PacketDistributor.sendToServer(new PacketPlayerFalldamage());
  }
}
