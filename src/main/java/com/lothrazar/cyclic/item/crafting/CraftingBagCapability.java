package com.lothrazar.cyclic.item.crafting;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class CraftingBagCapability extends ItemStackHandler {

  public static final int SLOTS = 9;
  private static final String NBT_KEY = "items";
  private final ItemStack bagStack;

  public CraftingBagCapability(ItemStack bagStack) {
    super(SLOTS);
    this.bagStack = bagStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = bagStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), data.getCompoundOrEmpty(NBT_KEY)));
      }
    }
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return !(stack.getItem() instanceof CraftingBagItem) && super.isItemValid(slot, stack);
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = bagStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, server.registryAccess());
      serialize(output);
      data.put(NBT_KEY, output.buildResult());
      bagStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
  }
}
