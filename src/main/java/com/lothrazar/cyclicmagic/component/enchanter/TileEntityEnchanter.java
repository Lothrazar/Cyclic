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
package com.lothrazar.cyclicmagic.component.enchanter;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityEnchanter extends TileEntityBaseMachineInvo implements ITickable, IFluidHandler, ITileRedstoneToggle {
  public static final int TANK_FULL = 10000;
  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int TIMER_FULL = 22;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final int MAX_LEVEL = 30;
  public static int FLUID_COST = 300;
  public static enum Fields {
    TIMER, EXP, REDSTONE, FUEL, FUELMAX, FUELDISPLAY;
  }
  private int timer = 0;
  private int needsRedstone = 0;
  public FluidTankBase tank = new FluidTankBase(TANK_FULL);
  public TileEntityEnchanter() {
    super(3);
    this.setFuelSlot(2, BlockEnchanter.FUEL_COST);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
    tank.setFluidAllowed(FluidRegistry.getFluid("xpjuice"));
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
    if (inputStack.isEmpty() || this.hasEnoughFluid() == false) {
      return;//no paying cost on empty work
    }
    if (this.updateFuelIsBurning() == false) {
      return;
    }
    if (this.getCurrentFluid() < 0) {
      this.setCurrentFluid(0);
    }
    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      ItemStack outputStack = ItemStack.EMPTY;
      if ((inputStack.getItem() == Items.BOOK || inputStack.isItemEnchantable()) &&
          this.hasEnoughFluid() &&
          this.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
        //pay item cost and build the enchatned output item
        outputStack = inputStack.copy();
        outputStack.setCount(1);
        inputStack.shrink(1);
        outputStack = EnchantmentHelper.addRandomEnchantment(world.rand, outputStack, MAX_LEVEL, true);
        this.setInventorySlotContents(SLOT_INPUT, inputStack);
        this.setInventorySlotContents(SLOT_OUTPUT, outputStack);
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
    tags.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    tags.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tags);
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    tank.readFromNBT(tags.getCompoundTag(NBT_TANK));
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
      case EXP:
        return this.getCurrentFluid();
      case REDSTONE:
        return needsRedstone;
      case FUEL:
        return this.getFuelCurrent();
      case FUELMAX:
        return this.getFuelMax();
      case FUELDISPLAY:
        return this.fuelDisplay;
    }
    return -1;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case EXP:
        this.setCurrentFluid(value);
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case FUEL:
        this.setFuelCurrent(value);
      break;
      case FUELMAX:
      break;
      case FUELDISPLAY:
        this.fuelDisplay = value % 2;
      break;
    }
  }
  private void setCurrentFluid(int amt) {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    if (fluid == null) {
      fluid = new FluidStack(FluidRegistry.getFluid("xpjuice"), amt);
    }
    fluid.amount = amt;
    this.tank.setFluid(fluid);
  }
  /******************************
   * fluid properties here
   ******************************/
  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return true;
    }
    return super.hasCapability(capability, facing);
  }
  @Override
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
      return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
    }
    return super.getCapability(capability, facing);
  }
  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }
  @Override
  public int fill(FluidStack resource, boolean doFill) {
    int result = tank.fill(resource, doFill);
    this.setField(Fields.EXP.ordinal(), result);
    return result;
  }
  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    FluidStack result = tank.drain(resource, doDrain);
    return result;
  }
  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    return result;
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
