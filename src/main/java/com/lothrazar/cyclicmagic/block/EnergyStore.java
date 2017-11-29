package com.lothrazar.cyclicmagic.block;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStore extends EnergyStorage {
  public static final int DEFAULT_CAPACITY = 1000000;
  public static final int MAX_INPUT = 5000;
  public EnergyStore() {
    super(DEFAULT_CAPACITY);
    this.maxReceive = MAX_INPUT;
  }
  public void setEnergyStored(int en) {
    if (en < 0) {
      en = 0;
    }
    this.energy = Math.min(en, this.capacity);
  }
  public int emptyCapacity() {
    return this.capacity - this.energy;
  }
}
