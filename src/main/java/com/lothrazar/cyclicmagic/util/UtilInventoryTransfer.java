package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class UtilInventoryTransfer {
  public static class BagDepositReturn {
    public BagDepositReturn(int m, ItemStack[] s) {
      moved = m;
      stacks = s;
    }
    public int moved;
    public ItemStack[] stacks;
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
      if (chestEmptySlot != null) {
        continue;
      } // slot not empty, skip over it
      for (int islotPlayer = Const.HOTBAR_SIZE; islotPlayer < getInvoEnd(player); islotPlayer++) {
        playerItem = player.inventory.getStackInSlot(islotPlayer);
        if (playerItem == null) {
          continue;
        } // empty inventory slot
        //ModMain.logger.info("try dep :"+islotPlayer + "_"+playerItem.getUnlocalizedName());
        inventory.setInventorySlotContents(islotInvo, playerItem);
        player.inventory.setInventorySlotContents(islotPlayer, null);
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
      if (playerEmptySlot != null) {
        continue;
      } // slot not empty, skip over it
      // ok we found an empty player slot
      for (int islotInvo = start; islotInvo < inventory.getSizeInventory(); islotInvo++) {
        chestItem = inventory.getStackInSlot(islotInvo);
        if (chestItem == null) {
          continue;
        } // empty inventory slot
        player.inventory.setInventorySlotContents(islotPlayer, chestItem);
        inventory.setInventorySlotContents(islotInvo, null);
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
      if (chestItem == null) {
        continue;
      } // empty chest slot
      for (int islotInv = Const.HOTBAR_SIZE; islotInv < getInvoEnd(player); islotInv++) {
        playerItem = player.inventory.getStackInSlot(islotInv);
        if (playerItem == null) {
          continue;
        } // empty inventory slot
        if (UtilItemStack.canMerge(playerItem, chestItem)) {
          // same item, including damage (block state)
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.stackSize;
          if (room <= 0) {
            continue;
          } // no room, check the next spot
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(playerItem.stackSize, room);
          chestItem.stackSize += toDeposit;
          chest.setInventorySlotContents(islotChest, chestItem);
          playerItem.stackSize -= toDeposit;
          if (playerItem.stackSize <= 0) // because of calculations
          // above, should
          // not be below zero
          {
            // item stacks with zero count do not destroy
            // themselves, they show
            // up and have unexpected behavior in game so set to
            // empty
            player.inventory.setInventorySlotContents(islotInv, null);
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
      if (chestItem == null) {
        continue;
      } // empty chest slot
      for (int islotInv = Const.HOTBAR_SIZE; islotInv < getInvoEnd(player); islotInv++) {
        playerItem = player.inventory.getStackInSlot(islotInv);
        if (playerItem == null) {
          continue;
        } // empty inventory slot
        if (UtilItemStack.canMerge(playerItem, chestItem)) {
          invMax = playerItem.getItem().getItemStackLimit(playerItem);
          room = invMax - playerItem.stackSize;
          if (room <= 0) {
            continue;
          } // no room, check the next spot
          toDeposit = Math.min(chestItem.stackSize, room);
          if (restockLeaveOne && chestItem.stackSize - toDeposit == 0) {
            // they decided in the config that leaving one behind is better
            toDeposit--;
            if (toDeposit == 0) {
              continue;
            } // dont do nothing
          }
          // add to player
          playerItem.stackSize += toDeposit;
          player.inventory.setInventorySlotContents(islotInv, playerItem);
          // remove from chest/invo
          chestItem.stackSize -= toDeposit;
          if (chestItem.stackSize <= 0) {
            chest.setInventorySlotContents(islotChest, null);
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
  public static ArrayList<ItemStack> dumpToIInventory(ArrayList<ItemStack> stacks, IInventory inventory, int startingSlot) {
    //and return the remainder after dumping
    ArrayList<ItemStack> remaining = new ArrayList<ItemStack>();
    ItemStack chestStack;
    for (ItemStack current : stacks) {
      if (current == null) {
        continue;
      }
      for (int i = startingSlot; i < inventory.getSizeInventory(); i++) {
        if (current == null) {
          continue;
        }
        chestStack = inventory.getStackInSlot(i);
        if (chestStack == null) {
          inventory.setInventorySlotContents(i, current);
          // and dont add current ot remainder at all ! sweet!
          current = null;
        }
        else if (UtilItemStack.canMerge(chestStack, current)) {
          int space = chestStack.getMaxStackSize() - chestStack.stackSize;
          int toDeposit = Math.min(space, current.stackSize);
          if (toDeposit > 0) {
            current.stackSize -= toDeposit;
            chestStack.stackSize += toDeposit;
            if (current.stackSize == 0) {
              current = null;
            }
          }
        }
      } // finished current pass over inventory
      if (current != null) {
        remaining.add(current);
      }
    }
    return remaining;
  }
  public static BagDepositReturn dumpFromListToIInventory(World world, IInventory chest, ItemStack[] stacks, boolean onlyMatchingItems) {
    ItemStack chestItem;
    ItemStack bagItem;
    int room;
    int toDeposit;
    int chestMax;
    int itemsMoved = 0;
    for (int islotStacks = 0; islotStacks < stacks.length; islotStacks++) {
      bagItem = stacks[islotStacks];
      if (bagItem == null || bagItem.stackSize == 0) {
        continue;
      }
      // System.out.println(bagItem.stackSize + "_" + bagItem.getDisplayName());
      for (int islotChest = 0; islotChest < chest.getSizeInventory(); islotChest++) {
        chestItem = chest.getStackInSlot(islotChest);
        //we have a space in the inventory thats empty. are we allowed
        if (chestItem == null && onlyMatchingItems == false) {
          //then yeah we are allowed to use the empty space
          if (chest.isItemValidForSlot(islotStacks, bagItem)) {
            // System.out.println("dump at " + islotChest);
            itemsMoved += bagItem.stackSize;
            chest.setInventorySlotContents(islotChest, bagItem);
            stacks[islotStacks] = null;
            bagItem = null;
            break;//move to next bag item, we're done here
          }
          else {
            //cant dump here. but also cant merge so move to next slot
            continue;
          }
        }
        if (chestItem == null) {
          //chest item is null, and were trying to merge (check is probably redundant here)
          continue;//go to next chest item
        }
        //ok so chestItem is not nulll
        if (bagItem == null || bagItem.stackSize == 0) {
          break;//stop lookin in the chest, get a new bag item
        }
        bagItem = stacks[islotStacks];
        if (UtilItemStack.canMerge(bagItem, chestItem)) {
          chestMax = chestItem.getItem().getItemStackLimit(chestItem);
          room = chestMax - chestItem.stackSize;
          if (room <= 0) {
            continue;//no room on this chest slot, so move to next slot
          } // no room, check the next spot
          //System.out.println("merge at " + islotChest);
          // so if i have 30 room, and 28 items, i deposit 28.
          // or if i have 30 room and 38 items, i deposit 30
          toDeposit = Math.min(bagItem.stackSize, room);
          chestItem.stackSize += toDeposit;
          chest.setInventorySlotContents(islotChest, chestItem);
          bagItem.stackSize -= toDeposit;
          itemsMoved += toDeposit;
          if (bagItem.stackSize <= 0) {
            // item stacks with zero count do not destroy
            // themselves, they show
            // up and have unexpected behavior in game so set to
            // empty
            stacks[islotStacks] = null;
          }
          else {
            // set to new quantity
            stacks[islotStacks] = bagItem;
          }
        } // end if items match
        if (bagItem == null || bagItem.stackSize == 0) {
          break;//stop lookin in the chest, get a new bag item
        }
      } // close loop on player inventory items
    } // close loop on chest items
    return new BagDepositReturn(itemsMoved, stacks);
  }
}
