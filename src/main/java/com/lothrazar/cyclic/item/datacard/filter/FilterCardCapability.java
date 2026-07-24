package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class FilterCardCapability extends ItemStackHandler {

  public static final int SLOTS = 9;
  private static final String NBT_KEY = "items";
  private static final String NBT_COUNT = "itemCount";
  private static final String NBT_TOOLTIP = "itemTooltip";
  private final ItemStack cardStack;

  public FilterCardCapability(ItemStack cardStack) {
    super(SLOTS);
    this.cardStack = cardStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), data.getCompound(NBT_KEY)));
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
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, server.registryAccess());
      serialize(output);
      data.put(NBT_KEY, output.buildResult());
      //precompute tooltip data into CUSTOM_DATA so it syncs to client
      //(NeoForge 1.21 has no getShareTag/readShareTag equivalent; CUSTOM_DATA is the wire format)
      int count = 0;
      Component firstName = null;
      for (int i = 0; i < this.getSlots(); i++) {
        ItemStack inSlot = this.getStackInSlot(i);
        if (!inSlot.isEmpty()) {
          count++;
          if (firstName == null) {
            firstName = inSlot.getHoverName();
          }
        }
      }
      data.putInt(NBT_COUNT, count);
      if (firstName != null) {
        data.putString(NBT_TOOLTIP, firstName.getString());
      }
      else {
        data.remove(NBT_TOOLTIP);
      }
      cardStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
  }
}
