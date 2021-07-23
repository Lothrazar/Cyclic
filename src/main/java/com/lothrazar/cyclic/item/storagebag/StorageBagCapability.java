package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StorageBagCapability implements ICapabilitySerializable<CompoundNBT> {

  ItemStackHandler invo = new ItemStackHandler(ItemStorageBag.SLOTS) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return !(stack.getItem() instanceof ItemStorageBag) && super.isItemValid(slot, stack);
    }
  };
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> invo);

  public StorageBagCapability(ItemStack stack, CompoundNBT nbt) {
    //
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundNBT serializeNBT() {
    return invo.serializeNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    invo.deserializeNBT(nbt);
  }
}
