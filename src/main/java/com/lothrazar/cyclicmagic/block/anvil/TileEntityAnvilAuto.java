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
package com.lothrazar.cyclicmagic.block.anvil;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;

public class TileEntityAnvilAuto extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  public static final int TANK_FULL = 10000;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;

  public static enum Fields {
    REDSTONE, FUEL;
  }

  public TileEntityAnvilAuto() {
    super(2);
    this.initEnergy(BlockAnvilAuto.FUEL_COST);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
  }


  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    ItemStack inputStack = this.getStackInSlot(SLOT_INPUT);
    if (UtilRepairItem.canRepair(inputStack) == false) {
      //move the non repairable deelio outta here 
      if (this.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
        //delete bug fix
        this.setInventorySlotContents(SLOT_OUTPUT, this.removeStackFromSlot(SLOT_INPUT));
      }
      return;
    }
    if (inputStack.isEmpty()) {
      return;//no paying cost on empty work
    }
    //pay energy each tick
    if (this.updateEnergyIsBurning()) {
      UtilRepairItem.doRepair(inputStack);
    }
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        return needsRedstone;
      case FUEL:
        return this.getEnergyCurrent();
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
      case FUEL:
        this.setEnergyCurrent(value);
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
}
