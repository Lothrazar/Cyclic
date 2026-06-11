package com.lothrazar.cyclic.item.compass;

import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.util.RegistryHolder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GpsCompassCapability extends ItemStackHandler {

  static final String NBT_KEY = "gps_slot";
  private final ItemStack compassStack;

  public GpsCompassCapability(ItemStack compassStack) {
    super(1);
    this.compassStack = compassStack;
    var lookup = RegistryHolder.get();
    if (lookup != null) {
      CompoundTag data = compassStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      if (data.contains(NBT_KEY)) {
        deserializeNBT(lookup, data.getCompound(NBT_KEY));
      }
    }
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return stack.is(ItemRegistry.LOCATION_DATA.get()) && super.isItemValid(slot, stack);
  }

  @Override
  protected void onContentsChanged(int slot) {
    var lookup = RegistryHolder.get();
    if (lookup == null) {
      return;
    }
    CompoundTag data = compassStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    data.put(NBT_KEY, serializeNBT(lookup));
    compassStack.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }
}
