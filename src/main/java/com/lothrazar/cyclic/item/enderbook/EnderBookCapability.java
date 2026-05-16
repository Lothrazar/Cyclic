package com.lothrazar.cyclic.item.enderbook;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class EnderBookCapability extends ItemStackHandler {

  public static final int SLOTS = 9;
  private static final String NBT_KEY = "items";
  private final ItemStack bookStack;

  public EnderBookCapability(ItemStack bookStack) {
    super(SLOTS);
    this.bookStack = bookStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = bookStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserializeNBT(server.registryAccess(), data.getCompound(NBT_KEY));
      }
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server == null) {
      return;
    }
    CompoundTag data = bookStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    data.put(NBT_KEY, serializeNBT(server.registryAccess()));
    int count = 0;
    for (int i = 0; i < getSlots(); i++) {
      if (!getStackInSlot(i).isEmpty()) {
        count++;
      }
    }
    data.putInt("itemCount", count);
    bookStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }
}
