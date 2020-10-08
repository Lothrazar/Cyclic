package com.lothrazar.cyclic.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UtilPlayer {

  public static ItemStack getPlayerItemIfHeld(PlayerEntity player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand.isEmpty()) {
      wand = player.getHeldItemOffhand();
    }
    return wand;
  }

  public static int getFirstSlotWithBlock(PlayerEntity player, BlockState targetState) {
    int ret = -1;
    ItemStack stack;
    for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
      stack = player.inventory.getStackInSlot(i);
      if (!stack.isEmpty() &&
          stack.getItem() != null &&
          Block.getBlockFromItem(stack.getItem()) == targetState.getBlock()) {
        return i;
      }
    }
    return ret;
  }

  public static BlockState getBlockstateFromSlot(PlayerEntity player, int slot) {
    ItemStack stack = player.inventory.getStackInSlot(slot);
    if (!stack.isEmpty() &&
        stack.getItem() != null &&
        Block.getBlockFromItem(stack.getItem()) != null) {
      Block b = Block.getBlockFromItem(stack.getItem());
      return b.getDefaultState();
    }
    return null;
  }

  public static void decrStackSize(PlayerEntity player, int slot) {
    if (player.isCreative() == false) {
      player.inventory.decrStackSize(slot, 1);
    }
  }

  public static Item getItemArmorSlot(PlayerEntity player, EquipmentSlotType slot) {
    ItemStack inslot = player.inventory.armorInventory.get(slot.getIndex());
    //    ItemStack inslot = player.inventory.armorInventory[slot.getIndex()];
    Item item = (inslot.isEmpty()) ? null : inslot.getItem();
    return item;
  }
}
