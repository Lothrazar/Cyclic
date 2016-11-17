package com.lothrazar.cyclicmagic.util;
import java.util.ArrayList;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class UtilInventory {


  public static ArrayList<ItemStack> dumpToIInventory(ArrayList<ItemStack> stacks, IInventory inventory) {
    //and return the remainder after dumping
    ArrayList<ItemStack> remaining = new ArrayList<ItemStack>();
    ItemStack chestStack;
    for (ItemStack current : stacks) {
      if (current == null) {
        continue;
      }
      for (int i = 0; i < inventory.getSizeInventory(); i++) {
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
}
