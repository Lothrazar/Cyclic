package com.lothrazar.cyclic.item.crafting;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

public class CraftingBagCapabilityProvider implements ICapabilitySerializable<CompoundTag> {

  private final int slots = 9;
  private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(slots) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return !(stack.getItem() instanceof CraftingBagItem) && super.isItemValid(slot, stack);
    }
  });

  public CraftingBagCapabilityProvider() {
    // is this needed?
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventory.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundTag serializeNBT() {
    if (inventory.isPresent()) {
      return inventory.resolve().get().serializeNBT();
    }
    return new CompoundTag();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
