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
package com.lothrazar.cyclicmagic.block.laser;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.data.BlockPosDim;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityLaser extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static enum Fields {
    REDSTONE, TIMER, R, G, B, ALPHA, PULSE;
  }

  private int needsRedstone = 0;
  private int red = 255;
  private int green = 0;
  private int blue = 0;
  private int alpha = 30;//1-100 will become 0-1
  private boolean isPulsing = true;

  public TileEntityLaser() {
    super(4);
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
  }

  BlockPosDim getTarget(int slot) {
    return ItemLocation.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger("redst");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger("redst", this.needsRedstone);
    return super.writeToNBT(tagCompound);
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
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return this.timer;
      case REDSTONE:
        return this.needsRedstone;
      case B:
        return blue;
      case G:
        return green;
      case R:
        return red;
      case ALPHA:
        return alpha;
      case PULSE:
        return isPulsing ? 1 : 0;
      default:
      break;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        this.timer = value;
      break;
      case REDSTONE:
        this.needsRedstone = value;
      break;
      case B:
        blue = value;
      break;
      case G:
        green = value;
      break;
      case R:
        red = value;
      break;
      case ALPHA:
        alpha = value;
      break;
      case PULSE:
        isPulsing = (value == 1);
      break;
      default:
      break;
    }
  }

  public boolean isPulsing() {
    return isPulsing; 
  }

  public float alphaCalculated() {
    return alpha / 100.0F;
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(getFieldCount());
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  public float[] getColor() {
   return new float[]{
       this.red/255.0F,
       this.green/255.0F,
       this.blue /255.0F
    };
   }
}
