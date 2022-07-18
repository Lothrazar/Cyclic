package com.lothrazar.cyclic.item.lunchbox;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityLunchbox implements ICapabilitySerializable<CompoundTag> {

  ItemStackHandler invo = new ItemStackHandler(ItemLunchbox.SLOTS) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.isEdible() && super.isItemValid(slot, stack);
    }
  };
  private final LazyOptional<ItemStackHandler> inventoryCap = LazyOptional.of(() -> invo);

  public CapabilityLunchbox(ItemStack stack, CompoundTag nbt) {
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
  public CompoundTag serializeNBT() {
    return invo.serializeNBT();
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    invo.deserializeNBT(nbt);
  }
}
