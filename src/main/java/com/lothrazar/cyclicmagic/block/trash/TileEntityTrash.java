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
package com.lothrazar.cyclicmagic.block.trash;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.liquid.FluidTankBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class TileEntityTrash extends TileEntityBaseMachineFluid implements ITileRedstoneToggle, ITickable {

  private static final int INVENTORY = 8;
  private int capacity = Fluid.BUCKET_VOLUME * 16;
  private boolean doFluid = true;
  private boolean doItems = true;

  static enum Fields {
    FLUID, ITEM, REDSTONE;
  }

  public TileEntityTrash() {
    super(INVENTORY);
    tank = new FluidTankBase(capacity);
    this.setSlotsForBoth();
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (doItems) {
      for (int slot = 0; slot < this.getSizeInventory(); slot++)
        this.removeStackFromSlot(slot);
    }
    if (doFluid) {
      this.drain(capacity, true);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setBoolean("doItems", this.doItems);
    compound.setBoolean("doFluid", this.doFluid);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.doItems = compound.getBoolean("doItems");
    this.doFluid = compound.getBoolean("doFluid");
  }

  @Override
  public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        && this.doFluid == false) {
      return false;
    }
    else if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
        && this.doItems == false) {
          return false;
        }
    return super.hasCapability(capability, facing);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case FLUID:
        return this.doFluid ? 1 : 0;
      case ITEM:
        return this.doItems ? 1 : 0;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case FLUID:
        value = value % 2;
        this.doFluid = value == 1;
        this.markDirty();
      break;
      case ITEM:
        value = value % 2;
        this.doItems = value == 1;
        this.markDirty();
      break;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    this.needsRedstone = (this.needsRedstone + 1) % 2;
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
