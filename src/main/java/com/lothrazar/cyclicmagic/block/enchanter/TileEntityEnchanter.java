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
package com.lothrazar.cyclicmagic.block.enchanter;

import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.fluid.FluidTankBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TileEntityEnchanter extends TileEntityBaseMachineFluid implements ITickable, ITileRedstoneToggle {

  public static final int TANK_FULL = 10000;
  //20mb per xp following convention set by EnderIO; OpenBlocks; and Reliquary https://github.com/PrinceOfAmber/Cyclic/issues/599
  public static final int TIMER_FULL = 22;
  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  private static final int MAX_LEVEL = 30;
  public static int FLUID_COST = 300;

  public static enum Fields {
    TIMER, REDSTONE, FUELDISPLAY;
  }

  private int timer = 0;
  private int needsRedstone = 0;

  public TileEntityEnchanter() {
    super(2);
    this.initEnergyWithCost(BlockEnchanter.FUEL_COST);
    this.setSlotsForExtract(SLOT_OUTPUT);
    this.setSlotsForInsert(SLOT_INPUT);
    tank = new FluidTankBase(TANK_FULL);
    tank.setFluidAllowed(FluidRegistry.getFluid("xpjuice"));
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
    if (inputStack.isEmpty() || this.hasEnoughFluid() == false) {
      return;//no paying cost on empty work
    }
    if (this.updateFuelIsBurning() == false) {
      return;
    }

    this.timer--;
    if (this.timer <= 0) {
      this.timer = TIMER_FULL;
      ItemStack outputStack = ItemStack.EMPTY;
      if ((inputStack.getItem() == Items.BOOK || inputStack.isItemEnchantable()) &&
          this.hasEnoughFluid() &&
          this.getStackInSlot(SLOT_OUTPUT).isEmpty()) {
        //pay item cost and build the enchatned output item
        outputStack = inputStack.copy();
        outputStack.setCount(1);
        inputStack.shrink(1);
        outputStack = EnchantmentHelper.addRandomEnchantment(world.rand, outputStack, MAX_LEVEL, true);
        this.setInventorySlotContents(SLOT_INPUT, inputStack);
        this.setInventorySlotContents(SLOT_OUTPUT, outputStack);
        this.drain(FLUID_COST, true);
      }
    }
  }

  private boolean hasEnoughFluid() {
    FluidStack contains = this.tank.getFluid();
    return (contains != null && contains.amount >= FLUID_COST);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setInteger(NBT_TIMER, timer);

    tags.setInteger(NBT_REDST, this.needsRedstone);
    return super.writeToNBT(tags);
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    timer = tags.getInteger(NBT_TIMER);

    this.needsRedstone = tags.getInteger(NBT_REDST);
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }



  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case TIMER:
        return timer;

      case REDSTONE:
        return needsRedstone;

      case FUELDISPLAY:
        return this.fuelDisplay;
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
        this.needsRedstone = value % 2;
      break;

      case FUELDISPLAY:
        this.fuelDisplay = value % 2;
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
