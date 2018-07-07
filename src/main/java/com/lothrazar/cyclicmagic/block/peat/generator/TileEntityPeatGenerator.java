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
package com.lothrazar.cyclicmagic.block.peat.generator;

import com.lothrazar.cyclicmagic.block.battery.TileEntityBattery;
import com.lothrazar.cyclicmagic.block.peat.ItemPeatFuel;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;

public class TileEntityPeatGenerator extends TileEntityBaseMachineInvo implements ITickable {

  public final static int TIMER_FULL = 200;
  public final static int SLOT_INPUT = 0;
  //FOR REFERENCERF tools coal gen is 64 per tick
  //Thermal magmatic dynamo makes 40 per tick from lava
  //forestry engine 20/tick
  private int perTick = 128;
  //total energy made per item is PER_TICK * TIMER_FULL
  private static final int CAPACITY = TileEntityBattery.CAPACITY / 2;
  //output slower than we generate
  private static final int TRANSFER_ENERGY_PER_TICK = 128 * 8;

  public static enum Fields {
    TIMER;
  }

  public TileEntityPeatGenerator() {
    super(1);
    this.setSlotsForInsert(SLOT_INPUT);
    this.initEnergy(0, CAPACITY, false);
    timer = 0;
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    this.tryOutputPower(TRANSFER_ENERGY_PER_TICK);
    // only if burning peat 
    if (timer > 0) {
      int capacity = energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored();
      int actuallyGained = Math.min(perTick, capacity);
      // this.energyStorage.receiveEnergy(PER_TICK, true);
      if (actuallyGained == perTick) {
        timer--;
        this.spawnParticlesAbove();
        // either we have room to eat everything that generated, or we didnt.
        //if we did, burn some fuel. if not, wait for more room in battery
        energyStorage.setEnergyStored(energyStorage.getEnergyStored() + actuallyGained);
      }
    }
    if (timer == 0) {
      //timer is zero. ok so it STAYS zero unless we can eat another peat brick
      // update fuel stuffs  
      if (this.isValidFuel(this.getStackInSlot(SLOT_INPUT))) {
        this.perTick = this.getFuelValue(getStackInSlot(SLOT_INPUT));
        this.decrStackSize(SLOT_INPUT);
        timer = TIMER_FULL;
      }
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return isValidFuel(stack);
  }

  private int getFuelValue(ItemStack peat) {
    if (peat.getItem() instanceof ItemPeatFuel) {
      return ((ItemPeatFuel) peat.getItem()).getEnergyPerTick();
    }
    return 0;
  }

  private boolean isValidFuel(ItemStack peat) {
    return peat.getItem() instanceof ItemPeatFuel;//TODO: NBT tag for fuel having?
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    this.perTick = compound.getInteger("energy_perTick");
    super.readFromNBT(compound);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("energy_perTick", this.perTick);
    return super.writeToNBT(compound);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return getFieldOrdinals().length;
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;
    }
    return -1;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case TIMER:
        timer = value;
    }
  }
}
