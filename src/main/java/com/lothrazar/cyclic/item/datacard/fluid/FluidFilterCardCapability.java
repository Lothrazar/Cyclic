package com.lothrazar.cyclic.item.datacard.fluid;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * Single-slot capability backing FluidFilterCardItem. The slot only accepts items
 * exposing a FluidHandlerItem capability (i.e. buckets, bottles, etc.).
 */
public class FluidFilterCardCapability extends ItemStackHandler {

  public static final int SLOTS = 1;
  private static final String NBT_KEY = "bucket";
  private static final String NBT_TOOLTIP = "fluidTooltip";
  private final ItemStack cardStack;

  public FluidFilterCardCapability(ItemStack cardStack) {
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
  public boolean isItemValid(int slot, ItemStack stack) {
    if (stack.isEmpty()) {
      return true;
    }
    //only allow items that carry a fluid (any bucket-like item)
    return stack.getCapability(Capabilities.FluidHandler.ITEM) != null;
  }

  @Override
  protected void onContentsChanged(int slot) {
    var server = ServerLifecycleHooks.getCurrentServer();
    if (server != null) {
      CompoundTag data = cardStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      data.put(NBT_KEY, serializeNBT(server.registryAccess()));
      //precompute the fluid display name into CUSTOM_DATA so it syncs to the client
      //(NeoForge 1.21 dropped Item.getShareTag; CUSTOM_DATA is the auto-synced channel)
      ItemStack bucket = this.getStackInSlot(0);
      IFluidHandlerItem fluidCap = bucket.getCapability(Capabilities.FluidHandler.ITEM);
      FluidStack fluid = (fluidCap != null) ? fluidCap.getFluidInTank(0) : FluidStack.EMPTY;
      if (fluid != null && !fluid.isEmpty()) {
        data.putString(NBT_TOOLTIP, fluid.getHoverName().getString());
      }
      else {
        data.remove(NBT_TOOLTIP);
      }
      cardStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
    }
  }
}
