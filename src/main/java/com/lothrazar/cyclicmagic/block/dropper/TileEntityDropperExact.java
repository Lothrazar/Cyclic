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
package com.lothrazar.cyclicmagic.block.dropper;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilItemStack;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

public class TileEntityDropperExact extends TileEntityBaseMachineInvo implements ITileRedstoneToggle, ITickable {

  private int needsRedstone = 1;
  private int slotCurrent = 0;
  private int dropCount = 1;
  private int delay = 10;
  private int hOffset = 0;

  public static enum Fields {
    TIMER, REDSTONE, DROPCOUNT, DELAY, OFFSET;
  }

  public TileEntityDropperExact() {
    super(9);
    this.initEnergy(BlockDropperExact.FUEL_COST);
    this.setSlotsForExtract(0, 8);
    this.setSlotsForInsert(0, 8);
    timer = delay;
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    if (this.updateEnergyIsBurning() == false) {
      return;
    }
    //TODO: not like this. find list of slots that are NONEMPTY and then pick one
    if (this.updateTimerIsZero()) {
      this.updateCurrentSlot();
      if (slotCurrent > -1 && this.getStackInSlot(slotCurrent).isEmpty() == false) {
        ItemStack dropMe = this.getStackInSlot(slotCurrent).copy();
        timer = delay;
        BlockPos target = this.getCurrentFacingPos().offset(this.getCurrentFacing(), hOffset);
        int amtDrop = Math.min(this.dropCount, dropMe.getCount());
        dropMe.setCount(amtDrop);
        UtilItemStack.dropItemStackMotionless(world, target, dropMe);
        this.decrStackSize(slotCurrent, amtDrop);
      }
    }
  }

  //same logic as dispenser
  private void updateCurrentSlot() {
    int j = 1;
    for (int k = 0; k < this.inv.size(); ++k) {
      if (!this.inv.get(k).isEmpty() && world.rand.nextInt(j++) == 0) {
        slotCurrent = k;
        return;
      }
    }
    slotCurrent = -1;
  }

  @Override
  public void readFromNBT(NBTTagCompound tagCompound) {
    super.readFromNBT(tagCompound);
    this.needsRedstone = tagCompound.getInteger(NBT_REDST);
    this.timer = tagCompound.getInteger(NBT_TIMER);
    this.delay = tagCompound.getInteger("delay");
    this.dropCount = tagCompound.getInteger("dropCount");
    this.hOffset = tagCompound.getInteger("hOffset");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
    tagCompound.setInteger(NBT_TIMER, timer);
    tagCompound.setInteger(NBT_REDST, this.needsRedstone);
    tagCompound.setInteger("delay", this.delay);
    tagCompound.setInteger("dropCount", this.dropCount);
    tagCompound.setInteger("hOffset", this.hOffset);
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
        return timer;
      case REDSTONE:
        return this.needsRedstone;
      case DELAY:
        return this.delay;
      case DROPCOUNT:
        return this.dropCount;
      case OFFSET:
        return this.hOffset;
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
      case DELAY:
        delay = Math.max(0, value);
      break;
      case DROPCOUNT:
        dropCount = Math.max(1, value);
      break;
      case OFFSET:
        hOffset = Math.max(0, value);
      break;
    }
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(getFieldCount());
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
}
