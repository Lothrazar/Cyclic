package com.lothrazar.cyclicmagic.block.battery;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityBattery extends TileEntityBaseMachineInvo implements ITickable {

  public static enum Fields {
    FUEL, POWER, N, E, S, W, U, D;
  }

  private static final double PCT_UPDATE_ON_TICK = 0.01;
  private int power = 1000;
  //for reference RFT powercells: 250k, 1M, 4M, ; gadgetry 480k
  public static final int PER_TICK = 1000 * 64;
  public static final int CAPACITY = 1000000;
  private Map<EnumFacing, Boolean> poweredSides;
  public TileEntityBattery() {
    super(1);
    this.initEnergy(0, CAPACITY);
    this.setSlotsForBoth();
    poweredSides = new HashMap<EnumFacing, Boolean>();
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, false);
    }
  }

  public int getPowerForSide(EnumFacing side) {
    if (this.getSideHasPower(side))
      return this.power;
    else
      return 0;
  }

  public boolean getSideHasPower(EnumFacing side) {
    return this.poweredSides.get(side);
  }

  public int getSideField(EnumFacing side) {
    return this.getSideHasPower(side) ? 1 : 0;
  }

  public void setSideField(EnumFacing side, int pow) {
    this.poweredSides.put(side, (pow == 1));
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case FUEL:
        this.setEnergyCurrent(value);
      break;
      case POWER:
        power = value;
      break;
      case D:
        this.setSideField(EnumFacing.DOWN, value % 2);
      break;
      case E:
        this.setSideField(EnumFacing.EAST, value % 2);
      break;
      case N:
        this.setSideField(EnumFacing.NORTH, value % 2);
      break;
      case S:
        this.setSideField(EnumFacing.SOUTH, value % 2);
      break;
      case U:
        this.setSideField(EnumFacing.UP, value % 2);
      break;
      case W:
        this.setSideField(EnumFacing.WEST, value % 2);
      break;
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case FUEL:
        return this.getEnergyCurrent();
      case POWER:
        return power;
      case D:
        return this.getSideField(EnumFacing.DOWN);
      case E:
        return this.getSideField(EnumFacing.EAST);
      case N:
        return this.getSideField(EnumFacing.NORTH);
      case S:
        return this.getSideField(EnumFacing.SOUTH);
      case U:
        return this.getSideField(EnumFacing.UP);
      case W:
        return this.getSideField(EnumFacing.WEST);
    }
    return -1;
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    if (world.rand.nextDouble() < PCT_UPDATE_ON_TICK) {
      //push client updates for energy bar but not every tick 
      world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }
    //attept to auto export power to nbrs 
    tryChargeItem();
    //auto export based on checkboxes
    tryOutputItem();
  }

  private void tryOutputItem() {
    for (EnumFacing f : EnumFacing.values()) {
      if (this.poweredSides.get(f)) {

        //this facing direction has output
        //TODO::!!>... copied from TileEntityCableBase 327 ish
        IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, f);
        if (handlerHere.getEnergyStored() == 0) {
          return;
        }
        EnumFacing themFacingMe = f.getOpposite();
        BlockPos posTarget = pos.offset(f);
        TileEntity tileTarget = world.getTileEntity(posTarget);
        if (tileTarget == null || tileTarget.hasCapability(CapabilityEnergy.ENERGY, themFacingMe) == false) {
          return;
        }
        IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, themFacingMe);
        if (handlerHere != null && handlerOutput != null
            && handlerHere.canExtract() && handlerOutput.canReceive()) {
          //first simulate
          int drain = handlerHere.extractEnergy(this.power, true);
          if (drain > 0) {
            //now push it into output, but find out what was ACTUALLY taken
            int filled = handlerOutput.receiveEnergy(drain, false);
            //now actually drain that much from here
            handlerHere.extractEnergy(filled, false);
            if (filled > 0 && tileTarget instanceof TileEntityCableBase) {
              //TODO: not so compatible with other fluid systems. itl do i guess
              TileEntityCableBase cable = (TileEntityCableBase) tileTarget;
              if (cable.isEnergyPipe()) {
                cable.updateIncomingEnergyFace(themFacingMe);
              }
            }
          }
        }
      }
    }
  }

  private void tryChargeItem() {
    ItemStack toCharge = this.getStackInSlot(0);
    if (toCharge.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage energyItemStack = toCharge.getCapability(CapabilityEnergy.ENERGY, null);
      if (energyItemStack.canReceive() && this.energyStorage.canExtract()) {
        int canRecieve = energyItemStack.receiveEnergy(PER_TICK, true);
        int canExtract = this.energyStorage.extractEnergy(PER_TICK, true);
        int actual = Math.min(canRecieve, canExtract);
        int toExtract = energyItemStack.receiveEnergy(actual, false);
        this.energyStorage.extractEnergy(toExtract, false);
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("power", power);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setBoolean(f.getName(), poweredSides.get(f));
    }
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    power = compound.getInteger("power");
    if (power <= 0) {
      power = 1000;
    }
    poweredSides = new HashMap<EnumFacing, Boolean>();
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, compound.getBoolean(f.getName()));
    }
  }
}
