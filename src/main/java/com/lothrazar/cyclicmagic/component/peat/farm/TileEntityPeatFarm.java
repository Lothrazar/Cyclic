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
package com.lothrazar.cyclicmagic.component.peat.farm;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.peat.generator.TileEntityPeatGenerator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class TileEntityPeatFarm extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable, IFluidHandler {

  public static final int TANK_FULL = Fluid.BUCKET_VOLUME * 20;
  public static final int TIMER_FULL = 5;
  private static final int PER_TICK = TileEntityPeatGenerator.PER_TICK / 2;
  private static final int CAPACITY = 60 * Fluid.BUCKET_VOLUME;

  public static enum Fields {
    REDSTONE, TIMER, FLUID;
  }

  private int needsRedstone = 1;
  public FluidTankBase tank = new FluidTankBase(TANK_FULL);

  private int blockPointer = 0;

  public TileEntityPeatFarm() {
    super(12);
    tank.setTileEntity(this);
    tank.setFluidAllowed(FluidRegistry.WATER);
    energyStorage = new EnergyStore(CAPACITY);
    timer = TIMER_FULL;
    this.setSlotsForInsert(0, this.getSizeInventory());
  }

  Block baked = null;
  Block unbaked = null;
  List<BlockPos> outer = null;

  private void init() {
    if (baked == null)
      baked = Block.getBlockFromName(Const.MODRES + "peat_baked");
    if (unbaked == null)
      unbaked = Block.getBlockFromName(Const.MODRES + "peat_unbaked");
    if (outer == null) {
      outer = getShape();
      List<BlockPos> waterShape = UtilShape.squareHorizontalHollow(this.pos, 6);
      outer.addAll(waterShape);
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return Block.getBlockFromItem(stack.getItem()) == unbaked;
  }

  @Override
  public void update() {
    this.init();
    if (isRunning() == false) {
      blockPointer = 0;//start over when turned off
      return;
    }
    energyStorage.extractEnergy(PER_TICK, false);
    if (energyStorage.getEnergyStored() == 0) {
      return;
    }
    //GET VALIDATE ITEM
    this.timer--;
    if (this.timer > 0) {
      return;
    }
    //now loop for build/harvest 
    if (blockPointer < outer.size()) {
      BlockPos target = outer.get(blockPointer);
      //  ModCyclic.logger.error("?"+blockPointer+"  "+target);
      boolean placeWater = (target.getX() - pos.getX()) % 3 == 0
          && (target.getZ() - pos.getZ()) % 3 == 0;
      //based on pattern, try to place either water or the block
      if (placeWater) {
        tryPlaceWater(target);
      }
      else {
        tryPlacePeat(target);
      }
      //and move along
      this.timer = TIMER_FULL;
      blockPointer++;
    }
    else {
      blockPointer = 0;
      //    ModCyclic.logger.error("RESET");
    }
  }

  private void tryPlacePeat(BlockPos target) {
    for (int i = 0; i < this.getSizeInventory(); i++) {
      if (this.getStackInSlot(i).isItemEqual(new ItemStack(unbaked))) {
        tryHarvest(target);
        if (world.getBlockState(target).getBlock().isReplaceable(world, target)
        //            || world.getBlockState(target).getBlock()==Blocks.WATER 
        //            || world.getBlockState(target).getBlock()==Blocks.FLOWING_WATER
        ) {
          world.setBlockState(target, unbaked.getDefaultState());
          this.decrStackSize(i);
          return;
        }
      }
    }
    //  ModCyclic.logger.error("not enough blocks");
  }

  private void tryPlaceWater(BlockPos target) {
    if (world.getBlockState(target).getBlock().isReplaceable(world, target)
        && tank.getFluidAmount() >= Fluid.BUCKET_VOLUME
        && tank.drain(Fluid.BUCKET_VOLUME, true) != null) {
      tank.drain(Fluid.BUCKET_VOLUME, false);
      world.setBlockState(target, Blocks.FLOWING_WATER.getDefaultState());
    }
  }

  private void tryHarvest(BlockPos target) {
    if (world.getBlockState(target).getBlock() == baked) {
      //    ModCyclic.logger.error("HARVEST peat " + target);
      world.destroyBlock(target, true);
    }
  }

  private List<BlockPos> getShape() {
    List<BlockPos> outer = UtilShape.squareHorizontalHollow(this.pos, 7);
    outer.addAll(UtilShape.squareHorizontalHollow(this.pos, 5));
    return outer;
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
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
    blockPointer = compound.getInteger("blockPointer");
    tank.readFromNBT(compound.getCompoundTag(NBT_TANK));
    // CapabilityEnergy.ENERGY.readNBT(energy, null, compound.getTag("powercable"));
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, this.needsRedstone);
    compound.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    //compound.setTag("powercable", CapabilityEnergy.ENERGY.writeNBT(energy, null));
    compound.setInteger("blockPointer", blockPointer);
    return super.writeToNBT(compound);
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  @Override
  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TIMER:
        return this.timer;
      case FLUID:
        return this.getCurrentFluid();
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case TIMER:
        this.timer = value;
      break;
      case FLUID:
        this.setCurrentFluid(value);
      break;
    }
  }

  private int getCurrentFluid() {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return 0;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    return (fluid == null) ? 0 : fluid.amount;
  }

  private void setCurrentFluid(int amt) {
    IFluidHandler fluidHandler = this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
    if (fluidHandler == null || fluidHandler.getTankProperties() == null || fluidHandler.getTankProperties().length == 0) {
      return;
    }
    FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
    if (fluid == null) {
      fluid = new FluidStack(FluidRegistry.WATER, amt);
    }
    fluid.amount = amt;
    this.tank.setFluid(fluid);
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityEnergy.ENERGY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
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
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result);
    return result;
  }

  @Override
  public FluidStack drain(FluidStack resource, boolean doDrain) {
    FluidStack result = tank.drain(resource, doDrain);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result.amount);
    return result;
  }

  @Override
  public FluidStack drain(int maxDrain, boolean doDrain) {
    FluidStack result = tank.drain(maxDrain, doDrain);
    this.world.markChunkDirty(pos, this);
    this.setField(Fields.FLUID.ordinal(), result.amount);
    return result;
  }
}
