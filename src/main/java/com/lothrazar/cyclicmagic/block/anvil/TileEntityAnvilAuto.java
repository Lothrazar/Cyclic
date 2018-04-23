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
package com.lothrazar.cyclicmagic.block.anvil;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.liquid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilString;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityAnvilAuto extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {

  public static final int TANK_FULL = 10000;
  public static final int TIMER_FULL = 3;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static int FLUID_COST = 75;
  static NonNullList<String> blacklistBlockIds;

  public static enum Fields {
    TIMER, REDSTONE, FUELDISPLAY;
  }

  private int timer = 0;
  private int needsRedstone = 0;


  public TileEntityAnvilAuto() {
    super(2);
    tank = new FluidTankBase(TANK_FULL);
    tank.setFluidAllowed(FluidRegistry.LAVA);
    this.initEnergyWithCost(BlockAnvilAuto.FUEL_COST);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
  }

  private boolean isBlockAllowed(ItemStack thing) {
    return UtilString.isInList(blacklistBlockIds, thing.getItem().getRegistryName()) == false;
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    ItemStack inputStack = this.getStackInSlot(SLOT_INPUT);
    //validate item
    if (inputStack.isItemDamaged() == false ||
        isBlockAllowed(inputStack) == false) {
      //all done
      if (this.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
        //delete bug fix
        this.setInventorySlotContents(SLOT_OUTPUT, this.removeStackFromSlot(SLOT_INPUT));
      }
      return;
    }
    if (inputStack.isEmpty() || this.hasEnoughFluid() == false) {
      return;//no paying cost on empty work
    }
    this.spawnParticlesAbove();
    //pay energy each tick
    if (this.updateFuelIsBurning() == false) {
      return;
    }
    if (this.getCurrentFluid() < 0) {
      this.setCurrentFluid(0);
    }
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      if (inputStack.isItemDamaged() && this.hasEnoughFluid()) {
        inputStack.setItemDamage(inputStack.getItemDamage() - 1);
        //pay fluid each repair update
        this.drain(FLUID_COST, true);
      }
    }
  }

  private boolean hasEnoughFluid() {
    FluidStack contains = this.tank.getFluid();
    return (contains != null && contains.amount >= FLUID_COST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);

    tags.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);

    this.needsRedstone = tags.getInteger(NBT_REDST);
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  public int getCurrentFluid() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return 0;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    return (fluid == null) ? 0 : fluid.amount;
  }

  @Override
  public FluidStack getCurrentFluidStack() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return null;
    }
    return fluidHandler.getTankProperties()[0].getContents();
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;


      case FUELDISPLAY:
        return this.fuelDisplay;
      case REDSTONE:
        return needsRedstone;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;

      case FUELDISPLAY:
        this.fuelDisplay = value % 2;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }

  @Override
  public void toggleNeedsRedstone() {
    this.setField(Fields.REDSTONE.ordinal(), (this.needsRedstone + 1) % 2);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
