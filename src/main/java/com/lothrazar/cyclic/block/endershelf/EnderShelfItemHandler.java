package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient.SyncPacketType;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.items.ItemStackHandler;

public class EnderShelfItemHandler extends ItemStackHandler {

  public static IntValue BOOKS_PER_ROW;
  public static final int ROWS = 5;
  public TileEnderShelf shelf;
  String[] nameCache = new String[ROWS];
  int[] extraBooks = new int[ROWS];

  public void resetNameCache() {
    nameCache = new String[5];
  }

  public EnderShelfItemHandler(TileEnderShelf shelf) {
    super(ROWS);
    this.shelf = shelf;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = super.serializeNBT();
    for (int i = 0; i < ROWS; i++) {
      tag.putInt(Const.MODID + ":idc" + i, extraBooks[i]);
    }
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    super.deserializeNBT(nbt);
    for (int i = 0; i < ROWS; i++) {
      extraBooks[i] = nbt.getInt(Const.MODID + ":idc" + i);
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
    if (this.shelf.getWorld() == null) {
      return ItemStack.EMPTY;
    }
    boolean oldEmpty = stacks.get(slot).isEmpty();
    ItemStack extracted = super.extractItem(slot, amount, simulate);
    if (extracted.getCount() < amount) {
      //
      int rem = amount - extracted.getCount();
      ModCyclic.LOGGER.info("EXTRACT: still some rem" + rem);
    }
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getWorld(), new PacketTileInventoryToClient(shelf.getPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    if (this.shelf.getWorld().isRemote && oldEmpty != stacks.get(slot).isEmpty()) {
      nameCache[slot] = "";
    }
    return extracted;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!this.isItemValid(slot, stack)) {
      return stack; // not valid
    }
    ItemStack remaining = super.insertItem(slot, stack, simulate);
    if (!remaining.isEmpty()) {
      ModCyclic.LOGGER.info("still some left on insert" + remaining);
    }
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getWorld(), new PacketTileInventoryToClient(shelf.getPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    return remaining;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stackIn) {
    if (stackIn.getItem() != Items.ENCHANTED_BOOK) {
      return false;
    }
    ListNBT chantsIn = EnchantedBookItem.getEnchantments(stackIn);
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
    //we have exactly one enchantment on incoming stack
    //    if (idCache[slot] > 0) {
    //      //try to match on cache 
    //      //      int hashStackIn = getHashForStack(stackIn);
    //      //      String id = chantsHere.getCompound(0).getString("id");
    //      if (getHashForStack(stackIn) != idCache[slot]) {
    //        //
    //        ModCyclic.LOGGER.error("mismatch cache, block by incoming idc " + EnchantedBookItem.getEnchantments(stackIn).getCompound(0).getString("id") + "vs " + idCache[slot]);
    //        return false;
    //      }
    //    }
    //    ModCyclic.LOGGER.error(slot + " no cache, old way do match " + stackIn.getTag() + " vs " + getStackInSlot(slot).getTag());
    //else no cache, old way
    return UtilEnchant.doBookEnchantmentsMatch(stackIn, stackHere);
  }
}
