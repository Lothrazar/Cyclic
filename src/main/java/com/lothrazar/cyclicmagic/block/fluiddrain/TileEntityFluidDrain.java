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
package com.lothrazar.cyclicmagic.block.fluiddrain;

import java.util.Collections;
import java.util.List;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.data.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.liquid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import com.lothrazar.cyclicmagic.util.UtilShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityFluidDrain extends TileEntityBaseMachineFluid implements ITileRedstoneToggle, ITickable, ITilePreviewToggle {

  public static final int TANK_FULL = MENERGY;
  private int radius = 7;
  private int depth = 4;
  private int shapePtr = 0;
  private List<BlockPos> shape = null;

  public static enum Fields {
    REDSTONE, RENDERPARTICLES;
  }

  public TileEntityFluidDrain() {
    super(18);
    this.setSlotsForInsert(0, 18);
    tank = new FluidTankFixDesync(TANK_FULL, this);
    tank.setTileEntity(this);
    this.initEnergy(new EnergyStore(MENERGY, MENERGY, MENERGY),
        BlockFluidDrain.FUEL_COST);
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
    this.shiftAllUp();
    if (this.isRunning() == false
        || this.tank.isFull()
        || this.getStackInSlot(0).getItem() instanceof ItemBlock == false) {
      return;
    }
    if (shape == null) {
      shape = this.getShape();
    }
    //look for fluid 
    if (this.shapePtr >= shape.size()) {
      shapePtr = 0;
      return;
    }
    BlockPos current = shape.get(shapePtr);
    shapePtr++;
    IBlockState currentState = world.getBlockState(current);
    if (currentState.getMaterial().isLiquid()
        && this.updateEnergyIsBurning()) {
      UtilParticle.spawnParticle(world, EnumParticleTypes.WATER_BUBBLE, current);
      IFluidHandler handle = FluidUtil.getFluidHandler(world, current, EnumFacing.UP);
      FluidStack fs = handle.getTankProperties()[0].getContents();
      if (fs == null || tank.canFillFluidType(fs) == false) {
        return;
      }
      this.tank.fill(fs, true);
      UtilPlaceBlocks.placeItemblock(world, current, this.getStackInSlot(0), null);
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    this.needsRedstone = compound.getInteger(NBT_REDST);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return this.needsRedstone;
      case RENDERPARTICLES:
        return this.renderParticles;
      default:
      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case RENDERPARTICLES:
        this.renderParticles = value % 2;
      break;
      default:
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

  public float getFillRatio() {
    return tank.getFluidAmount() / tank.getCapacity();
  }

  @Override
  public boolean isPreviewVisible() {
    return renderParticles == 1;
  }

  @Override
  public List<BlockPos> getShape() {
    List<BlockPos> circle = UtilShape.cubeFilled(pos.down(depth), radius, depth);
    Collections.reverse(circle);
    return circle;
  }
}
