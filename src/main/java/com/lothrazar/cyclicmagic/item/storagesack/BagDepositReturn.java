package com.lothrazar.cyclicmagic.item.storagesack;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BagDepositReturn {

  public BagDepositReturn(int m, NonNullList<ItemStack> s) {
    moved = m;
    stacks = s;
  }

  public int moved;
  public NonNullList<ItemStack> stacks;
}