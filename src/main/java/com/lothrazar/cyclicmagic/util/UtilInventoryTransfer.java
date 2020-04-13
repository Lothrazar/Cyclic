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
package com.lothrazar.cyclicmagic.util;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.item.storagesack.BagDepositReturn;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class UtilInventoryTransfer {

  public static ArrayList<ItemStack> dumpToIInventory(List<ItemStack> stacks, IInventory inventory, int startingSlot, int maxSlot) {
    //and return the remainder after dumping
    ArrayList<ItemStack> remaining = new ArrayList<ItemStack>();
    ItemStack chestStack;
    for (ItemStack current : stacks) {
      for (int i = startingSlot; i < maxSlot; i++) {
        if (current.isEmpty()) {
          continue;
        }
        chestStack = inventory.getStackInSlot(i);
        if (chestStack.isEmpty()) {
          inventory.setInventorySlotContents(i, current);
          // and dont add current ot remainder at all ! sweet!
          current = ItemStack.EMPTY;
        }
        else if (UtilItemStack.canMerge(chestStack, current)) {
          int space = chestStack.getMaxStackSize() - chestStack.getCount();
          int toDeposit = Math.min(space, current.getCount());
          if (toDeposit > 0) {
            current.shrink(toDeposit);
            chestStack.grow(toDeposit);
            if (current.getCount() == 0) {
              current = ItemStack.EMPTY;
            }
          }
        }
      } // finished current pass over inventory
      if (!current.isEmpty()) {
        remaining.add(current);
      }
    }
    return remaining;
  }

  public static ArrayList<ItemStack> dumpToIInventory(List<ItemStack> stacks, IInventory inventory, int startingSlot) {
    return dumpToIInventory(stacks, inventory, startingSlot, inventory.getSizeInventory());
  }

  public static BagDepositReturn dumpFromListToCapability(World world, TileEntity tile,
      EnumFacing side,
      NonNullList<ItemStack> stacks, boolean onlyMatchingItems) {
    int itemsMoved = 0;
    if (tile == null ||
        tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side) == false) {
      return new BagDepositReturn(itemsMoved, stacks);
    }
    ItemStack bagItem;
    //  final  ItemStack chestItem;
    IItemHandler chest = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
    for (int islotStacks = 0; islotStacks < stacks.size(); islotStacks++) {
      bagItem = stacks.get(islotStacks);
      if (bagItem.isEmpty() || bagItem.getCount() == 0) {
        continue;
      }
      for (int islotChest = 0; islotChest < chest.getSlots(); islotChest++) {
        //
        //        chestItem = chest.getStackInSlot(islotChest);
        //we have a space in the inventory thats empty. are we allowed
        if (onlyMatchingItems == false
            || UtilItemStack.canMerge(bagItem, chest.getStackInSlot(islotChest))) {
          //just plain deposit
          int before = bagItem.getCount();
          bagItem = chest.insertItem(islotChest, bagItem, false);
          stacks.set(islotStacks, bagItem);
          //
          itemsMoved += (before - bagItem.getCount());
        }
      }
    }
    return new BagDepositReturn(itemsMoved, stacks);
  }

  public static BagDepositReturn dumpFromListToIInventory(World world, IInventory chest,
      NonNullList<ItemStack> stacks, boolean onlyMatchingItems) {
    ItemStack chestItem;
    ItemStack bagItem;
    int room;
    int toDeposit;
    int chestMax;
    int itemsMoved = 0;
    for (int islotStacks = 0; islotStacks < stacks.size(); islotStacks++) {
      bagItem = stacks.get(islotStacks);
      if (bagItem.isEmpty() || bagItem.getCount() == 0) {
        continue;
      }
      for (int islotChest = 0; islotChest < chest.getSizeInventory(); islotChest++) {
        chestItem = chest.getStackInSlot(islotChest);
        //we have a space in the inventory thats empty. are we allowed
        if (chestItem.isEmpty() && onlyMatchingItems == false) {
          //then yeah we are allowed to use the empty space
          if (chest.isItemValidForSlot(islotStacks, bagItem)) {
            itemsMoved += bagItem.getCount();
            chest.setInventorySlotContents(islotChest, bagItem);
            stacks.set(islotStacks, ItemStack.EMPTY);
            bagItem = ItemStack.EMPTY;
            break;//move to next bag item, we're done here
          }
          else {
            //cant dump here. but also cant merge so move to next slot
            continue;
          }
        }
        if (chestItem.isEmpty()) {
          //chest item is null, and were trying to merge (check is probably redundant here)
          continue;//go to next chest item
        }
        //ok so chestItem is not nulll
        if (UtilItemStack.isEmpty(bagItem)) {
          break;//stop lookin in the chest, get a new bag item
        }
        bagItem = stacks.get(islotStacks);
        if (UtilItemStack.canMerge(bagItem, chestItem)) {
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.getCount();
          if (room <= 0) {
            continue;//no room on this chest slot, so move to next slot
          } // no room, check the next spot
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(bagItem.getCount(), room);
          chestItem.grow(toDeposit);
          chest.setInventorySlotContents(islotChest, chestItem);
          bagItem.shrink(toDeposit);
          itemsMoved += toDeposit;
          if (bagItem.getCount() <= 0) {
            // item stacks with zero count do not destroy
            // themselves, they show
            // up and have unexpected behavior in game so set to
            // empty
            stacks.set(islotStacks, ItemStack.EMPTY);
          }
          else {
            // set to new quantity
            stacks.set(islotStacks, bagItem);
            //            stacks[islotStacks] = bagItem;
          }
        } // end if items match
        if (UtilItemStack.isEmpty(bagItem)) {
          break;//stop lookin in the chest, get a new bag item
        }
      } // close loop on player inventory items
    } // close loop on chest items
    return new BagDepositReturn(itemsMoved, stacks);
  }
}
