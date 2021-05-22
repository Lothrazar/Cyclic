package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.net.PacketTileInventory;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilEnchant;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemStackHandler;

public class EnderShelfItemHandler extends ItemStackHandler {

  public TileEnderShelf shelf;

  public EnderShelfItemHandler(TileEnderShelf shelf) {
    super(5);
    this.shelf = shelf;
  }

  public ItemStack emptySlot(int slot) {
    ItemStack returnStack = this.getStackInSlot(slot);
    this.stacks.set(slot, ItemStack.EMPTY);
    return returnStack;
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (this.shelf.getWorld() == null) {
      return ItemStack.EMPTY;
    }
    ItemStack extracted = super.extractItem(slot, amount, simulate);
    if (!this.shelf.getWorld().isRemote && !simulate) {
      PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(this.shelf.getPos(), slot, this.getStackInSlot(slot), PacketTileInventory.TYPE.SET));
    }
    return extracted;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return stack.getItem() == Items.ENCHANTED_BOOK &&
        EnchantedBookItem.getEnchantments(stack).size() == 1 &&
        (this.getStackInSlot(slot).isEmpty() || UtilEnchant.doBookEnchantmentsMatch(stack, this.getStackInSlot(slot)));
  }

  @Override
  public int getStackLimit(int slot, ItemStack stack) {
    return 64;
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (shelf == null || shelf.getWorld() == null) {
      return stack;
    }
    ItemStack remaining = super.insertItem(slot, stack, simulate);
    if (!this.shelf.getWorld().isRemote && !simulate) {
      PacketRegistry.sendToAllClients(this.shelf.getWorld(), new PacketTileInventory(this.shelf.getPos(), slot, this.getStackInSlot(slot), PacketTileInventory.TYPE.SET));
    }
    return remaining;
  }

  public int firstSlotWithItem() {
    for (int i = 0; i < this.getSlots(); i++) {
      if (!this.getStackInSlot(i).isEmpty()) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT nbt = super.serializeNBT();
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    super.deserializeNBT(nbt);
  }
}
