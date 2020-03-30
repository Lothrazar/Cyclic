package com.lothrazar.cyclic.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class UtilPlayer {

  public static ItemStack getPlayerItemIfHeld(PlayerEntity player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand.isEmpty()) {
      wand = player.getHeldItemOffhand();
    }
    return wand;
  }

  public static int getFirstSlotWithBlock(PlayerEntity player) {
    int ret = -1;
    ItemStack stack;
    for (int i = 9; i < 36; i++) {
      stack = player.inventory.getStackInSlot(i);
      if (!stack.isEmpty() &&
          stack.getItem() != null &&
          Block.getBlockFromItem(stack.getItem()) != Blocks.AIR) {
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
}
