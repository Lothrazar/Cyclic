package com.lothrazar.cyclic.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage implements INBTSerializable<CompoundNBT> {

  public static final String NBTENERGY = "energy";
  public int energyLastSynced;

  public CustomEnergyStorage(int capacity, int maxTransfer) {
    super(capacity, maxTransfer);
  }

  public void setEnergy(int energyIn) {
    if (energyIn < 0) {
      energyIn = 0;
    }
    if (energyIn > getMaxEnergyStored()) {
      energyIn = getMaxEnergyStored();
    }
    this.energy = energyIn;
  }

  @Override
  public CompoundNBT serializeNBT() {
    CompoundNBT tag = new CompoundNBT();
    tag.putInt(NBTENERGY, getEnergyStored());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundNBT nbt) {
    this.energy = nbt.getInt(NBTENERGY);
  }
}
