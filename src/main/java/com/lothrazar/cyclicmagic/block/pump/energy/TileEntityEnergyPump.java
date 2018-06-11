/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.block.pump.energy;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityEnergyPump extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  // Thermal does 1k, 4k, 9k, 16k, 25k per tick variants
  private int transferRate = 8 * 1000;

  private int needsRedstone = 0;

  public static enum Fields {
    REDSTONE, TRANSFER_RATE;
  }


  public TileEntityEnergyPump() {
    super(0);
    this.initEnergy(0, 8 * 1000);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    transferRate = compound.getInteger("transferRate");
    if (transferRate == 0) {
      transferRate = 8 * 1000;//legacy support
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger("transferRate", this.transferRate);
    return super.writeToNBT(compound);
  }

  @Override
  public EnumFacing getCurrentFacing() {
    //TODO: same as item pump so pump base class!?!?
    EnumFacing facingTo = super.getCurrentFacing();
    if (facingTo.getAxis().isVertical()) {
      facingTo = facingTo.getOpposite();
    }
    return facingTo;
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    IEnergyStorage myEnergy = this.getCapability(CapabilityEnergy.ENERGY, null);
    EnumFacing importFromSide = this.getCurrentFacing();
    TileEntity importFromTile = world.getTileEntity(pos.offset(importFromSide));
    IEnergyStorage exportHandler = null;
    IEnergyStorage importHandlr = null;
    if (importFromTile != null) {
      importHandlr = importFromTile.getCapability(CapabilityEnergy.ENERGY, importFromSide.getOpposite());
      // ModCyclic.logger.error("importFromTile  "+importFromTile.getBlockType().getLocalizedName());
    }
    //ALL EXCEPT THIS SIDE
    //IMPORT
    if (importHandlr != null && importHandlr.canExtract()) {
      int drain = importHandlr.extractEnergy(transferRate, true);
      if (drain > 0) {
        //now push it into output, but find out what was ACTUALLY taken
        int filled = myEnergy.receiveEnergy(drain, false);
        //now actually drain that much  
        importHandlr.extractEnergy(filled, false);
        //    ModCyclic.logger.error("pump take IN  " + filled + "i am holding" + this.pumpEnergyStore.getEnergyStored());
      }
    }
    //EXPORT
    List<EnumFacing> sidesOut = getSidesNotFacing();
    for (EnumFacing exportToSide : sidesOut) {
      TileEntity exportToTile = world.getTileEntity(pos.offset(exportToSide));
      if (exportToTile != null) {
        exportHandler = exportToTile.getCapability(CapabilityEnergy.ENERGY, exportToSide.getOpposite());
        //   ModCyclic.logger.error("exportToTile   "+exportToTile.getBlockType().getLocalizedName());
      }
      if (exportHandler != null && exportHandler.canReceive()) {
        int drain = myEnergy.extractEnergy(transferRate, true);
        if (drain > 0) {
          //now push it into output, but find out what was ACTUALLY taken
          int filled = exportHandler.receiveEnergy(drain, false);
          //now actually drain that much  
          myEnergy.extractEnergy(filled, false);
          if (importFromTile instanceof TileEntityCableBase) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityCableBase cable = (TileEntityCableBase) importFromTile;
            //  ModCyclic.logger.error("pump EXPORT  " + filled);
            if (cable.isEnergyPipe()) {
              // ModCyclic.logger.error("cable receive from   "+ side);
              cable.updateIncomingEnergyFace(importFromSide); // .getOpposite()
            }
          }
          break;//found a side that works, all done
        }
      }
    }
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
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = (this.needsRedstone + 1) % 2;
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY &&
        (facing == this.getCurrentFacing() || facing == this.getCurrentFacing().getOpposite())) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
}
