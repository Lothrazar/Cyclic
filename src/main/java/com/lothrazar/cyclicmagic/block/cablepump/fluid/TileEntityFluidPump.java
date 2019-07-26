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
package com.lothrazar.cyclicmagic.block.cablepump.fluid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.block.cablepump.TileEntityBasePump;
import com.lothrazar.cyclicmagic.data.FluidWrapper;
import com.lothrazar.cyclicmagic.data.ITileFluidWrapper;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.liquid.FluidTankBase;
import com.lothrazar.cyclicmagic.util.UtilFluid;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidPump extends TileEntityBasePump implements ITickable, ITileRedstoneToggle, ITileFluidWrapper {

  private NonNullList<FluidWrapper> stacksWrapped = NonNullList.withSize(9, new FluidWrapper());
  private int transferRate = Fluid.BUCKET_VOLUME;
  private int filterType = 0;

  public static enum Fields {
    REDSTONE, TRANSFER_RATE, FILTERTYPE;
  }

  public TileEntityFluidPump() {
    super(0);
    tank = new FluidTankBase(Fluid.BUCKET_VOLUME);
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

  /**
   * for every side connected to me pull fluid in from it UNLESS its my current facing direction. for THAT side, i push fluid out from me pull first then push
   *
   * TODO: UtilFluid that does a position, a facing, and tries to move fluid across
   *
   *
   */
  @Override
  public void update() {
    if (this.isPowered() == false && this.onlyRunIfPowered()) {
      return;//i am not powered, and i require it
    }
    //incoming target side
    BlockPos target = pos.offset(this.getCurrentFacing());
    UtilFluid.tryFillTankFromPosition(world, target, this.getCurrentFacing().getOpposite(), tank, transferRate,
        this.isWhitelist(), this.getFilterNonempty());
    if (world.getBlockState(target).getMaterial().isLiquid()
        && this.transferRate == Fluid.BUCKET_VOLUME) {
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, target);
      IFluidHandler handle = FluidUtil.getFluidHandler(world, target, EnumFacing.UP);
      if (handle.getTankProperties() == null || handle.getTankProperties().length == 0) {
        return;
      }
      FluidStack fluidFromWorld = handle.getTankProperties()[0].getContents();
      if (fluidFromWorld != null
          && UtilFluid.isStackInvalid(fluidFromWorld, isWhitelist(), getFilterNonempty())
          && this.tank.canFillFluidType(fluidFromWorld)) {
        this.tank.fill(fluidFromWorld, true);
        world.setBlockToAir(target);
      }
    }
    //eXPORT: now try to DEPOSIT fluid next door
    List<EnumFacing> sidesOut = getSidesNotFacing();
    Collections.shuffle(sidesOut);
    for (EnumFacing exportToSide : sidesOut) {
      moveFluid(exportToSide);
    }
  }

  private void moveFluid(EnumFacing myFacingDir) {
    if (this.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, myFacingDir) == false) {
      return;
    }
    EnumFacing themFacingMe = myFacingDir.getOpposite();
    BlockPos posSide = pos.offset(myFacingDir);
    boolean outputSuccess = UtilFluid.tryFillPositionFromTank(world, posSide, themFacingMe, tank, transferRate);
    if (outputSuccess && world.getTileEntity(posSide) instanceof TileEntityCableBase) {
      //TODO capability for sided
      TileEntityCableBase cable = (TileEntityCableBase) world.getTileEntity(posSide);
      if (cable.isFluidPipe()) {
        cable.updateIncomingFluidFace(themFacingMe);
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    needsRedstone = compound.getInteger(NBT_REDST);
    transferRate = compound.getInteger("transferSaved");
    NBTTagList invList = compound.getTagList("fluidGhostSlots", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < invList.tagCount(); i++) {
      NBTTagCompound stackTag = invList.getCompoundTagAt(i);
      int slot = stackTag.getByte("Slot");
      FluidWrapper wrapper = FluidWrapper.loadStackWrapperFromNBT(stackTag);
      if (wrapper == null) {
        wrapper = new FluidWrapper();
      }
      stacksWrapped.set(slot, wrapper);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger("transferSaved", this.transferRate);
    NBTTagList invList = new NBTTagList();
    for (int i = 0; i < this.getWrapperCount(); i++) {
      NBTTagCompound stackTag = new NBTTagCompound();
      stackTag.setByte("Slot", (byte) i);
      if (this.getStackWrapper(i) != null)
        this.getStackWrapper(i).writeToNBT(stackTag);
      invList.appendTag(stackTag);
    }
    compound.setTag("fluidGhostSlots", invList);
    return super.writeToNBT(compound);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case TRANSFER_RATE:
        return this.transferRate;
      case FILTERTYPE:
        return this.filterType;
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
        if (value > 0)
          transferRate = value;
      break;
      case FILTERTYPE:
        this.filterType = value % 2;
      break;
    }
  }

  private boolean isWhitelist() {
    //default is zero, and default blacklist makes sense -> it is empty, so everythings allowed
    return this.filterType == 1;
  }

  private List<FluidStack> getFilterNonempty() {
    List<FluidStack> filt = new ArrayList<>();
    for (FluidWrapper wrap : this.stacksWrapped) {
      if (wrap.isEmpty() == false) {
        filt.add(wrap.getStack().copy());
      }
    }
    return filt;
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
  public int getWrapperCount() {
    return stacksWrapped.size();
  }

  @Override
  public FluidWrapper getStackWrapper(int i) {
    FluidWrapper f = this.stacksWrapped.get(i);
    return f;
  }

  @Override
  public void setStackWrapper(int i, FluidWrapper stack) {
    if (stack == null)
      stacksWrapped.set(i, new FluidWrapper());
    else
      stacksWrapped.set(i, stack);
  }
}
