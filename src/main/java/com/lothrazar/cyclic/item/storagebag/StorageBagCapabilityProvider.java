package com.lothrazar.cyclic.item.storagebag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class StorageBagCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

  private int slots;
  private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(slots) {

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
      return !(stack.getItem() instanceof StorageBagItem) && super.isItemValid(slot, stack);
    }
  });

  public StorageBagCapabilityProvider(ItemStack stack, int slots) {
    //    this.bag = stack;
    this.slots = slots;
  }

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundNBT serializeNBT() {
    if (inventory.isPresent()) {
      return inventory.resolve().get().serializeNBT();
    }
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
