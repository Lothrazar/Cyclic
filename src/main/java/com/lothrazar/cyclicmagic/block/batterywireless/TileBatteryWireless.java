package com.lothrazar.cyclicmagic.block.batterywireless;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileBatteryWireless extends TileEntityBaseMachineInvo implements ITickable {

  public static final int CAPACITY = 1000 * 64;
  //same as cable
  public static final int TRANSFER_ENERGY_PER_TICK = 1000 * 16;
  public TileBatteryWireless() {
    super(1);
    this.initEnergy(0, CAPACITY);
    this.setSlotsForBoth();
  }

  private BlockPos getTarget() {
    return new BlockPos(-1096, 4, 400);
  }
  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    BlockPos target = this.getTarget();
    if (target != null) {
      TileEntity tileTarget = world.getTileEntity(target);
      if (tileTarget == null) {
        return;
      }
      if (tileTarget.hasCapability(CapabilityEnergy.ENERGY, null)) {
        // 
        //
        //drain from ME to Target 
        IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, null);
        IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, null);
      
        int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
        if (drain > 0) {
          //now push it into output, but find out what was ACTUALLY taken
          int filled = handlerOutput.receiveEnergy(drain, false);
          //now actually drain that much from here
          handlerHere.extractEnergy(filled, false);
        }
        
      
      
      //
      }
    }
  }
}
