package com.lothrazar.cyclicmagic.util;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class UtilInventory {
  public static boolean hasValidOpenContainer(EntityPlayer p){
    return p != null && p.openContainer != null && p.openContainer.inventorySlots.size() > 0 && 
        p.openContainer.getSlot(0) != null && 
        p.openContainer.getSlot(0).inventory == null;
  }
  public static IInventory getOpenContainerInventory(EntityPlayer p){
    // a workaround since player does not reference the inventory, only the
    // container
    // and Container has no get method
    return  p.openContainer.getSlot(0).inventory;
  }
  public static ItemStack getPlayerItemIfHeld(EntityPlayer player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand == null) {
      wand = player.getHeldItemOffhand();
    }
    return wand;
  }
  final static int width = 9;
  public static int mergeItemsBetweenStacks(ItemStack takeFrom, ItemStack moveTo) {
    int room = moveTo.getMaxStackSize() - moveTo.stackSize;
    int moveover = 0;
    if (room > 0) {
      moveover = Math.min(takeFrom.stackSize, room);
      moveTo.stackSize += moveover;
      takeFrom.stackSize -= moveover;
    }
    return moveover;
  }
  public static void shiftSlotDown(EntityPlayer player, int currentItem) {
    int topNumber = currentItem + width;
    int midNumber = topNumber + width;
    int lowNumber = midNumber + width;
    // so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26,
    // 35
    ItemStack bar = player.inventory.getStackInSlot(currentItem);
    ItemStack top = player.inventory.getStackInSlot(topNumber);
    ItemStack mid = player.inventory.getStackInSlot(midNumber);
    ItemStack low = player.inventory.getStackInSlot(lowNumber);
    player.inventory.setInventorySlotContents(currentItem, null);
    player.inventory.setInventorySlotContents(currentItem, top);// lot so 0 gets
    // what 9 had
    player.inventory.setInventorySlotContents(topNumber, null);
    player.inventory.setInventorySlotContents(topNumber, mid);
    player.inventory.setInventorySlotContents(midNumber, null);
    player.inventory.setInventorySlotContents(midNumber, low);
    player.inventory.setInventorySlotContents(lowNumber, null);
    player.inventory.setInventorySlotContents(lowNumber, bar);
  }
  public static void shiftSlotUp(EntityPlayer player, int currentItem) {
    // so we move each up by nine
    int topNumber = currentItem + width;
    int midNumber = topNumber + width;
    int lowNumber = midNumber + width;
    // so if we had the final slot hit (8 for keyboard 9) we would go 8, 17, 26,
    // 35
    ItemStack bar = player.inventory.getStackInSlot(currentItem);
    ItemStack top = player.inventory.getStackInSlot(topNumber);
    ItemStack mid = player.inventory.getStackInSlot(midNumber);
    ItemStack low = player.inventory.getStackInSlot(lowNumber);
    player.inventory.setInventorySlotContents(currentItem, null);
    player.inventory.setInventorySlotContents(currentItem, low);// lot so 0 gets
    // what 9 had
    player.inventory.setInventorySlotContents(lowNumber, null);
    player.inventory.setInventorySlotContents(lowNumber, mid);
    player.inventory.setInventorySlotContents(midNumber, null);
    player.inventory.setInventorySlotContents(midNumber, top);
    player.inventory.setInventorySlotContents(topNumber, null);
    player.inventory.setInventorySlotContents(topNumber, bar);
  }
  public static void shiftBarDown(EntityPlayer player) {
    shiftSlotDown(player, 0);
    shiftSlotDown(player, 1);
    shiftSlotDown(player, 2);
    shiftSlotDown(player, 3);
    shiftSlotDown(player, 4);
    shiftSlotDown(player, 5);
    shiftSlotDown(player, 6);
    shiftSlotDown(player, 7);
    shiftSlotDown(player, 8);
  }
  public static void shiftBarUp(EntityPlayer player) {
    shiftSlotUp(player, 0);
    shiftSlotUp(player, 1);
    shiftSlotUp(player, 2);
    shiftSlotUp(player, 3);
    shiftSlotUp(player, 4);
    shiftSlotUp(player, 5);
    shiftSlotUp(player, 6);
    shiftSlotUp(player, 7);
    shiftSlotUp(player, 8);
  }
  public static void decrStackSize(EntityPlayer entityPlayer, int currentItem) {
    if (entityPlayer.capabilities.isCreativeMode == false) {
      entityPlayer.inventory.decrStackSize(currentItem, 1);
    }
  }
  public static void decrStackSize(EntityPlayer entityPlayer, EnumHand hand) {
    if (entityPlayer.capabilities.isCreativeMode == false) {
      entityPlayer.getHeldItem(hand).stackSize--;
    }
  }
  public static IBlockState getBlockstateFromSlot(EntityPlayer player, int slot) {
    ItemStack stack = player.inventory.getStackInSlot(slot);
    if (stack != null &&
        stack.getItem() != null &&
        Block.getBlockFromItem(stack.getItem()) != null) {
      Block b = Block.getBlockFromItem(stack.getItem());
      return UtilItem.getStateFromMeta(b, stack.getMetadata());
    }
    return null;
  }
  public static int getFirstSlotWithBlock(EntityPlayer player) {
    int ret = -1;
    ItemStack stack;
    for (int i = 9; i < 27; i++) {
      stack = player.inventory.getStackInSlot(i);
      if (stack != null &&
          stack.getItem() != null &&
          Block.getBlockFromItem(stack.getItem()) != null) { return i; }
    }
    return ret;
  }
}
