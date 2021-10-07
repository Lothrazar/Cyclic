package com.lothrazar.cyclic.block.enderitemshelf;

import com.lothrazar.cyclic.net.PacketTileInventoryToClient;
import com.lothrazar.cyclic.net.PacketTileInventoryToClient.SyncPacketType;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class ClientAutoSyncItemHandler extends ItemStackHandler {

  private BlockEntity shelf;

  public ClientAutoSyncItemHandler(BlockEntity shelf, int rows) {
    super(rows);
    this.shelf = shelf;
  }

  public ItemStack emptySlot(int slot) {
    ItemStack returnStack = this.getStackInSlot(slot);
    this.stacks.set(slot, ItemStack.EMPTY);
    return returnStack;
  }

  @Override
  public ItemStack extractItem(int slot, int amount, boolean simulate) {
    ItemStack extracted = super.extractItem(slot, amount, simulate);
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getLevel(), new PacketTileInventoryToClient(shelf.getBlockPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    return extracted;
  }

  @Override
  public int getStackLimit(int slot, ItemStack stack) {
    return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
  }

  @Override
  public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
    if (!this.isItemValid(slot, stack)) {
      return stack; // not valid
    }
    ItemStack remaining = super.insertItem(slot, stack, simulate);
    if (!simulate) {
      PacketRegistry.sendToAllClients(shelf.getLevel(), new PacketTileInventoryToClient(shelf.getBlockPos(), slot, getStackInSlot(slot), SyncPacketType.SET));
    }
    return remaining;
  }
}
