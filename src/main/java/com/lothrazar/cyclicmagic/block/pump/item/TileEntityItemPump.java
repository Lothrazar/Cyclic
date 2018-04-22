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
package com.lothrazar.cyclicmagic.block.pump.item;

import java.util.List;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.gui.ITileRedstoneToggle;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityItemPump extends TileEntityBaseMachineInvo implements ITickable, ITileRedstoneToggle {

  private static final int SLOT_TRANSFER = 0;
  private static int TRANSFER_ITEM_TICK_DELAY = 0;

  public static enum Fields {
    REDSTONE, FILTERTYPE;
  }

  static final int FILTER_SIZE = 9;
  private int itemTransferCooldown = 0;
  private int needsRedstone = 0;
  private int filterType = 0;

  public TileEntityItemPump() {
    super(1 + FILTER_SIZE);
    this.setSlotsForExtract(0);
    this.setSlotsForInsert(0);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case FILTERTYPE:
        return this.filterType;
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case FILTERTYPE:
        this.filterType = value % 2;
      break;
      case REDSTONE:
        this.needsRedstone = value % 2;
      break;
    }
  }

  private boolean isWhitelist() {
    //default is zero, and default blacklist makes sense -> it is empty, so everythings allowed
    return this.filterType == 1;
  }

  private boolean isStackInvalid(ItemStack stackToTest) {
    List<ItemStack> inventoryContents = getFilter();
    //edge case: if list is empty ?? should be covered already
    if (OreDictionary.containsMatch(true,
        NonNullList.<ItemStack> from(ItemStack.EMPTY, inventoryContents.toArray(new ItemStack[0])),
        stackToTest)) {
      //the item that I target matches something in my filter
      //meaning, if i am in whitelist mode, it is valid, it matched the allowed list
      //if I am in blacklist mode, nope not valid
      return !this.isWhitelist();
    }
    //here is the opposite: i did NOT match the list
    return this.isWhitelist();
  }

  private List<ItemStack> getFilter() {
    List<ItemStack> validForSide = this.inv.subList(1, FILTER_SIZE + 1);
    return NonNullList.<ItemStack> from(ItemStack.EMPTY, validForSide.toArray(new ItemStack[0]));
  }

  @Override
  public EnumFacing getCurrentFacing() {
    // weird hack IDK when its needed
    //but it makes sure this always returns where the white connectory connector exists
    EnumFacing facingTo = super.getCurrentFacing();
    if (facingTo.getAxis().isVertical()) {
      facingTo = facingTo.getOpposite();
    }
    return facingTo;
  }

  /**
   * for every side connected to me pull fluid in from it UNLESS its my current facing direction. for THAT side, i push fluid out from me pull first then push
   *
   * TODO: UtilFluid that does a position, a facing, and tries to move fluid across
   *
   *
   */
  @Override
  public void update() {
    if (this.isRunning() == false) {
      return;
    }
    boolean skipItemsThisTick = false;
    if (TRANSFER_ITEM_TICK_DELAY > 1) {
      //if its 1, just move 1 per tck and no prob
      if (this.itemTransferCooldown > 0) {
        this.itemTransferCooldown--;
        skipItemsThisTick = true;
      }
      else {
        //transfer is fine to go ahead, reset for next time
        itemTransferCooldown = TRANSFER_ITEM_TICK_DELAY;
      }
    }
    //   ModCyclic.logger.error("itemTransferCooldown " + itemTransferCooldown + "_" + skipItemsThisTick);
    if (skipItemsThisTick) {
      return;
    }
    this.tryExport();
    this.tryImport();
  }

  public void tryExport() {
    if (this.getStackInSlot(SLOT_TRANSFER).isEmpty()) {
      return;//im empty nothing to give
    }
    boolean outputSuccess = false;
    ItemStack stackToExport = this.getStackInSlot(SLOT_TRANSFER).copy();
    EnumFacing importFromSide = this.getCurrentFacing();
    EnumFacing exportToSide = importFromSide.getOpposite();
    BlockPos posTarget = pos.offset(exportToSide);
    TileEntity tileTarget = world.getTileEntity(posTarget);
    //   ModCyclic.logger.log("EXPORT TO  FROM " + tileTarget.getClass());
    if (tileTarget == null ||
        tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, exportToSide.getOpposite()) == false) {
      return;
    }
    ItemStack pulled = UtilItemStack.tryDepositToHandler(world, posTarget, exportToSide.getOpposite(), stackToExport);
    if (pulled.getCount() != stackToExport.getCount()) {
      this.setInventorySlotContents(SLOT_TRANSFER, pulled);
      //one or more was put in
      outputSuccess = true;
    }
    if (outputSuccess && world.getTileEntity(pos.offset(importFromSide)) instanceof TileEntityCableBase) {
      TileEntityCableBase cable = (TileEntityCableBase) world.getTileEntity(pos.offset(importFromSide));
      if (cable.isItemPipe())
        cable.updateIncomingItemFace(importFromSide.getOpposite());
    }
  }

  public void tryImport() {
    if (this.getStackInSlot(SLOT_TRANSFER).isEmpty() == false) {
      return;//im full leave me alone
    }
    EnumFacing importFromSide = this.getCurrentFacing();
    BlockPos posTarget = pos.offset(importFromSide);
    TileEntity tileTarget = world.getTileEntity(posTarget);
    if (tileTarget == null) {
      return;
    }
    //   ModCyclic.logger.log("IMPORT FROM " + tileTarget.getClass() + " at side " + importFromSide);
    ItemStack itemTarget;
    if (tileTarget.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, importFromSide.getOpposite())) {
      IItemHandler itemHandlerFrom = tileTarget.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, importFromSide.getOpposite());
      for (int i = 0; i < itemHandlerFrom.getSlots(); i++) {
        itemTarget = itemHandlerFrom.getStackInSlot(i);
        if (itemTarget.isEmpty()) {
          continue;
        }
        //check against whitelist/blacklist system
        if (this.isStackInvalid(itemTarget)) {
          //          ModCyclic.logger.log("not valid " + itemTarget.getDisplayName());
          continue;
        }
        //passed filter check, so do the transaction
        ItemStack pulled = itemHandlerFrom.extractItem(i, 1, false);
        if (pulled != null && pulled.isEmpty() == false) {
          this.setInventorySlotContents(SLOT_TRANSFER, pulled.copy());
          return;
        }
      }
    }
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    itemTransferCooldown = compound.getInteger("itemTransferCooldown");
    needsRedstone = compound.getInteger(NBT_REDST);
    filterType = compound.getInteger("wbtype");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger(NBT_REDST, needsRedstone);
    compound.setInteger("itemTransferCooldown", itemTransferCooldown);
    compound.setInteger("wbtype", filterType);
    return super.writeToNBT(compound);
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

  public static void syncConfig(Configuration config) {
    TRANSFER_ITEM_TICK_DELAY = config.getInt("TRANSFER_ITEM_TICK_DELAY", Const.ConfigCategory.cables, 1, 1, 200, "Tick Delay between item transfers (1 means 1 item per tick so no delay).  Only affects Item Extractor.  ");
  }
}
