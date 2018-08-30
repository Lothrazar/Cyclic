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
package com.lothrazar.cyclicmagic.block.dice;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;

public class TileEntityDice extends TileEntityBaseMachineInvo implements ITickable {

  private static final int TICKS_MAX_SPINNING = 45;
  private static final int TICKS_PER_CHANGE = 4;
  private static final String NBT_PART = "is_spinning";

  public static enum Fields {
    TIMER, SPINNING;
  }

  private int timer = 0;
  private int spinningIfZero = 1;

  public TileEntityDice() {
    super(0);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void update() {
    if (this.timer == 0) {
      this.spinningIfZero = 1;
      world.updateComparatorOutputLevel(pos, this.blockType);
    }
    else {
      this.timer--;
      //toggle block state
      if (this.timer % TICKS_PER_CHANGE == 0) {
        this.spinningIfZero = 0;
        EnumFacing fac = BlockDice.getRandom(world.rand);
        IBlockState stateold = world.getBlockState(pos);
        IBlockState newstate = stateold.withProperty(BlockDice.PROPERTYFACING, fac);
        world.setBlockState(pos, newstate);
        //        world.notifyBlockUpdate(pos, stateold, newstate, 3);
      }
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);
    tags.setInteger(NBT_PART, this.spinningIfZero);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);
    spinningIfZero = tags.getInteger(NBT_PART);
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          return timer;
        case SPINNING:
          return this.spinningIfZero;
      }
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case TIMER:
          this.timer = value;
        break;
        case SPINNING:
          spinningIfZero = value;
        break;
      }
    }
  }

  public void startSpinning() {
    timer = TICKS_MAX_SPINNING;
    spinningIfZero = 0;
  }

  public boolean isSpinning() {
    return spinningIfZero == 0;
  }
}
