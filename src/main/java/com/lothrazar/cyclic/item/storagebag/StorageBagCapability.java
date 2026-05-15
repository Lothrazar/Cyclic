package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class StorageBagCapability extends ItemStackHandler {

  private static final String NBT_KEY = "items";
  private final ItemStack bagStack;

  public StorageBagCapability(ItemStack bagStack) {
    super(ItemStorageBag.SLOTS);
    this.bagStack = bagStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = ItemStorageBag.getCustomData(bagStack);
      if (data.contains(NBT_KEY)) {
        deserializeNBT(server.registryAccess(), data.getCompound(NBT_KEY));
      }
    }
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = ItemStorageBag.getCustomData(bagStack);
      data.put(NBT_KEY, serializeNBT(server.registryAccess()));
      ItemStorageBag.setCustomData(bagStack, data);
    }
  }
}
