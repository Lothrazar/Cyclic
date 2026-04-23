package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient.SyncPacketType;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.library.util.EnchantUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.items.ItemStackHandler;

public class EnderShelfItemHandler extends ItemStackHandler {

  public static ModConfigSpec.IntValue BOOKS_PER_ROW;
  public static final int ROWS = 5;
  public TileEnderShelf shelf;
  String[] nameCache = new String[ROWS];
  String[] enchantmentIdCache = new String[ROWS];
  int[] extraBooks = new int[ROWS];

  public void resetNameCache() {
    nameCache = new String[5];
  }

  public EnderShelfItemHandler(TileEnderShelf shelf) {
    super(ROWS);
    this.shelf = shelf;
  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    CompoundTag tag = super.serializeNBT(provider);
    for (int i = 0; i < ROWS; i++) {
      tag.putInt("cyclicmagic" + ":idc" + i, extraBooks[i]);
      if (enchantmentIdCache[i] == null) {
        enchantmentIdCache[i] = "";
      }
      tag.putString("cyclicmagic" + ":ench", enchantmentIdCache[i]);
    }
    return tag;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
    super.deserializeNBT(provider,nbt);
    for (int i = 0; i < ROWS; i++) {
      extraBooks[i] = nbt.getInt("cyclicmagic" + ":idc" + i);
      enchantmentIdCache[i] = nbt.getString("cyclicmagic" + ":ench");
    }
  }

  public ItemStack emptySlot(int slot) {
    ItemStack returnStack = this.getStackInSlot(slot);
    this.stacks.set(slot, ItemStack.EMPTY);
    return returnStack;
  }

  @Override
  public int getStackLimit(int slot, ItemStack stack) {
    return BOOKS_PER_ROW.get();
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (this.shelf.getLevel() == null) {
      return ItemStack.EMPTY;
    }
    boolean oldEmpty = stacks.get(slot).isEmpty();
    ItemStack extracted = super.extractItem(slot, amount, simulate);
    boolean newEmpty = stacks.get(slot).isEmpty();
    if (oldEmpty != newEmpty) {
      this.refreshId(slot);
    }
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getLevel(), new PacketTileInventoryToClient(shelf.getBlockPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    if (this.shelf.getLevel().isClientSide && oldEmpty != stacks.get(slot).isEmpty()) {
      nameCache[slot] = "";
    }
    return extracted;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!this.isItemValid(slot, stack)) {
      return stack; // not valid
    }
    boolean oldEmpty = stacks.get(slot).isEmpty();
    ItemStack remaining = super.insertItem(slot, stack, simulate);
    boolean newEmpty = stacks.get(slot).isEmpty();
    if (oldEmpty != newEmpty) {
      this.refreshId(slot);
    }
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getLevel(), new PacketTileInventoryToClient(shelf.getBlockPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    return remaining;
  }

  private void refreshId(int slot) {
    ItemStack stackIn = this.getStackInSlot(slot);
    if (stackIn.isEmpty()) {
      this.enchantmentIdCache[slot] = "";
    }
    else {
      net.minecraft.world.item.enchantment.ItemEnchantments chantsIn = stackIn.getOrDefault(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
      if (!chantsIn.isEmpty()) {
        var entry = chantsIn.entrySet().iterator().next();
        this.enchantmentIdCache[slot] = entry.getKey().unwrapKey().map(k -> k.location().toString()).orElse("");
        nameCache[slot] = net.minecraft.world.item.enchantment.Enchantment.getFullname(entry.getKey(), entry.getIntValue()).getString();
      }
    }
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stackIn) {
    if (stackIn.getItem() != Items.ENCHANTED_BOOK) {
      return false;
    }
    net.minecraft.world.item.enchantment.ItemEnchantments chantsIn = stackIn.getOrDefault(net.minecraft.core.component.DataComponents.STORED_ENCHANTMENTS, net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
    if (chantsIn.size() != 1) {
      return false;
    }
    //item must be non empty enchanted book with exactly one
    boolean oldEmpty = this.getStackInSlot(slot).isEmpty();
    if (oldEmpty) {
      return true; //yes it can go here
    }
    //target slot is also not empty, enchants must match
    ItemStack stackHere = this.getStackInSlot(slot);
    //
    if (this.enchantmentIdCache[slot] != null && !this.enchantmentIdCache[slot].isEmpty()) {
      var entry = chantsIn.entrySet().iterator().next();
      boolean match = this.enchantmentIdCache[slot].equals(entry.getKey().unwrapKey().map(k -> k.location().toString()).orElse(""));
      return match;
    }
    //else no cache, old way
    return EnchantUtil.doBookEnchantmentsMatch(stackIn, stackHere);
  }
}
