package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Lothrazar at https://github.com/PrinceOfAmber
 */
public class UtilInventorySort {
  public static class SortGroup {
    public SortGroup(String k) {
      stacks = new ArrayList<ItemStack>();
      key = k;
    }
    public void add(ItemStack s) {
      stacks.add(s);
    }
    public ArrayList<ItemStack> stacks;
    public String key;
  }
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
    updatePlayerContainerClient(player);
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
    updatePlayerContainerClient(player);
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
        if (playerItem.getItem().equals(chestItem.getItem())
            && playerItem.getItemDamage() == chestItem.getItemDamage()) {
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
    updatePlayerContainerClient(player);
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
        if (playerItem.getItem().equals(chestItem.getItem())
            && playerItem.getItemDamage() == chestItem.getItemDamage()) {
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
    updatePlayerContainerClient(player);
  }
  final static String NBT_SORT = "terraria_sort";
  final static int SORT_ALPH = 0;
  final static int SORT_ALPHI = 1;
  private static int getNextSort(EntityPlayer p) {
    int prev = p.getEntityData().getInteger(NBT_SORT);
    int n = prev + 1;
    if (n >= 2)
      n = 0;
    p.getEntityData().setInteger(NBT_SORT, n);
    return n;
  }
  public static void sort(EntityPlayer player, IInventory invo) {
    int sortType = getNextSort(player);
    int iSize = invo.getSizeInventory();
    Map<String, SortGroup> unames = new HashMap<String, SortGroup>();
    ItemStack item = null;
    SortGroup temp;
    String key = "";
    for (int i = 0; i < iSize; i++) {
      item = invo.getStackInSlot(i);
      if (item == null) {
        continue;
      }
      if (sortType == SORT_ALPH) // : why do it this way ->
        key = item.getUnlocalizedName() + item.getItemDamage();
      else if (sortType == SORT_ALPHI)
        key = item.getItem().getClass().getName() + item.getUnlocalizedName() + item.getItemDamage();
      // else if(sortType == SORT_CLASS)
      // key = item.getItem().getClass().getName()+ item.getItemDamage();
      temp = unames.get(key);
      if (temp == null) {
        temp = new SortGroup(key);
      }
      if (temp.stacks.size() > 0) {
        // try to merge with top
        ItemStack top = temp.stacks.remove(temp.stacks.size() - 1);
        int room = top.getMaxStackSize() - top.stackSize;
        if (room > 0) {
          int moveover = Math.min(item.stackSize, room);
          top.stackSize += moveover;
          item.stackSize -= moveover;
          if (item.stackSize == 0) {
            item = null;
            invo.setInventorySlotContents(i, item);
          }
        }
        temp.stacks.add(top);
      }
      if (item != null)
        temp.add(item);
      unames.put(key, temp);
    }
    // http://stackoverflow.com/questions/780541/how-to-sort-a-hashmap-in-java
    ArrayList<SortGroup> sorted = new ArrayList<SortGroup>(unames.values());
    Collections.sort(sorted, new Comparator<SortGroup>() {
      public int compare(SortGroup o1, SortGroup o2) {
        return o1.key.compareTo(o2.key);
      }
    });
    for (SortGroup p : sorted) {
      for (int i = 0; i < p.stacks.size(); i++) {
        invo.setInventorySlotContents(i, null);
        invo.setInventorySlotContents(i, p.stacks.get(i));
      }
    }
    for (int j = 0; j < iSize; j++) {
      invo.setInventorySlotContents(j, null);
    }
    // alternately loop by rows
    // so we start at k again, add Const.ALL_COLS to go down one row
    updatePlayerContainerClient(player);
  }
  private static int getInvoEnd(EntityPlayer p) {
    return p.inventory.getSizeInventory() - Const.ARMOR_SIZE - 1 ;//now that we have shield slot, need the offset 1, otherwise boots get included
  }
  /**
   * call this from SERVER SIDE if you are doing stuff to containers/invos/tile
   * entities but your client GUI's are not updating
   * 
   * @param p
   */
  public static void updatePlayerContainerClient(EntityPlayer p) {
    // http://www.minecraftforge.net/forum/index.php?topic=15351.0
    p.inventory.markDirty();
    if (p.openContainer == null) {
      ModMain.logger.error("Cannot update null container");
    }
    else {
      p.openContainer.detectAndSendChanges();
    }
  }
}
