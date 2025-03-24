package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.neoforged.neoforge.items.ItemStackHandler;

public class CapabilityProviderFilterCard implements ICapabilitySerializable<CompoundTag> {

  public static final int SLOTS = 9;
  private final ItemStackHandler inventory =  new ItemStackHandler(SLOTS) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  public CapabilityProviderFilterCard() {
    // is this needed?
  }

//  @Override
//  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
//    if (cap == ForgeCapabilities.ITEM_HANDLER) {
//      return inventory.cast();
//    }
//    return LazyOptional.empty();
//  }
//
//  @Override
//  public CompoundTag serializeNBT() {
//    if (inventory.isPresent()) {
//      return inventory.resolve().get().serializeNBT();
//    }
//    return new CompoundTag();
//  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
