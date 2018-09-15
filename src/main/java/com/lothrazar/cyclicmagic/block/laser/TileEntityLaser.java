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

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.OffsetEnum;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityLaser extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static final int MAX_TIMER = 100;

  public static enum Fields {
    REDSTONE, TIMER, R, G, B, ALPHA, PULSE, EXTENDING, XOFF, YOFF, ZOFF;
  }

  private int needsRedstone = 0;
  private int red = 255;
  private int green = 0;
  private int blue = 0;
  private int alpha = 30;//1-100 will become 0-1
  private boolean isPulsing = true;
  private boolean isExtending = false;
  private OffsetEnum xOffset = OffsetEnum.CENTER;
  private OffsetEnum yOffset = OffsetEnum.CENTER;
  private OffsetEnum zOffset = OffsetEnum.CENTER;

  public TileEntityLaser() {
    super(4);
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    timer++;
    if (timer > MAX_TIMER) {
      timer = 0;
    }
  }

  BlockPosDim getTarget(int slot) {
    return ItemLocation.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    this.needsRedstone = tags.getInteger("redst");
    red = tags.getInteger("red");
    green = tags.getInteger("green");
    blue = tags.getInteger("blue");
    alpha = tags.getInteger("alpha");
    isPulsing = tags.getBoolean("puls");
    isExtending = tags.getBoolean("extend");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger("redst", this.needsRedstone);
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("alpha", alpha);
    tags.setBoolean("puls", isPulsing);
    tags.setBoolean("extend", isExtending);
    return super.writeToNBT(tags);
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
      case EXTENDING:
        return isExtending ? 1 : 0;
      case XOFF:
        return this.xOffset.ordinal();
      case YOFF:
        return this.yOffset.ordinal();
      case ZOFF:
        return this.zOffset.ordinal();
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
      case EXTENDING:
        isExtending = (value == 1);
      break;
      case XOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.xOffset = OffsetEnum.values()[value];
      break;
      case YOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.yOffset = OffsetEnum.values()[value];
      break;
      case ZOFF:
        if (value >= OffsetEnum.values().length)
          value = 0;
        this.zOffset = OffsetEnum.values()[value];
      break;
      default:
      break;
    }
  }

  public boolean isPulsing() {
    return isPulsing;
  }

  public boolean isExtending() {
    return isExtending;
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

  public OffsetEnum getxOffset() {
    return this.xOffset;
  }

  public OffsetEnum getyOffset() {
    return this.yOffset;
  }

  public OffsetEnum getzOffset() {
    return this.zOffset;
  }

  public float[] getColor() {
    return new float[] {
        this.red / 255.0F,
        this.green / 255.0F,
        this.blue / 255.0F
    };
  }
}
