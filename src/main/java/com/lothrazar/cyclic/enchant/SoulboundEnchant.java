package com.lothrazar.cyclic.enchant;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import com.lothrazar.cyclic.registry.EnchantRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.item.ItemEntity;
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
  //NBT key inside the player's persistent data; survives death and server restarts
  private static final String NBT_KEY = "cyclic_soulbound_save";
  private static final String NBT_ITEM = "item";
  private static final String NBT_SLOT = "slot";
  //temporary slot hint: populated in LivingDeathEvent (inventory intact) and consumed in LivingDropsEvent
  //slots: 0-35 = main inventory (0-8 = hotbar), 36-39 = armor, 40 = offhand
  private static final Map<UUID, Integer> SLOT_HINT = new HashMap<>();

  public static BooleanValue CFG;

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
   * Fires BEFORE inventory is cleared for drops - use this to record the slot.
   * Slots 0-35 are main inventory (hotbar = 0-8), 36-39 are armor, 40 is offhand.
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
    Inventory inv = player.getInventory();
    //check main inventory (0-35)
    for (int i = 0; i < inv.getContainerSize(); i++) {
      if (EnchantmentHelper.getItemEnchantmentLevel(holder, inv.getItem(i)) > 0) {
        SLOT_HINT.put(player.getUUID(), i);
        return;
      }
    }
    //check armor slots (36-39)
    for (int i = 0; i < inv.armor.size(); i++) {
      if (EnchantmentHelper.getItemEnchantmentLevel(holder, inv.armor.get(i)) > 0) {
        SLOT_HINT.put(player.getUUID(), 36 + i);
        return;
      }
    }
    //check offhand (40)
    if (!inv.offhand.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(holder, inv.offhand.get(0)) > 0) {
      SLOT_HINT.put(player.getUUID(), 40);
    }
  }

  /**
   * Fires AFTER inventory has been cleared into drops. Find the soulbound item drop,
   * pull slot hint from the temp map, and persist both to player NBT.
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
    if (holder == null) {
      SLOT_HINT.remove(player.getUUID());
      return;
    }
    Iterator<ItemEntity> it = event.getDrops().iterator();
    while (it.hasNext()) {
      ItemEntity drop = it.next();
      ItemStack stack = drop.getItem();
      if (stack.isEmpty()) {
        continue;
      }
      if (EnchantmentHelper.getItemEnchantmentLevel(holder, stack) > 0) {
        HolderLookup.Provider provider = player.level().registryAccess();
        Tag itemTag = stack.copy().save(provider);
        if (itemTag instanceof CompoundTag itemCtag) {
          int slot = SLOT_HINT.getOrDefault(player.getUUID(), -1);
          CompoundTag save = new CompoundTag();
          save.put(NBT_ITEM, itemCtag);
          save.putInt(NBT_SLOT, slot);
          player.getPersistentData().put(NBT_KEY, save);
          it.remove();
        }
        //cap = one item; always clean up hint
        SLOT_HINT.remove(player.getUUID());
        return;
      }
    }
    //no soulbound drop found; clean up hint regardless
    SLOT_HINT.remove(player.getUUID());
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
    CompoundTag save = pdata.getCompound(NBT_KEY);
    //consume so it can never be restored twice
    pdata.remove(NBT_KEY);
    HolderLookup.Provider provider = respawned.level().registryAccess();
    ItemStack saved = ItemStack.parse(provider, save.get(NBT_ITEM)).orElse(ItemStack.EMPTY);
    if (saved.isEmpty()) {
      return;
    }
    int slot = save.getInt(NBT_SLOT);
    //try to restore to original slot first
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
        return;
      }
    }
    //fallback: first available slot
    if (!respawned.getInventory().add(saved)) {
      respawned.drop(saved, false);
    }
  }
}
