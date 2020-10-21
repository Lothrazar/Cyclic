package com.lothrazar.cyclicmagic.block.batterycheat;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import net.minecraft.util.ITickable;

public class TileEntityBatteryInfinite extends TileEntityBaseMachineInvo implements ITickable {

  public static final int PER_TICK_IN = Integer.MAX_VALUE / 2 - 1;
  public static final int PER_TICK_OUT = Integer.MAX_VALUE / 8;
  private static final int CAPACITY = Integer.MAX_VALUE - 1;

  public TileEntityBatteryInfinite() {
    super(0);
    this.initEnergy(new EnergyStore(CAPACITY, CAPACITY, CAPACITY));
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    //generate free power
    this.energyStorage.receiveEnergy(PER_TICK_IN, false);
    //then output
    this.tryOutputPower(PER_TICK_OUT);
  }
}
