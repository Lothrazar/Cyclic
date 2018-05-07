package com.lothrazar.cyclicmagic.energy.battery;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityBattery extends TileEntityBaseMachineInvo implements ITickable {

  //for reference RFT powercells: 250k, 1M, 4M, ; gadgetry 480k
  // int dynamics is 1M
  public static final int PER_TICK = 256;
  private static final int CAPACITY = 1000000;
  //  private static final int TRANSFER_ENERGY_PER_TICK = PER_TICK * 4;

  public TileEntityBattery() {
    super(1);
    this.initEnergy(0, CAPACITY);
    this.setSlotsForBoth();
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    ItemStack toCharge = this.getStackInSlot(0);
    if (toCharge.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage handlerHere = toCharge.getCapability(CapabilityEnergy.ENERGY, null);
      if (handlerHere.canReceive() && this.energyStorage.canExtract()) {
        int canRecieve = handlerHere.receiveEnergy(PER_TICK, true);
        int canExtract = this.energyStorage.extractEnergy(PER_TICK, true);
        int actual = Math.min(canRecieve, canExtract);
        handlerHere.receiveEnergy(actual, false);
        this.energyStorage.extractEnergy(actual, false);
      }
    }
  }
}
