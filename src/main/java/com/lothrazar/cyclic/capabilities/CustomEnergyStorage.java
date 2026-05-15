package com.lothrazar.cyclic.capabilities;

import net.neoforged.neoforge.energy.EnergyStorage;

public class CustomEnergyStorage extends EnergyStorage {

  public CustomEnergyStorage(int capacity, int maxTransfer) {
    super(capacity, maxTransfer);
  }

  public CustomEnergyStorage(int capacity, int maxReceive, int maxExtract) {
    super(capacity, maxReceive, maxExtract);
  }

  public void setEnergy(int energy) {
    this.energy = energy;
  }
}
