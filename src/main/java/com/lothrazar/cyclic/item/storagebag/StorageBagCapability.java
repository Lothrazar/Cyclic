package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
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
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), data.getCompoundOrEmpty(NBT_KEY)));
      }
    }
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = ItemStorageBag.getCustomData(bagStack);
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, server.registryAccess());
      serialize(output);
      data.put(NBT_KEY, output.buildResult());
      ItemStorageBag.setCustomData(bagStack, data);
    }
  }
}
