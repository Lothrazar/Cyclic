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
package com.lothrazar.cyclicmagic.energy.peat.generator;

import com.lothrazar.cyclicmagic.block.EnergyStore;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileEntityPeatGenerator extends TileEntityBaseMachineInvo implements ITickable {

  public final static int TIMER_FULL = 200;
  public final static int SLOT_INPUT = 0;
  //FOR REFERENCERF tools coal gen is 64 per tick
  //Thermal magmatic dynamo makes 40 per tick from lava
  //forestry engine 20/tick
  public static final int PER_TICK = 128;
  //total energy made per item is PER_TICK * TIMER_FULL
  private static final int CAPACITY = PER_TICK * 100;
  //output slower than we generate
  private static final int TRANSFER_ENERGY_PER_TICK = PER_TICK / 2;

  public static enum Fields {
    TIMER;
  }

  public TileEntityPeatGenerator() {
    super(1);
    this.setSlotsForInsert(SLOT_INPUT);
    this.initEnergy();
    energyStorage = new EnergyStore(CAPACITY);
    timer = 0;
  }

  @Override
  public void update() {
    if (isValid() == false) {
      return;
    }
    this.tryOutputPower();
    // only if burning peat 
    if (timer > 0) {
      int actuallyGained = this.energyStorage.receiveEnergy(PER_TICK, true);
      if (actuallyGained == PER_TICK) {
        this.spawnParticlesAbove();
        // either we have room to eat everything that generated, or we didnt.
        //if we did, burn some fuel. if not, wait for more room in battery
        this.energyStorage.receiveEnergy(PER_TICK, false);
        timer--;
      }
    }
    if (timer == 0) {
      //timer is zero. ok so it STAYS zero unless we can eat another peat brick
      // update fuel stuffs  
      if (this.isValidFuel(this.getStackInSlot(SLOT_INPUT))) {
        this.decrStackSize(SLOT_INPUT);
        timer = TIMER_FULL;
      }
    }
  }

  private void tryOutputPower() {
    // TODO share code in base class somehow? with CableBase maybe?
    for (EnumFacing f : EnumFacing.values()) {
      BlockPos posTarget = pos.offset(f);
      IEnergyStorage handlerHere = this.getCapability(CapabilityEnergy.ENERGY, f);
      TileEntity tileTarget = world.getTileEntity(posTarget);
      if (tileTarget == null) {
        continue;
      }
      IEnergyStorage handlerOutput = tileTarget.getCapability(CapabilityEnergy.ENERGY, f);
      if (handlerHere != null && handlerOutput != null
          && handlerHere.canExtract() && handlerOutput.canReceive()) {
        //first simulate
        int drain = handlerHere.extractEnergy(TRANSFER_ENERGY_PER_TICK, true);
        if (drain > 0) {
          //now push it into output, but find out what was ACTUALLY taken
          int filled = handlerOutput.receiveEnergy(drain, false);
          //now actually drain that much from here
          handlerHere.extractEnergy(filled, false);
          if (tileTarget instanceof TileEntityCableBase) {
            //TODO: not so compatible with other fluid systems. itl do i guess
            TileEntityCableBase cable = (TileEntityCableBase) tileTarget;
            if (cable.isEnergyPipe())
              cable.updateIncomingEnergyFace(f.getOpposite());
          }
          //              return;// stop now because only pull from one side at a time
        }
      }
    }
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return isValidFuel(stack);
  }

  private boolean isValidFuel(ItemStack peat) {
    return peat.getItem().equals(Item.getByNameOrId(Const.MODRES + "peat_fuel"));
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
