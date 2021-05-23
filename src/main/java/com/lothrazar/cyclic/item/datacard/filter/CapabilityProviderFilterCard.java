package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityProviderFilterCard implements ICapabilitySerializable<CompoundNBT> {

  public static final int SLOTS = 9;
  private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(SLOTS) {
    //    @Override
    //    public boolean isItemValid(int slot, ItemStack stack) {
    //      return !(stack.getItem() instanceof FilterCardItem) && super.isItemValid(slot, stack);
    //    }
  });

  public CapabilityProviderFilterCard() {
    // is this needed?
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundNBT serializeNBT() {
    if (inventory.isPresent()) {
      CompoundNBT nbt = inventory.resolve().get().serializeNBT();
      return nbt;
    }
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
