package com.lothrazar.cyclicmagic.energy;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.util.ITickable;

public class TileEntityBatteryInfinite extends TileEntityBaseMachineInvo implements ITickable {

  public static final int PER_TICK = 256;
  private static final int CAPACITY = PER_TICK * 1000;
  private static final int TRANSFER_ENERGY_PER_TICK = PER_TICK * 4;
  private boolean isCreative = true;

  public TileEntityBatteryInfinite() {
    super(0);
    this.initEnergy(0, CAPACITY);
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    if(isCreative){
      this.energyStorage.receiveEnergy(PER_TICK * 4, false);
    }
    //TODO: output only on redstone signal , standard system
    //default to always on
    this.tryOutputPower(TRANSFER_ENERGY_PER_TICK);
  }
}
