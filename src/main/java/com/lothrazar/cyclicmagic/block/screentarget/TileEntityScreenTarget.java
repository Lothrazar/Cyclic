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
import com.lothrazar.cyclicmagic.item.locationgps.ItemLocationGps;
import com.lothrazar.cyclicmagic.util.Const;
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
  private int style;
  private int showType, xp, yp;
  private int fontSize = 30;
  private int offset = 0;

  public static enum Fields {
    RED, GREEN, BLUE, SHOWTYPE, STYLE, FONT, XPADDING, YPADDING, OFFSET;
  }

  public TileEntityScreenTarget() {
    super(1);
  }

  BlockPosDim getTarget(int slot) {
    return ItemLocationGps.getPosition(this.getStackInSlot(slot));
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return stack.getItem() instanceof ItemLocationGps;
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
    return super.getFieldArray(getFieldCount());
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
    style = tags.getInteger("style");
    showType = tags.getInteger("showtype");
    fontSize = tags.getInteger("font");
    xp = tags.getInteger("xp");
    yp = tags.getInteger("yp");
    offset = tags.getInteger("offset");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString("text", text);
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("style", style);
    tags.setInteger("showtype", showType);
    tags.setInteger("font", fontSize);
    tags.setInteger("xp", xp);
    tags.setInteger("yp", yp);
    tags.setInteger("offset", offset);
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
      case STYLE:
        return this.style;
      case SHOWTYPE:
        return this.showType;
      case FONT:
        return fontSize;
      case XPADDING:
        return xp;
      case YPADDING:
        return yp;
      case OFFSET:
        return offset;
      default:
      break;
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
      case STYLE:
        this.style = value % EnumDisplayStyle.values().length;
      break;
      case SHOWTYPE:
        this.showType = value % EnumShowType.values().length;
      break;
      case FONT:
        this.fontSize = value;
      break;
      case XPADDING:
        xp = value;
      break;
      case YPADDING:
        yp = value;
      break;
      case OFFSET:
        offset = value;
      break;
    }
  }

  @Override
  public void update() {
    if (isRunning() == false) {
      return;
    }
    if (this.world.getTotalWorldTime() % Const.TICKS_PER_SEC != 0) {
      return;
    }
    BlockPosDim target = this.getTarget(SLOT_TRANSFER);
    TileEntity te = getTargetTile(target);
    if (te == null) {
      this.text = "";
      return;
    }
    updateText(te, target);
  }

  private void updateText(TileEntity te, BlockPosDim target) {
    switch (showType()) {
      case ENERGY:
        this.text = getEnergyString(te, target);
      break;
      case FLUID:
        this.text = getFluidStr(te, target);
      break;
      case ITEM:
        this.text = getItemStr(te, target);
      break;
    }
  }

  private TileEntity getTargetTile(BlockPosDim target) {
    if (target == null || target.getDimension() != world.provider.getDimension()) {
      return null;
    }
    return world.getTileEntity(target.toBlockPos());
  }

  private String getItemStr(TileEntity te, BlockPosDim target) {
    String itemStr;
    EnumFacing side = target.getSide() == null ? EnumFacing.UP : target.getSide();
    if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
      IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
      //therefore  
      int max = itemHandler.getSlots();
      int empty = 0;
      for (int i = 0; i < max; i++) {
        if (itemHandler.getStackInSlot(i).isEmpty()) {
          empty++;
        }
      }
      itemStr = this.formatQuantity(max - empty, max);
    }
    else {
      itemStr = "--";
    }
    return itemStr;
  }

  private String getFluidStr(TileEntity te, BlockPosDim target) {
    String fluidStr = "";
    EnumFacing side = target.getSide() == null ? EnumFacing.UP : target.getSide();
    if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
      IFluidHandler energy = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
      //therefore   
      for (IFluidTankProperties f : energy.getTankProperties()) {
        if (f == null || f.getContents() == null) {
          continue;
        }
        fluidStr += this.formatQuantity(f.getContents().amount, f.getCapacity());
        break;
      }
    }
    else {
      fluidStr = "--";
    }
    return fluidStr;
  }

  private String getEnergyString(TileEntity te, BlockPosDim target) {
    String energyStr;
    EnumFacing side = target.getSide() == null ? EnumFacing.UP : target.getSide();
    if (te.hasCapability(CapabilityEnergy.ENERGY, side)) {
      IEnergyStorage energy = te.getCapability(CapabilityEnergy.ENERGY, side);
      //therefore   
      energyStr = this.formatQuantity(energy.getEnergyStored(), energy.getMaxEnergyStored());
    }
    else {
      energyStr = "--";
    }
    return energyStr;
  }

  public EnumShowType showType() {
    return EnumShowType.values()[this.showType];
  }

  public EnumDisplayStyle displayStyle() {
    return EnumDisplayStyle.values()[this.style];
  }

  private String formatQuantity(final float current, final float max) {
    if (this.style == EnumDisplayStyle.PERCENT.ordinal()) {
      float pct = current / max * 100.0F;
      return String.format("%.2f", pct) + "%";
    }
    else {
      String qty = ((int) current) + "";
      if (this.style == EnumDisplayStyle.MAX.ordinal()) {
        qty += "/" + ((int) max);
      }
      return qty;
    }
  }

  public float getFontSize() {
    return fontSize / 1000.0F;
  }

  public float getXOffset() {
    return xp / 100F;
  }

  public float getYOffset() {
    return yp / -100F;
  }

  @Override
  public int getInventoryStackLimit() {
    return 1;
  }
}
