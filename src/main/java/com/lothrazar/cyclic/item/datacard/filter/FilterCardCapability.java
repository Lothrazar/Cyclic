package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class FilterCardCapability extends ItemStackHandler {

  public static final int SLOTS = 9;
  private static final String NBT_KEY = "items";
  private final ItemStack cardStack;

  public FilterCardCapability(ItemStack cardStack) {
    super(SLOTS);
    this.cardStack = cardStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
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
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      data.put(NBT_KEY, serializeNBT(server.registryAccess()));
      cardStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
  }
}
