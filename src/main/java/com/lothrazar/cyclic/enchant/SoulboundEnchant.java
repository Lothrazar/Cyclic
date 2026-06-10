package com.lothrazar.cyclic.enchant;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.BooleanValue;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class SoulboundEnchant {

  public static final String ID = "soulbound";
  private static final String NBT_KEY = "cyclic_soulbound_save";
  private static final String NBT_ITEM = "item";
  private static final String NBT_SLOT = "slot";
  // Temporary store: populated in LivingDeathEvent (inventory intact), consumed in LivingDropsEvent
  private static final Map<UUID, ListTag> PENDING = new HashMap<>();

  public static BooleanValue CFG;
  public static net.neoforged.neoforge.common.ModConfigSpec.IntValue DURABILITY_COST;

  public static boolean isEnabled() {
    return CFG == null || CFG.get();
  }

  private static Holder<Enchantment> getHolder(Player player) {
    try {
      return player.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(EnchantRegistry.SOULBOUND);
    }
    catch (Exception e) {
      return null;
    }
  }

  /**
   * Fires BEFORE inventory is cleared for drops - record all soulbound items and their slots.
   */
  @SubscribeEvent
  public void onLivingDeath(LivingDeathEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    if (player.level().isClientSide()) {
      return;
    }
    Holder<Enchantment> holder = getHolder(player);
    if (holder == null) {
      return;
    }
    HolderLookup.Provider provider = player.level().registryAccess();
    ListTag pending = new ListTag();
    Inventory inv = player.getInventory();
    for (int i = 0; i < inv.getContainerSize(); i++) {
      addIfSoulbound(holder, inv.getItem(i), i, provider, pending);
    }
    for (int i = 0; i < inv.armor.size(); i++) {
      addIfSoulbound(holder, inv.armor.get(i), 36 + i, provider, pending);
    }
    if (!inv.offhand.isEmpty()) {
      addIfSoulbound(holder, inv.offhand.get(0), 40, provider, pending);
    }
    if (!pending.isEmpty()) {
      PENDING.put(player.getUUID(), pending);
    }
  }

  private static void addIfSoulbound(Holder<Enchantment> holder, ItemStack stack, int slot, HolderLookup.Provider provider, ListTag out) {
    if (stack.isEmpty() || EnchantmentHelper.getItemEnchantmentLevel(holder, stack) <= 0) {
      return;
    }
    Tag itemTag = stack.copy().save(provider);
    if (itemTag instanceof CompoundTag itemCtag) {
      CompoundTag entry = new CompoundTag();
      entry.put(NBT_ITEM, itemCtag);
      entry.putInt(NBT_SLOT, slot);
      out.add(entry);
    }
  }

  /**
   * Fires AFTER inventory has been cleared into drops. Remove all soulbound drops
   * and persist the saved item list (with slot info from onLivingDeath) to player NBT.
   */
  @SubscribeEvent
  public void onPlayerDrops(LivingDropsEvent event) {
    if (!isEnabled()) {
      return;
    }
    if (!(event.getEntity() instanceof Player player)) {
      return;
    }
    if (player.level().isClientSide()) {
      return;
    }
    Holder<Enchantment> holder = getHolder(player);
    ListTag pending = PENDING.remove(player.getUUID());
    if (holder == null || pending == null || pending.isEmpty()) {
      return;
    }
    event.getDrops().removeIf(drop -> {
      ItemStack stack = drop.getItem();
      return !stack.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0;
    });
    player.getPersistentData().put(NBT_KEY, pending);
  }

  @SubscribeEvent
  public void onClone(PlayerEvent.Clone event) {
    if (!event.isWasDeath()) {
      return;
    }
    Player original = event.getOriginal();
    Player respawned = event.getEntity();
    CompoundTag pdata = original.getPersistentData();
    if (!pdata.contains(NBT_KEY)) {
      return;
    }
    Tag savedTag = pdata.get(NBT_KEY);
    pdata.remove(NBT_KEY);
    if (!(savedTag instanceof ListTag savedList)) {
      return;
    }
    HolderLookup.Provider provider = respawned.level().registryAccess();
    for (Tag entryTag : savedList) {
      if (!(entryTag instanceof CompoundTag entry)) {
        continue;
      }
      ItemStack saved = ItemStack.parse(provider, entry.get(NBT_ITEM)).orElse(ItemStack.EMPTY);
      if (saved.isEmpty()) {
        continue;
      }
      int durabilityCost = DURABILITY_COST == null ? 5 : DURABILITY_COST.get();
      if (durabilityCost > 0 && saved.isDamageableItem() && !respawned.isCreative()) {
        saved.setDamageValue(Math.min(saved.getDamageValue() + durabilityCost, saved.getMaxDamage() - 1));
      }
      int slot = entry.getInt(NBT_SLOT);
      if (slot >= 0) {
        Inventory inv = respawned.getInventory();
        boolean canUseSlot = slot < inv.getContainerSize()
            ? inv.getItem(slot).isEmpty()
            : slot < 40 ? inv.armor.get(slot - 36).isEmpty()
            : slot == 40 && inv.offhand.get(0).isEmpty();
        if (canUseSlot) {
          if (slot < inv.getContainerSize()) {
            inv.setItem(slot, saved);
          }
          else if (slot < 40) {
            inv.armor.set(slot - 36, saved);
          }
          else {
            inv.offhand.set(0, saved);
          }
          continue;
        }
      }
      if (!respawned.getInventory().add(saved)) {
        respawned.drop(saved, false);
      }
    }
  }
}
