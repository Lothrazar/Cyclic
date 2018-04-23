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
package com.lothrazar.cyclicmagic.block.tank;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.liquid.FluidTankFixDesync;
import com.lothrazar.cyclicmagic.core.util.UtilFluid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.Fluid;

public class TileEntityBucketStorage extends TileEntityBaseMachineFluid implements ITickable {

  public static final int TRANSFER_FLUID_PER_TICK = 500;

  public TileEntityBucketStorage() {
    super(0);
    tank = new FluidTankFixDesync(Fluid.BUCKET_VOLUME * 64, this);
  }

  @Override
  public void update() {
    //drain below but only to one of myself
    TileEntity below = this.world.getTileEntity(this.pos.down());
    if (below != null && below instanceof TileEntityBucketStorage) {
      UtilFluid.tryFillPositionFromTank(world, this.pos.down(), EnumFacing.UP, tank, TRANSFER_FLUID_PER_TICK);
    }
  }
}
