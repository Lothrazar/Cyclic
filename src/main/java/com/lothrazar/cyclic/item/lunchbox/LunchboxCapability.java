package com.lothrazar.cyclic.item.lunchbox;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class LunchboxCapability extends ItemStackHandler {

  private static final String NBT_KEY = "items";
  private final ItemStack boxStack;

  public LunchboxCapability(ItemStack boxStack) {
    super(ItemLunchbox.SLOTS);
    this.boxStack = boxStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = boxStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserializeNBT(server.registryAccess(), data.getCompound(NBT_KEY));
      }
    }
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return stack.has(DataComponents.FOOD) && super.isItemValid(slot, stack);
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server == null) {
      return;
    }
    CompoundTag data = boxStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    data.put(NBT_KEY, serializeNBT(server.registryAccess()));
    int empty = 0;
    for (int i = 0; i < getSlots(); i++) {
      if (getStackInSlot(i).isEmpty()) {
        empty++;
      }
    }
    data.putInt("count_max", getSlots());
    data.putInt("count_empty", empty);
    boxStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }
}
