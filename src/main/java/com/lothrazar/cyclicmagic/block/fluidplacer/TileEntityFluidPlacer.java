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
package com.lothrazar.cyclicmagic.block.fluidplacer;

import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.liquid.FluidTankBase;
import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityFluidPlacer extends TileEntityBaseMachineFluid implements ITickable {

  public TileEntityFluidPlacer() {
    super(0);
    tank = new FluidTankBase(Fluid.BUCKET_VOLUME);
  }

  @Override
  public void update() {
    if (this.isPowered() == false ||
        tank.getFluid() == null ||
        tank.getFluid().getFluid() == null) {
      return;
    }
    EnumFacing facingTo = this.getCurrentFacing().getOpposite();
    BlockPos posTarget = pos.offset(facingTo);
    if (world.isAirBlock(posTarget) == false) {
      return;
    }
    FluidStack maybeDrain = tank.drain(new FluidStack(tank.getFluid().getFluid(), Fluid.BUCKET_VOLUME), false);
    if (maybeDrain != null && maybeDrain.amount == Fluid.BUCKET_VOLUME) {
      Block fluidBlock = tank.getFluid().getFluid().getBlock();
      //if we can drain a full bucket, then do it and place!
      world.setBlockState(posTarget, fluidBlock.getDefaultState());
      tank.drain(new FluidStack(tank.getFluid().getFluid(), Fluid.BUCKET_VOLUME), true);
    }
  }
}
