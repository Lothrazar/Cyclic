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
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class UtilInventoryTransfer {

  public static class BagDepositReturn {

    public BagDepositReturn(int m, NonNullList<ItemStack> s) {
      moved = m;
      stacks = s;
    }

    public int moved;
    public NonNullList<ItemStack> stacks;
  }

  //TODO: this whole class is a big mess, lots of code repetition; needs work.
  public static void dumpFromPlayerToIInventory(World world, IInventory inventory, EntityPlayer player) {
    ItemStack chestEmptySlot;
    ItemStack playerItem;
    int start = 0;
    // we loop on the chest and look for empty slots
    // once we have an empty slot, we find something to fill it with
    // inventory and chest has 9 rows by 3 columns, never changes. same as
    // 64
    // max stack size
    for (int islotInvo = start; islotInvo < inventory.getSizeInventory(); islotInvo++) {
      chestEmptySlot = inventory.getStackInSlot(islotInvo);
      if (!chestEmptySlot.isEmpty()) {
        continue;
      } // slot not empty, skip over it
      for (int islotPlayer = Const.HOTBAR_SIZE; islotPlayer < getInvoEnd(player); islotPlayer++) {
        playerItem = player.inventory.getStackInSlot(islotPlayer);
        if (playerItem.isEmpty()) {
          continue;
        } // empty inventory slot
        //ModMain.logger.info("try dep :"+islotPlayer + "_"+playerItem.getUnlocalizedName());
        inventory.setInventorySlotContents(islotInvo, playerItem);
        player.inventory.setInventorySlotContents(islotPlayer, ItemStack.EMPTY);
        break;
      } // close loop on player inventory items
    } // close loop on chest items
    UtilPlayer.updatePlayerContainerClient(player);
  }

  public static void dumpFromIInventoryToPlayer(World world, IInventory inventory, EntityPlayer player) {
    ItemStack playerEmptySlot;
    ItemStack chestItem;
    int start = 0; // dont start at zero every time
    for (int islotPlayer = Const.HOTBAR_SIZE; islotPlayer < getInvoEnd(player); islotPlayer++) {
      playerEmptySlot = player.inventory.getStackInSlot(islotPlayer);
      if (!playerEmptySlot.isEmpty()) {
        continue;
      } // slot not empty, skip over it
      // ok we found an empty player slot
      for (int islotInvo = start; islotInvo < inventory.getSizeInventory(); islotInvo++) {
        chestItem = inventory.getStackInSlot(islotInvo);
        if (chestItem.isEmpty()) {
          continue;
        } // empty inventory slot
        player.inventory.setInventorySlotContents(islotPlayer, chestItem);
        inventory.setInventorySlotContents(islotInvo, ItemStack.EMPTY);
        start = islotInvo + 1;
        break;
      } // close loop on player inventory items
    } // close loop on chest items
    UtilPlayer.updatePlayerContainerClient(player);
  }

  public static void sortFromPlayerToInventory(World world, IInventory chest, EntityPlayer player) {
    // source:
    // https://github.com/PrinceOfAmber/SamsPowerups/blob/master/Spells/src/main/java/com/lothrazar/samsmagic/spell/SpellChestDeposit.java#L84
    ItemStack chestItem;
    ItemStack playerItem;
    int room;
    int toDeposit;
    int chestMax;
    // player inventory and the small chest have the same dimensions
    int START_CHEST = 0;
    int END_CHEST = chest.getSizeInventory();
    // inventory and chest has 9 rows by 3 columns, never changes. same as
    // 64
    // max stack size
    for (int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++) {
      chestItem = chest.getStackInSlot(islotChest);
      if (chestItem.isEmpty()) {
        continue;
      } // empty chest slot
      for (int islotInv = Const.HOTBAR_SIZE; islotInv < getInvoEnd(player); islotInv++) {
        playerItem = player.inventory.getStackInSlot(islotInv);
        if (playerItem.isEmpty()) {
          continue;
        } // empty inventory slot
        if (UtilItemStack.canMerge(playerItem, chestItem)) {
          // same item, including damage (block state)
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.getCount();
          if (room <= 0) {
            continue;
          } // no room, check the next spot
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(playerItem.getCount(), room);
          chestItem.grow(toDeposit);
          chest.setInventorySlotContents(islotChest, chestItem);
          playerItem.shrink(toDeposit);
          if (playerItem.getCount() <= 0) {// because of calculations  above, should  not be below zero
            // item stacks with zero count do not destroy
            // themselves, they show
            // up and have unexpected behavior in game so set to
            // empty
            player.inventory.setInventorySlotContents(islotInv, ItemStack.EMPTY);
          }
          else {
            // set to new quantity
            player.inventory.setInventorySlotContents(islotInv, playerItem);
          }
        } // end if items match
      } // close loop on player inventory items
    } // close loop on chest items
    UtilPlayer.updatePlayerContainerClient(player);
  }

  public static void sortFromInventoryToPlayer(World world, IInventory chest, EntityPlayer player, boolean restockLeaveOne) {
    ItemStack chestItem;
    ItemStack playerItem;
    int room;
    int toDeposit;
    int invMax;
    // player inventory and the small chest have the same dimensions
    int START_CHEST = 0;
    int END_CHEST = chest.getSizeInventory();
    // inventory and chest has 9 rows by 3 columns, never changes. same as
    // 64
    // max stack size
    for (int islotChest = START_CHEST; islotChest < END_CHEST; islotChest++) {
      chestItem = chest.getStackInSlot(islotChest);
      if (chestItem.isEmpty()) {
        continue;
      } // empty chest slot
      for (int islotInv = Const.HOTBAR_SIZE; islotInv < getInvoEnd(player); islotInv++) {
        playerItem = player.inventory.getStackInSlot(islotInv);
        if (playerItem.isEmpty()) {
          continue;
        } // empty inventory slot
        if (UtilItemStack.canMerge(playerItem, chestItem)) {
          invMax = playerItem.getItem().getItemStackLimit(playerItem);
          room = invMax - playerItem.getCount();
          if (room <= 0) {
            continue;
          } // no room, check the next spot
          toDeposit = Math.min(chestItem.getCount(), room);
          if (restockLeaveOne && chestItem.getCount() - toDeposit == 0) {
            // they decided in the config that leaving one behind is better
            toDeposit--;
            if (toDeposit == 0) {
              continue;
            } // dont do nothing
          }
          // add to player
          playerItem.grow(toDeposit);
          player.inventory.setInventorySlotContents(islotInv, playerItem);
          // remove from chest/invo
          chestItem.shrink(toDeposit);
          if (chestItem.getCount() <= 0) {
            chest.setInventorySlotContents(islotChest, ItemStack.EMPTY);
          }
          else {
            chest.setInventorySlotContents(islotChest, chestItem);
          }
        } // end if items match
      } // close loop on player inventory items
    } // close loop on chest items
    UtilPlayer.updatePlayerContainerClient(player);
  }

  private static int getInvoEnd(EntityPlayer p) {
    return p.inventory.getSizeInventory() - Const.ARMOR_SIZE - 1;//now that we have shield slot, need the offset 1, otherwise boots get included
  }

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

  public static BagDepositReturn dumpFromListToIInventory(World world, IInventory chest, NonNullList<ItemStack> stacks, boolean onlyMatchingItems) {
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
