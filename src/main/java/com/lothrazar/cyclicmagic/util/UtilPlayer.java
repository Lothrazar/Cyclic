package com.lothrazar.cyclicmagic.util;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class UtilPlayer {
  public static ItemStack getPlayerItemIfHeld(EntityPlayer player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand == ItemStack.EMPTY) {
      wand = player.getHeldItemOffhand();
    }
    return wand;
  }
  public static IBlockState getBlockstateFromSlot(EntityPlayer player, int slot) {
    ItemStack stack = player.inventory.getStackInSlot(slot);
    if (stack != ItemStack.EMPTY &&
        stack.getItem() != null &&
        Block.getBlockFromItem(stack.getItem()) != null) {
      Block b = Block.getBlockFromItem(stack.getItem());
      return UtilItemStack.getStateFromMeta(b, stack.getMetadata());
    }
    return null;
  }
  public static int getFirstSlotWithBlock(EntityPlayer player) {
    int ret = -1;
    ItemStack stack;
    for (int i = 9; i < 36; i++) {
      stack = player.inventory.getStackInSlot(i);
      if (stack != ItemStack.EMPTY &&
          stack.getItem() != null &&
          Block.getBlockFromItem(stack.getItem()) != null) { return i; }
    }
    return ret;
  }
  final static int width = 9;
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
    player.inventory.setInventorySlotContents(currentItem, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(currentItem, top);// lot so 0 gets
    // what 9 had
    player.inventory.setInventorySlotContents(topNumber, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(topNumber, mid);
    player.inventory.setInventorySlotContents(midNumber, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(midNumber, low);
    player.inventory.setInventorySlotContents(lowNumber, ItemStack.EMPTY);
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
    player.inventory.setInventorySlotContents(currentItem, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(currentItem, low);// lot so 0 gets
    // what 9 had
    player.inventory.setInventorySlotContents(lowNumber, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(lowNumber, mid);
    player.inventory.setInventorySlotContents(midNumber, ItemStack.EMPTY);
    player.inventory.setInventorySlotContents(midNumber, top);
    player.inventory.setInventorySlotContents(topNumber, ItemStack.EMPTY);
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
  public static IInventory getOpenContainerInventory(EntityPlayer p) {
    // a workaround since player does not reference the inventory, only the
    // container
    // and Container has no get method
    return p.openContainer.getSlot(0).inventory;
  }
  public static void decrStackSize(EntityPlayer entityPlayer, int currentItem) {
    if (entityPlayer.capabilities.isCreativeMode == false) {
      entityPlayer.inventory.decrStackSize(currentItem, 1);
    }
  }
  public static void decrStackSize(EntityPlayer entityPlayer, EnumHand hand) {
    if (entityPlayer.capabilities.isCreativeMode == false) {
      entityPlayer.getHeldItem(hand).shrink(1);
    }
  }
  public static boolean hasValidOpenContainer(EntityPlayer p) {
    return p != null && p.openContainer != null && p.openContainer.inventorySlots.size() > 0 &&
        p.openContainer.getSlot(0) != null &&
        p.openContainer.getSlot(0).inventory != null;
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
      ModCyclic.logger.error("Cannot update null container");
    }
    else {
      p.openContainer.detectAndSendChanges();
    }
  }
  public static Item getItemArmorSlot(EntityPlayer player, EntityEquipmentSlot slot) {
    ItemStack inslot = player.inventory.armorInventory.get(slot.getIndex());
//    ItemStack inslot = player.inventory.armorInventory[slot.getIndex()];
    Item item = (inslot == ItemStack.EMPTY) ? null : inslot.getItem();
    return item;
  }
}
