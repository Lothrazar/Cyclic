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
package com.lothrazar.cyclicmagic.block.screentarget;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.ITileTextbox;
import com.lothrazar.cyclicmagic.item.location.ItemLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityScreenTarget extends TileEntityBaseMachineInvo implements ITileTextbox, ITickable {

  public static final int SLOT_TRANSFER = 0;
  private String text = "";
  private int red = 100;
  private int green = 100;
  private int blue = 100;
  private int paddingX = 0;
  private int paddingY = 0;

  public static enum Justification {
    LEFT, CENTER, RIGHT;
  }

  public static enum Fields {
    RED, GREEN, BLUE, PADDINGX, PADDINGY;
  }

  public TileEntityScreenTarget() {
    super(1);
  }

  BlockPosDim getTarget(int slot) {
    return ItemLocation.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() instanceof ItemLocation;
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String s) {
    text = s;
  }

  public int getColor() {
    //TODO: fix maybe? IllegalArgumentException: Color parameter outside of expected range
    //    return new java.awt.Color(red, green, blue).getRGB();
    return ((red & 0xFF) << 16) | //red
        ((green & 0xFF) << 8) | //green
        ((blue & 0xFF) << 0);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    text = tags.getString("text");
    red = tags.getInteger("red");
    green = tags.getInteger("green");
    blue = tags.getInteger("blue");
    paddingX = tags.getInteger("paddingx");
    paddingY = tags.getInteger("paddingy");
    int just = tags.getInteger("justif");

  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString("text", text);
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("paddingx", paddingX);
    tags.setInteger("paddingy", paddingY);
    return super.writeToNBT(tags);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case BLUE:
        return blue;
      case GREEN:
        return green;
      case RED:
        return red;
      case PADDINGX:
        return this.paddingX;
      case PADDINGY:
        return this.paddingY;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case BLUE:
        blue = value;
      break;
      case GREEN:
        green = value;
      break;
      case RED:
        red = value;
      break;
      case PADDINGX:
        paddingX = value;
      break;
      case PADDINGY:
        paddingY = value;
      break;
    }
  }


  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    BlockPosDim target = this.getTarget(SLOT_TRANSFER);
    if (target == null || target.getDimension() != world.provider.getDimension()) {
      return;
    }
    // 
    TileEntity te = world.getTileEntity(target.toBlockPos());
    if (te == null) {
      return;
    }
    if (te.hasCapability(CapabilityEnergy.ENERGY, EnumFacing.UP)) {
      IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
      //therefore  
      this.text = energy.getEnergyStored() + "/" + energy.getMaxEnergyStored()
          + System.lineSeparator();
    }

    if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
      IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
      //therefore  
      int max = itemHandler.getSlots();
      int empty = 0;
      for (int i = 0; i < max; i++) {
        if (itemHandler.getStackInSlot(i).isEmpty()) {
          empty++;
        }
      }
      this.text += empty + "/" + max
          + System.lineSeparator();

    }
    if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP)) {
      IFluidHandler energy = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.UP);
      //therefore   
      for (IFluidTankProperties f : energy.getTankProperties()) {
        this.text += f.getContents().getLocalizedName() + " " + f.getContents().amount + "/" + f.getCapacity()
            + System.lineSeparator();
      }
    }
  }
}
