package com.lothrazar.cyclicmagic.block;
import net.minecraftforge.energy.EnergyStorage;

public class EnergyStore extends EnergyStorage {
  public EnergyStore(int capacity) {
    super(capacity);
  }
  
  public void setEnergyStored(int en){
    this.energy = Math.min(en, this.capacity);
   
  }
  
  
  
  
}
