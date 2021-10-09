package com.lothrazar.cyclic.item.enderbook;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityProviderEnderBook implements ICapabilitySerializable<CompoundTag> {

  public static final int SLOTS = 9;
  private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(SLOTS) {

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  });

  public CapabilityProviderEnderBook() {}

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    if (inventory.isPresent()) {
      CompoundTag nbt = inventory.resolve().get().serializeNBT();
      return nbt;
    }
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
