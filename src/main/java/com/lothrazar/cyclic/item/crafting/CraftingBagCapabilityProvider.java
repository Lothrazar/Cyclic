package com.lothrazar.cyclic.item.crafting;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CraftingBagCapabilityProvider implements ICapabilitySerializable<CompoundNBT> {

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
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return LazyOptional.empty();
  }

  @Override
  public CompoundNBT serializeNBT() {
    return inventory.map(ItemStackHandler::serializeNBT).orElse(new CompoundNBT());
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    inventory.ifPresent(h -> h.deserializeNBT(nbt));
  }
}
