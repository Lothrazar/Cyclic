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
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
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
    // 26.1: Inventory#armor/#offhand fields are gone - equipment now lives on the entity itself, keyed by
    // EquipmentSlot. Inventory.EQUIPMENT_SLOT_MAPPING (public) maps slots 36-39 to armor, 40 to offhand.
    for (int i = 36; i <= 40; i++) {
      EquipmentSlot slot = Inventory.EQUIPMENT_SLOT_MAPPING.get(i);
      if (slot != null) {
        addIfSoulbound(holder, player.getItemBySlot(slot), i, provider, pending);
      }
    }
    if (!pending.isEmpty()) {
      PENDING.put(player.getUUID(), pending);
    }
  }

  private static void addIfSoulbound(Holder<Enchantment> holder, ItemStack stack, int slot, HolderLookup.Provider provider, ListTag out) {
    if (stack.isEmpty() || EnchantmentHelper.getItemEnchantmentLevel(holder, stack) <= 0) {
      return;
    }
    // 26.1: ItemStack#save(Provider) removed - purely Codec-based now, bridged to raw Tag storage
    Tag itemTag = ItemStack.CODEC.encodeStart(provider.createSerializationContext(NbtOps.INSTANCE), stack.copy()).result().orElse(null);
    if (itemTag == null) {
      return;
    }
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
      // 26.1: ItemStack.parse(Provider, Tag) removed - purely Codec-based now
      ItemStack saved = ItemStack.CODEC.parse(provider.createSerializationContext(NbtOps.INSTANCE), entry.get(NBT_ITEM)).result().orElse(ItemStack.EMPTY);
      if (saved.isEmpty()) {
        continue;
      }
      int durabilityCost = DURABILITY_COST == null ? 5 : DURABILITY_COST.get();
      if (durabilityCost > 0 && saved.isDamageableItem() && !respawned.isCreative()) {
        saved.setDamageValue(Math.min(saved.getDamageValue() + durabilityCost, saved.getMaxDamage() - 1));
      }
      int slot = entry.getIntOr(NBT_SLOT, 0);
      if (slot >= 0) {
        Inventory inv = respawned.getInventory();
        // 26.1: Inventory#armor/#offhand fields are gone - use Inventory.EQUIPMENT_SLOT_MAPPING + the
        // entity's own getItemBySlot/setItemSlot for slots 36-40 (armor + offhand), same as onLivingDeath above.
        EquipmentSlot eqSlot = slot >= 36 && slot <= 40 ? Inventory.EQUIPMENT_SLOT_MAPPING.get(slot) : null;
        boolean canUseSlot = slot < inv.getContainerSize()
            ? inv.getItem(slot).isEmpty()
            : eqSlot != null && respawned.getItemBySlot(eqSlot).isEmpty();
        if (canUseSlot) {
          if (slot < inv.getContainerSize()) {
            inv.setItem(slot, saved);
          }
          else if (eqSlot != null) {
            respawned.setItemSlot(eqSlot, saved);
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
