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
package com.lothrazar.cyclicmagic.block.base;

import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.fluid.FluidTankFixDesync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBaseMachineFluid extends TileEntityBaseMachineInvo implements IFluidHandler {

  public FluidTankBase tank;

  public TileEntityBaseMachineFluid(int fluidTankSize) {
    this(0, fluidTankSize);
  }

  public TileEntityBaseMachineFluid(int inventorySize, int fluidTankSize) {
    super(inventorySize);
    tank = new FluidTankFixDesync(fluidTankSize, this);
  }

  public static class ContainerDummy extends Container {

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
      return false;
    }
  }

  public int getCapacity() {
    return tank.getCapacity();
  }

  public FluidStack getCurrentFluidStack() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return null;
    }
    return fluidHandler.getTankProperties()[0].getContents();
  }

  @Override
  public IFluidTankProperties[] getTankProperties() {
    FluidTankInfo info = tank.getInfo();
    return new IFluidTankProperties[] { new FluidTankProperties(info.fluid, info.capacity, true, true) };
  }

  private boolean doesFluidMatchTank(FluidStack incoming) {
    if (tank.getFluid() == null) {
      return true;
    }
    return tank.getFluid().getFluid() == incoming.getFluid();
  }

  @Override
  public int fill(FluidStack resource, boolean doFill) {
    if (resource == null || doesFluidMatchTank(resource) == false) {
      return 0;
    }
    if (resource.amount + tank.getFluidAmount() > tank.getCapacity()) {//enForce limit
      resource.amount = tank.getCapacity() - tank.getFluidAmount();
    }
    int result = tank.fill(resource, doFill);
    tank.setFluid(resource);
    return result;
  }

  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    if (doesFluidMatchTank(resource) == false) {
      return resource;
    }
    FluidStack result = tank.drain(resource, doDrain);
    tank.setFluid(resource);
    return result;
  }

  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    tank.setFluid(result);
    return result;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    return super.writeToNBT(tagCompound);
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    tank.readFromNBT(tagCompound.getCompoundTag(NBT_TANK));
  }

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
    this.world.markChunkDirty(pos, this);
    return super.getCapability(capability, facing);
  }

  /**
   * fix fluid rendering breaks because pipes and pumps update my fluid level only client side
   * 
   * @param fluid
   */
  @SideOnly(Side.CLIENT)
  public void updateFluidTo(FluidStack fluid) {
    this.tank.setFluid(fluid);
  }
}
