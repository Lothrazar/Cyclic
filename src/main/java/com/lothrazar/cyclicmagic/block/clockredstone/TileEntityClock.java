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
package com.lothrazar.cyclicmagic.block.clockredstone;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityClock extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static enum Fields {
    TIMER, TOFF, TON, POWER, REDSTONE, N, E, S, W, U, D;
  }

  private int timeOff;//dont let these times be zero !!!
  private int timeOn;
  private int power;
  private int needsRedstone = 0;
  private Map<EnumFacing, Boolean> poweredSides = new HashMap<EnumFacing, Boolean>();

  public TileEntityClock() {
    super(0);
    timer = 0;
    timeOff = 60;
    timeOn = 60;
    power = 15;
    this.facingResetAllOn();
  }

  public int getPower() {
    return this.power;
  }

  public int getPowerForSide(EnumFacing side) {
    if (this.getSideHasPower(side))
      return this.power;
    else
      return 0;
  }

  public boolean getSideHasPower(EnumFacing side) {
    return this.poweredSides.get(side);
  }

  public int getSideField(EnumFacing side) {
    return this.getSideHasPower(side) ? 1 : 0;
  }

  public void setSideField(EnumFacing side, int pow) {
    this.poweredSides.put(side, (pow == 1));
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
    //oldState.getBlock() instanceof BlockRedstoneClock &&
    return !(newSate.getBlock() instanceof BlockRedstoneClock);// : oldState != newSate;
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (this.power == 0) {
      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRedstoneClock.POWERED, false));
      return;
    }
    this.timer++;
    boolean powered;
    boolean prevPowered = world.getBlockState(pos).getValue(BlockRedstoneClock.POWERED);
    if (timer < timeOff) {
      powered = false;
    }
    else if (timer < timeOff + timeOn) {
      //we are in the ON section
      powered = true;
    }
    else {
      timer = 0;
      powered = false;
    }
    if (prevPowered != powered) {
      world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRedstoneClock.POWERED, powered));
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
      case TOFF:
        return timeOff;
      case TON:
        return timeOn;
      case REDSTONE:
        return this.needsRedstone;
      case D:
        return this.getSideField(EnumFacing.DOWN);
      case E:
        return this.getSideField(EnumFacing.EAST);
      case N:
        return this.getSideField(EnumFacing.NORTH);
      case S:
        return this.getSideField(EnumFacing.SOUTH);
      case U:
        return this.getSideField(EnumFacing.UP);
      case W:
        return this.getSideField(EnumFacing.WEST);
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case POWER:
        if (value < 0) {
          value = 0;
        }
        if (value > 15) {
          value = 15;
        }
        power = value;
      break;
      case TIMER:
        timer = value;
      break;
      case TOFF:
        timeOff = Math.max(value, 1);
      break;
      case TON:
        timeOn = Math.max(value, 1);
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      case D:
        this.setSideField(EnumFacing.DOWN, value % 2);
      break;
      case E:
        this.setSideField(EnumFacing.EAST, value % 2);
      break;
      case N:
        this.setSideField(EnumFacing.NORTH, value % 2);
      break;
      case S:
        this.setSideField(EnumFacing.SOUTH, value % 2);
      break;
      case U:
        this.setSideField(EnumFacing.UP, value % 2);
      break;
      case W:
        this.setSideField(EnumFacing.WEST, value % 2);
      break;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("off", timeOff);
    compound.setInteger("on", timeOn);
    compound.setInteger("power", power);
    compound.setInteger(NBT_REDST, needsRedstone);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setBoolean(f.getName(), poweredSides.get(f));
    }
    return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    timeOff = compound.getInteger("off");
    timeOn = compound.getInteger("on");
    power = compound.getInteger("power");
    needsRedstone = compound.getInteger(NBT_REDST);
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, compound.getBoolean(f.getName()));
    }
    if (this.detectAllOff()) {
      this.facingResetAllOn();//fix legacy data for one
    }
  }

  private boolean detectAllOff() {
    boolean areAnyOn = false;
    for (EnumFacing f : EnumFacing.values()) {
      areAnyOn = areAnyOn || poweredSides.get(f);
    }
    return !areAnyOn;
  }

  private void facingResetAllOn() {
    for (EnumFacing f : EnumFacing.values()) {
      poweredSides.put(f, true);
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
}
