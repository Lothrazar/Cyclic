package com.lothrazar.cyclic.item.datacard.fluid;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Nine-slot capability backing FluidFilterCardItem. Each slot only accepts items
 * exposing a FluidHandlerItem capability (i.e. buckets, bottles, etc.).
 */
public class FluidFilterCardCapability extends ItemStackHandler {

  public static final int SLOTS = 9;
  private static final String NBT_KEY = "bucket";
  private static final String NBT_TOOLTIP = "fluidTooltip";
  private static final String NBT_COUNT = "fluidCount";
  private final ItemStack cardStack;

  public FluidFilterCardCapability(ItemStack cardStack) {
    super(SLOTS);
    this.cardStack = cardStack;
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserialize(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), data.getCompoundOrEmpty(NBT_KEY)));
      }
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    if (stack.isEmpty()) {
      return true;
    }
    //only allow items that carry a fluid (any bucket-like item)
    return FluidUtil.getFluidHandler(stack).isPresent();
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      TagValueOutput output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, server.registryAccess());
      serialize(output);
      data.put(NBT_KEY, output.buildResult());
      //precompute tooltip data into CUSTOM_DATA so it syncs to the client
      int count = 0;
      String firstName = null;
      for (int i = 0; i < this.getSlots(); i++) {
        ItemStack bucket = this.getStackInSlot(i);
        if (bucket.isEmpty()) {
          continue;
        }
        IFluidHandlerItem fluidCap = FluidUtil.getFluidHandler(bucket).orElse(null);
        if (fluidCap == null) {
          continue;
        }
        FluidStack fluid = fluidCap.getFluidInTank(0);
        if (fluid == null || fluid.isEmpty()) {
          continue;
        }
        count++;
        if (firstName == null) {
          firstName = fluid.getHoverName().getString();
        }
      }
      data.putInt(NBT_COUNT, count);
      if (firstName != null) {
        data.putString(NBT_TOOLTIP, firstName);
      }
      else {
        data.remove(NBT_TOOLTIP);
      }
      cardStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
  }
}
