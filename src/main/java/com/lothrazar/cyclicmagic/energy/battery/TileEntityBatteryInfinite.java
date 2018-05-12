package com.lothrazar.cyclicmagic.energy.battery;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.util.ITickable;

public class TileEntityBatteryInfinite extends TileEntityBaseMachineInvo implements ITickable {

  public static final int PER_TICK = 256;
  private static final int CAPACITY = PER_TICK * 1000;
  private static final int TRANSFER_ENERGY_PER_TICK = PER_TICK * 4;

  public TileEntityBatteryInfinite() {
    super(0);
    this.initEnergy(0, CAPACITY);
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    //generate free power
    this.energyStorage.receiveEnergy(PER_TICK * 4, false);
    //then output
    this.tryOutputPower(TRANSFER_ENERGY_PER_TICK);
  }
}
