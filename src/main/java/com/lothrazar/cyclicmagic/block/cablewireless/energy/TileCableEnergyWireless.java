package com.lothrazar.cyclicmagic.block.cablewireless.energy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileCableEnergyWireless extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {

  public static final int ENERGY_FULL = 1000 * 64;
  public static final int MAX_TRANSFER = 1000;
  public static final int SLOT_COUNT = 9;
  //it transfers this to each location if possible
  private int transferRate = MAX_TRANSFER / 2;

  public static enum Fields {
    REDSTONE, TRANSFER_RATE;
  }

  List<Integer> slotList;
  private int needsRedstone = 0;

  public TileCableEnergyWireless() {
    super(SLOT_COUNT);
    this.initEnergy(new EnergyStore(ENERGY_FULL, ENERGY_FULL, ENERGY_FULL));
    slotList = IntStream.rangeClosed(
        0, TileCableEnergyWireless.SLOT_COUNT).boxed().collect(Collectors.toList());
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
      case TRANSFER_RATE:
        return this.transferRate;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case TRANSFER_RATE:
        transferRate = value;
      break;
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() instanceof ItemLocation;
  }

  private BlockPosDim getTarget(int slot) {
    return ItemLocation.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    //shuffle into random order
    Collections.shuffle(slotList);
    for (int slot : slotList) {
      //try to output the same rate to every location at the same speed
      outputEnergy(slot);
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone == 1) ? 0 : 1;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  private boolean isTargetValid(BlockPosDim target) {
    return target != null &&
        target.dimension == this.getDimension() &&
        world.isAreaLoaded(target.toBlockPos(), target.toBlockPos().up());
  }

  private void outputEnergy(int slot) {
    BlockPosDim dim = this.getTarget(slot);
    if (!this.isTargetValid(dim)) {
      return;
    }
    BlockPos target = dim.toBlockPos();
    TileEntity tileTarget = world.getTileEntity(target);
    if (tileTarget != null && tileTarget.hasCapability(CapabilityEnergy.ENERGY, null)) {
      //drain from ME to Target 
      IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, null);
      IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, null);
      int drain = handlerHere.extractEnergy(transferRate, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = handlerOutput.receiveEnergy(drain, false);
        //now actually drain that much from here
        handlerHere.extractEnergy(filled, false);
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.transferRate = compound.getInteger("transferRate");
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("transferRate", transferRate);
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }
}
