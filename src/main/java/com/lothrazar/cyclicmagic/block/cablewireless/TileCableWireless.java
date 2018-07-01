package com.lothrazar.cyclicmagic.block.cablewireless;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.liquid.FluidTankBase;
import com.lothrazar.cyclicmagic.core.util.UtilFluid;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableWireless extends TileEntityBaseMachineFluid implements ITickable {

  public static final int ENERGY_FULL = 1000 * 64;
  //same as cable
  public static final int TRANSFER_ENERGY_PER_TICK = 1000 * 16;
  public static final int TRANSFER_FLUID_PER_TICK = 500;
  public static final int TANK_FULL = 10000;
  private static final int SLOT_CARD = 0;
  private static final int SLOT_TRANSFER = 1;

  public static enum Fields {
    REDSTONE;
  }

  private int needsRedstone = 0;

  public TileCableWireless() {
    super(2);
    tank = new FluidTankBase(TANK_FULL);
    this.initEnergy(0, ENERGY_FULL);
    this.setSlotsForInsert(1);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }

  private BlockPos getTarget() {
    return ItemLocation.getPosition(this.getStackInSlot(SLOT_CARD));
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    BlockPos target = this.getTarget();
    if (target != null && world.isAreaLoaded(target, target.up())
        && world.getTileEntity(target) != null) {//&& !this.world.isRemote
      TileEntity tileTarget = world.getTileEntity(target);
      outputEnergy(tileTarget);
      outputItems(tileTarget);
      UtilFluid.tryFillPositionFromTank(world, this.getTarget(), null, this.tank, TRANSFER_FLUID_PER_TICK);
    }
  }

  private void outputItems(TileEntity tileTarget) {
    BlockPos target = this.getTarget();
    ItemStack stackToExport;
    stackToExport = this.getStackInSlot(SLOT_TRANSFER).copy();
    stackToExport.setCount(1);
    if (stackToExport.isEmpty() == false) {
      ItemStack leftAfterDeposit = UtilItemStack.tryDepositToHandler(world, target, null, stackToExport);
      if (leftAfterDeposit.getCount() < stackToExport.getCount()) { //something moved!
        //then save result
        this.decrStackSize(SLOT_TRANSFER);
      }
    }
  }

  private void outputEnergy(TileEntity tileTarget) {
    if (tileTarget.hasCapability(CapabilityEnergy.ENERGY, null)) {
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
    }
  }
}
