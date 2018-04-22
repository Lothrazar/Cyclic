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
package com.lothrazar.cyclicmagic.block.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.oredict.OreDictionary;

public class TileEntityItemCableSort extends TileEntityBaseMachineInvo implements ITickable {

  public static final int FILTER_SIZE = 8;
  private static final int TICKS_TEXT_CACHED = 7;
  private static final int TIMER_SIDE_INPUT = 15;
  public static final int INVENTORY_SIZE = 0;

  public static enum LockType {
    FILTER, EVERYTHING, NOTHING;

    public String nameLower() {
      return UtilChat.lang("tile.item_pipe_sort." + this.name().toLowerCase());
    }
  }

  private Map<EnumFacing, Integer> mapIncoming = Maps.newHashMap();
  private Map<EnumFacing, Integer> allowEverything = Maps.newHashMap();
  private Map<EnumFacing, Integer> ignoreDamageIfOne = Maps.newHashMap();
  private BlockPos connectedInventory;
  private int labelTimer = 0;
  private String labelText = "";

  public TileEntityItemCableSort() {
    super(FILTER_SIZE * EnumFacing.values().length + 1);// 49
    this.setSlotsForInsert(0);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncoming.put(f, 0);
      allowEverything.put(f, 0);
      ignoreDamageIfOne.put(f, 0);//default to not ignore
    }
  }

  public LockType getLockType(EnumFacing f) {
    return LockType.values()[allowEverything.get(f)];
  }

  private List<ItemStack> getFilterForSide(EnumFacing f) {
    //order of colors
    //black; down
    //white; up
    //red; north
    //blue; south
    //green: west
    //yellow; east
    int row = f.ordinal();
    //so its 48 total slots, skip 0 for transfer
    // [1,8], [9, 16], [17, 24] [25, 32] [33, 40] , [41, 48]
    //sublist loses the specific type so convert it back
    int start = row * FILTER_SIZE + 1;
    int end = (row + 1) * FILTER_SIZE;
    List<ItemStack> validForSide = this.inv.subList(start, end + 1);
    // this.debugStacks(" items for side (r,s,e) " + row + "," + start + "," + end + "::", f, validForSide);
    return NonNullList.<ItemStack> from(ItemStack.EMPTY, validForSide.toArray(new ItemStack[0]));
  }

  public String getLabelText() {
    return labelText;
  }

  @SuppressWarnings("serial")
  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      mapIncoming.put(f, compound.getInteger(f.getName() + "_incoming"));
      allowEverything.put(f, compound.getInteger(f.getName() + "_toggle"));
      ignoreDamageIfOne.put(f, compound.getInteger(f.getName() + "_damage"));
    }
    labelText = compound.getString("label");
    labelTimer = compound.getInteger("labelt");
    connectedInventory = new Gson().fromJson(compound.getString("connectedInventory"), new TypeToken<BlockPos>() {}.getType());
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    super.writeToNBT(compound);
    for (EnumFacing f : EnumFacing.values()) {
      compound.setInteger(f.getName() + "_incoming", mapIncoming.get(f));
      compound.setInteger(f.getName() + "_toggle", allowEverything.get(f));
      compound.setInteger(f.getName() + "_damage", ignoreDamageIfOne.get(f));
    }
    compound.setString("label", labelText);
    compound.setInteger("labelt", labelTimer);
    return compound;
  }

  public BlockPos getConnectedPos() {
    return connectedInventory;
  }

  public void setConnectedPos(BlockPos connectedInventory) {
    this.connectedInventory = connectedInventory;
  }

  public void updateIncomingFace(EnumFacing inputFrom) {
    mapIncoming.put(inputFrom, TIMER_SIDE_INPUT);
  }

  private boolean isIncomingFromFace(EnumFacing face) {
    return mapIncoming.get(face) > 0;
  }

  private List<EnumFacing> getValidSidesForStack(ItemStack stackToExport) {
    List<EnumFacing> faces = new ArrayList<EnumFacing>();
    for (EnumFacing f : EnumFacing.values()) {
      if (this.isIncomingFromFace(f)) {
        continue;
      }
      if (this.getLockType(f) == LockType.FILTER) {
        List<ItemStack> inventoryContents = getFilterForSide(f);
        if (this.ignoreDamageIfOne.get(f) == 1) {
          //ignore damage, only check that items are equal
          for (ItemStack inFilter : inventoryContents) {
            if (inFilter.getItem().equals(stackToExport.getItem())) {
              faces.add(f);
              break;
            }
          }
        }
        else {
          //use normal ore-dict matching
          if (OreDictionary.containsMatch(true,
              NonNullList.<ItemStack> from(ItemStack.EMPTY, inventoryContents.toArray(new ItemStack[0])),
              stackToExport)) {
            faces.add(f);
          }
        }
      }
    }
    return faces;
  }

  private List<EnumFacing> getEverythingSides(ItemStack stackToExport) {
    List<EnumFacing> faces = new ArrayList<EnumFacing>();
    for (EnumFacing f : EnumFacing.values()) {
      if (this.getLockType(f) == LockType.EVERYTHING) {
        faces.add(f);
      }
    }
    return faces;
  }

  @Override
  public void update() {
    this.tickLabelText();
    tickDownIncomingFaces();
    ItemStack stackToExport = this.getStackInSlot(0).copy();
    if (stackToExport.isEmpty()) {
      return;
    }
    //now look over any sides that are NOT incoming, try to export
    //Actually shuffle the positions. if we are at a 3 way juncture, spread out where it goes first
    List<EnumFacing> targetFaces = this.getValidSidesForStack(stackToExport);
    //debug(" filter ", stackToExport, targetFaces);
    if (targetFaces.size() > 0) {
      Collections.shuffle(targetFaces);
      tryExportToTheseFaces(targetFaces);
    }
    else {
      //this stack does not match any of the filters
      targetFaces = this.getEverythingSides(stackToExport);
      //   debug(" EVERYTHING  ", stackToExport, targetFaces);
      Collections.shuffle(targetFaces);
      tryExportToTheseFaces(targetFaces);
    }
  }

  //  private void debug(String prefix, ItemStack s, List<EnumFacing> targetFaces) {
  //    String debug = s.getDisplayName() + prefix + " VALID FOR  ";
  //    for (EnumFacing f : targetFaces) {
  //      debug = debug + f.name() + " ";
  //    }
  //    ModCyclic.logger.log(debug);
  //  }
  //  private void debugStacks(String prefix, EnumFacing face, List<ItemStack> targetFaces) {
  //    String debug = face.name() + prefix;
  //    for (ItemStack f : targetFaces) {
  //      debug = debug + f.getDisplayName() + " ";
  //    }
  //    ModCyclic.logger.log(debug);
  //  }
  public void tryExportToTheseFaces(List<EnumFacing> targetFaces) {
    BlockPos posTarget;
    TileEntity tileTarget;
    ItemStack stackToExport;
    for (EnumFacing f : targetFaces) {
      stackToExport = this.getStackInSlot(0).copy();
      if (stackToExport.isEmpty()) {
        return;
      }
      if (this.isIncomingFromFace(f) == false) {
        //ok,  not incoming from here. so lets output some
        posTarget = pos.offset(f);
        tileTarget = world.getTileEntity(posTarget);
        if (tileTarget == null) {
          continue;
        }
        boolean outputSuccess = false;
        ItemStack leftAfterDeposit = UtilItemStack.tryDepositToHandler(world, posTarget, f.getOpposite(), stackToExport);
        if (leftAfterDeposit.getCount() < stackToExport.getCount()) { //something moved!
          //then save result
          this.setInventorySlotContents(0, leftAfterDeposit);
          outputSuccess = true;
        }
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityItemCableSort) {
          TileEntityItemCableSort cable = (TileEntityItemCableSort) world.getTileEntity(posTarget);
          cable.updateIncomingFace(f.getOpposite());
        }
        if (outputSuccess && world.getTileEntity(posTarget) instanceof TileEntityCableBase) {
          //TODO: holy balls do we need a base class
          TileEntityCableBase cable = (TileEntityCableBase) world.getTileEntity(posTarget);
          if (cable.isItemPipe())
            cable.updateIncomingItemFace(f.getOpposite());
        }
      }
    }
  }

  /**
   * with normal item movement it moves too fast for user to read cache the current item for a few ticks so full item pipes dont show empty or flashing fast text
   */
  private void tickLabelText() {
    this.labelTimer--;
    if (this.labelTimer <= 0) {
      this.labelTimer = 0;
      this.labelText = "";
      if (this.getStackInSlot(0).isEmpty() == false) {
        this.labelText = this.getStackInSlot(0).getDisplayName();
        this.labelTimer = TICKS_TEXT_CACHED;
      }
    }
  }

  public boolean hasAnyIncomingFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        return true;
    }
    return false;
  }

  public void tickDownIncomingFaces() {
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        mapIncoming.put(f, mapIncoming.get(f) - 1);
    }
  }

  public String getIncomingStrings() {
    String in = "";
    for (EnumFacing f : EnumFacing.values()) {
      if (mapIncoming.get(f) > 0)
        in += f.name().toLowerCase() + " ";
    }
    return in.trim();
  }

  public int[] getFieldOrdinals() {
    return super.getFieldArray(getFieldCount());
  }

  @Override
  public int getFieldCount() {
    return EnumFacing.values().length * 2;
  }

  @Override
  public int getField(int id) {
    if (id < EnumFacing.values().length) {
      EnumFacing enumID = EnumFacing.values()[id];
      return allowEverything.get(enumID);
    }
    else {
      EnumFacing enumID = EnumFacing.values()[id % EnumFacing.values().length];
      // ModCyclic.logger.log(" getFField " + id + " ---- " + ignoreDamageIfOne.get(enumID));
      return ignoreDamageIfOne.get(enumID);
    }
  }

  @Override
  public void setField(int id, int value) {
    if (id < EnumFacing.values().length) {
      //ignore area
      EnumFacing enumID = EnumFacing.values()[id];
      allowEverything.put(enumID, value % LockType.values().length);
    }
    else {
      //lock area
      EnumFacing enumID = EnumFacing.values()[id % EnumFacing.values().length];
      ignoreDamageIfOne.put(enumID, value % 2);
      // ModCyclic.logger.log("ignoreDamageIfOne SET as" + ignoreDamageIfOne.get(enumID) + "VS getrfield " + this.getField(id));
    }
  }
}
