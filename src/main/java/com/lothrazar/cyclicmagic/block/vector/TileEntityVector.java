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
package com.lothrazar.cyclicmagic.block.vector;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.nbt.NBTTagCompound;

/**
 * PLAN: gui to change power and vector.
 * 
 * make sure it saves data when you harvest and place
 * 
 * @author Sam
 *
 */
public class TileEntityVector extends TileEntityBaseMachineInvo implements ITileRedstoneToggle {

  public static final int MAX_ANGLE = 90;
  public static final int MAX_YAW = 360;
  public static final int MAX_POWER = 300;///999 is op, maybe upgraes or config later
  public static final int DEFAULT_ANGLE = 45;
  public static final int DEFAULT_YAW = 90;
  public static final int DEFAULT_POWER = 60;
  public static final String NBT_ANGLE = "vectorAngle";
  public static final String NBT_POWER = "vectorPower";
  public static final String NBT_YAW = "vectorYaw";
  public static final String NBT_RED = "redst";
  private int angle = DEFAULT_ANGLE;
  private int power = DEFAULT_POWER;
  private int yaw = DEFAULT_YAW;
  public static final String NBT_SOUND = "sound";
  private int playSound = 1;
  private int needsRedstone = 0;

  public static enum Fields {
    ANGLE, POWER, YAW, SOUND, REDSTONE;
  }

  public TileEntityVector() {
    super(0);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    power = tagCompound.getInteger(NBT_POWER);
    angle = tagCompound.getInteger(NBT_ANGLE);
    yaw = tagCompound.getInteger(NBT_YAW);
    playSound = tagCompound.getInteger(NBT_SOUND);
    needsRedstone = tagCompound.getInteger(NBT_RED);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_POWER, power);
    tagCompound.setInteger(NBT_ANGLE, angle);
    tagCompound.setInteger(NBT_YAW, yaw);
    tagCompound.setInteger(NBT_SOUND, playSound);
    tagCompound.setInteger(NBT_RED, needsRedstone);
    return super.writeToNBT(tagCompound);
  }

  public float getActualPower() {//stored as integer. used as decimal from 0.01 and up
    float actual = ((float) power) / 100F;//over 100 so that power 100 is = 1 and so on
    //also divide by 3 bc 999 is overpowered. so maximum actual is 333
    //actual = Math.max(actual / 3F, 0.01F);//but not lower than 1. so 1-5 is the same, is fine
    return actual;
  }

  public int getPower() {
    return power;
  }

  public int getAngle() {
    return angle;
  }

  public int getYaw() {
    return yaw;
  }

  public boolean playSound() {
    return this.playSound == 1;
  }

  @Override
  public int getField(int id) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case ANGLE:
          return angle;
        case POWER:
          return power;
        case YAW:
          return yaw;
        case SOUND:
          return playSound;
        case REDSTONE:
          return needsRedstone;
      }
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    if (id >= 0 && id < this.getFieldCount()) {
      switch (Fields.values()[id]) {
        case ANGLE:
          this.angle = Math.min(value, MAX_ANGLE);
        break;
        case POWER:
          this.power = Math.min(value, MAX_POWER);
          if (this.power <= 0) this.power = 1;
        break;
        case YAW:
          this.yaw = Math.min(value, MAX_YAW);
        break;
        case SOUND:
          this.playSound = value;
        break;
        case REDSTONE:
          this.needsRedstone = value;
        break;
      }
    }
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void toggleNeedsRedstone() {
    int val = this.needsRedstone + 1;
    if (val > 1) {
      val = 0;//hacky lazy way
    }
    this.setField(Fields.REDSTONE.ordinal(), val);
  }

  public boolean onlyRunIfPowered() {
    return this.needsRedstone == 1;
  }
}
