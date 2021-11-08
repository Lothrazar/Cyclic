package com.lothrazar.cyclic.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<net.minecraft.nbt.Tag> {

  public static final String NBTENERGY = "energy";

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

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putInt(NBTENERGY, getEnergyStored());
    return tag;
  }

  @Override
  public void deserializeNBT(net.minecraft.nbt.Tag nbt) {
    CompoundTag real = (CompoundTag) nbt;
    setEnergy(real.getInt(NBTENERGY));
  }
}
