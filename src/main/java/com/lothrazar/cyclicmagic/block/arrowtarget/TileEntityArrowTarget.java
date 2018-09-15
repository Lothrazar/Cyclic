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
package com.lothrazar.cyclicmagic.block.arrowtarget;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class TileEntityArrowTarget extends TileEntityBaseMachineInvo implements ITickable {

  public static final int TIMER_MAX = 10;

  public static enum Fields {
    TIMER, POWER;
  }

  private int power;

  public TileEntityArrowTarget() {
    super(0);
    timer = 0;
    power = 0;
  }

  public int getPower() {
    return this.power;
  }

  public void setPower(int redstone) {
    this.timer = TIMER_MAX;
    power = redstone;
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    //oldState.getBlock() instanceof BlockRedstoneClock &&
    return !(newSate.getBlock() instanceof BlockArrowTarget);// : oldState != newSate;
  }

  @Override
  public void update() {
    if (this.isValid() == false) {
      return;
    }
    this.timer--;
    if (timer < 0) {
      timer = 0;
    }
    boolean poweredNow = timer > 0;
    boolean prevPowered = world.getBlockState(pos).getValue(BlockArrowTarget.POWERED);
    if (prevPowered != poweredNow) {
      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockArrowTarget.POWERED, poweredNow));
      //super weird hotfix for down state not updating
      //all other directions read update, but not down apparently!
      world.notifyNeighborsOfStateChange(pos.down(), world.getBlockState(pos.down()).getBlock(), true);
    }
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case POWER:
        return power;
      case TIMER:
        return timer;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case POWER:
        power = MathHelper.clamp(value, 0, 16);
      break;
      case TIMER:
        timer = MathHelper.clamp(value, 0, TIMER_MAX);
      break;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("power", power);
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    power = compound.getInteger("power");
  }
}
