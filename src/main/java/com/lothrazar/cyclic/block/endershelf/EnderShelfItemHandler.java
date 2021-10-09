package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient.SyncPacketType;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.items.ItemStackHandler;

public class EnderShelfItemHandler extends ItemStackHandler {

  public static IntValue BOOKS_PER_ROW;
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
  public CompoundTag serializeNBT() {
    CompoundTag tag = super.serializeNBT();
    for (int i = 0; i < ROWS; i++) {
      tag.putInt(Const.MODID + ":idc" + i, extraBooks[i]);
      if (enchantmentIdCache[i] == null) {
        enchantmentIdCache[i] = "";
      }
      tag.putString(Const.MODID + ":oh yeah the enchantmentIdCacheench", enchantmentIdCache[i]);
    }
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    super.deserializeNBT(nbt);
    for (int i = 0; i < ROWS; i++) {
      extraBooks[i] = nbt.getInt(Const.MODID + ":idc" + i);
      enchantmentIdCache[i] = nbt.getString(Const.MODID + ":ench");
    }
  }

  public ItemStack emptySlot(int slot) {
    ItemStack returnStack = this.getStackInSlot(slot);
    this.stacks.set(slot, ItemStack.EMPTY);
    return returnStack;
  }

  @Override
  public int getStackLimit(int slot, ItemStack stack) {
    return BOOKS_PER_ROW.get(); // Math.min(getSlotLimit(slot), stack.getMaxStackSize());
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (this.shelf.getLevel() == null) {
      return ItemStack.EMPTY;
    }
    boolean oldEmpty = stacks.get(slot).isEmpty();
    ItemStack extracted = super.extractItem(slot, amount, simulate);
    boolean newEmpty = stacks.get(slot).isEmpty();
    //    if (extracted.getCount() < amount) {
    //      int rem = amount - extracted.getCount();
    //      ModCyclic.LOGGER.info("EXTRACT: still some rem" + rem);
    //    }
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
    //    if (!remaining.isEmpty()) {
    //      ModCyclic.LOGGER.info("still some left on insert" + remaining);
    //    }
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
      ListTag chantsIn = EnchantedBookItem.getEnchantments(stackIn);
      this.enchantmentIdCache[slot] = ((CompoundTag) chantsIn.get(0)).getString("id");
      Map<Enchantment, Integer> enchantments = EnchantmentHelper.deserializeEnchantments(chantsIn);
      for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
        nameCache[slot] = entry.getKey().getFullname(entry.getValue()).getString();
        break;
      }
    }
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stackIn) {
    if (stackIn.getItem() != Items.ENCHANTED_BOOK) {
      return false;
    }
    ListTag chantsIn = EnchantedBookItem.getEnchantments(stackIn);
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
    if (this.enchantmentIdCache[slot] != null || !this.enchantmentIdCache[slot].isEmpty()) {
      //      ModCyclic.LOGGER.info("match on id cache");
      boolean match = this.enchantmentIdCache[slot].equals(((CompoundTag) chantsIn.get(0)).getString("id"));
      return match;
    }
    //else no cache, old way
    return UtilEnchant.doBookEnchantmentsMatch(stackIn, stackHere);
  }
}
