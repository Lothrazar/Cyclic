package com.lothrazar.cyclic.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class UtilNBT {

  public static void setItemStackBlockPos(ItemStack item, BlockPos pos) {
    if (pos == null || item.isEmpty()) {
      return;
    }
    UtilNBT.setItemStackNBTVal(item, "xpos", pos.getX());
    UtilNBT.setItemStackNBTVal(item, "ypos", pos.getY());
    UtilNBT.setItemStackNBTVal(item, "zpos", pos.getZ());
  }

  public static void putBlockPos(CompoundNBT tag, BlockPos pos) {
    tag.putInt("xpos", pos.getX());
    tag.putInt("ypos", pos.getY());
    tag.putInt("zpos", pos.getZ());
  }

  public static BlockPos getItemStackBlockPos(ItemStack item) {
    if (item.isEmpty() || !item.getOrCreateTag().contains("xpos")) {
      return null;
    }
    CompoundNBT tag = item.getOrCreateTag();
    return getBlockPos(tag);
  }

  public static BlockPos getBlockPos(CompoundNBT tag) {
    return new BlockPos(tag.getInt("xpos"), tag.getInt("ypos"), tag.getInt("zpos"));
  }

  public static void setItemStackNBTVal(ItemStack item, String prop, int value) {
    if (item.isEmpty()) {
      return;
    }
    item.getOrCreateTag().putInt(prop, value);
  }

  public static void setItemStackNBTVal(ItemStack stack, String prop, String value) {
    stack.getOrCreateTag().putString(prop, value);
  }
}
