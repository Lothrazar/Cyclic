package com.lothrazar.cyclic.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundTag> {

  public CustomEnergyStorage(int capacity, int maxTransfer) {
    super(capacity, maxTransfer);
  }

  public void setEnergy(int energyIn) {
    if (energyIn < 0) {
      energyIn = 0;
    }
    if (energyIn > getMaxEnergyStored()) {
      energyIn = getEnergyStored();
    }
    this.energy = energyIn;
  }

  public void addEnergy(int energy) {
    this.energy += energy;
    if (this.energy > getMaxEnergyStored()) {
      this.energy = getEnergyStored();
    }
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putInt("energy", getEnergyStored());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    setEnergy(nbt.getInt("energy"));
  }
}
