package com.lothrazar.cyclic.capabilities.item;

import com.lothrazar.library.cap.EnergyStorageWrapper;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

/**
 * Item-level energy capability backed by the host stack's CUSTOM_DATA component.
 * Reads/writes a single int under the "energy" key so the value persists across
 * place/break cycles and serializes with the stack.
 */
public class ItemEnergyCap extends EnergyStorageWrapper {

  private static final String KEY = "energy";
  private final ItemStack host;

  public ItemEnergyCap(ItemStack host, int capacity) {
    super(capacity, capacity);
    this.host = host;
    CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    if (data.contains(KEY)) {
      super.setEnergy(data.getInt(KEY));
    }
  }

  private void save() {
    CompoundTag data = host.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    data.putInt(KEY, getEnergyStored());
    host.set(DataComponents.CUSTOM_DATA, CustomData.of(data));
  }

  @Override
  public void setEnergy(int energyIn) {
    super.setEnergy(energyIn);
    if (host != null) {
      save();
    }
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    int r = super.receiveEnergy(maxReceive, simulate);
    if (!simulate && r > 0) {
      save();
    }
    return r;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    int r = super.extractEnergy(maxExtract, simulate);
    if (!simulate && r > 0) {
      save();
    }
    return r;
  }
}
