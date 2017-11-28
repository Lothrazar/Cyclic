package com.lothrazar.cyclicmagic.block;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStore extends EnergyStorage {
  public static final int DEFAULT_CAPACITY = 1000000;
  public static final int DEFAULT_FLOW = 100;
  public EnergyStore() {
    super(DEFAULT_CAPACITY);
    this.maxReceive = DEFAULT_FLOW;
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
