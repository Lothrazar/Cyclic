package com.lothrazar.cyclic.block.cable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

@Deprecated
public enum WrenchActionType {

  EXTRACT, DISABLE;

  private static final String NBT = "ActionType";
  private static final String NBTTIMEOUT = "timeout";

  public static int getTimeout(ItemStack wand) {
    return wand.getOrCreateTag().getInt(NBTTIMEOUT);
  }

  public static void setTimeout(ItemStack wand) {
    wand.getOrCreateTag().putInt(NBTTIMEOUT, 15);
    //less than one tick
  }

  public static void tickTimeout(ItemStack wand) {
    CompoundNBT tags = wand.getOrCreateTag();
    int t = tags.getInt(NBTTIMEOUT);
    if (t > 0) {
      wand.getOrCreateTag().putInt(NBTTIMEOUT, t - 1);
    }
  }

  public static int get(ItemStack wand) {
    if (wand.isEmpty()) {
      return 0;
    }
    CompoundNBT tags = wand.getOrCreateTag();
    return tags.getInt(NBT);
  }

  public static WrenchActionType getType(ItemStack wand) {
    return values()[get(wand)];
  }

  public static String getName(ItemStack wand) {
    try {
      return "tool.cable_wrench." + getType(wand).toString().toLowerCase();
    }
    catch (Exception e) {
      return "tool.cable_wrench." + EXTRACT.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    CompoundNBT tags = wand.getOrCreateTag();
    int type = tags.getInt(NBT);
    type++;
    if (type >= values().length) {
      type = EXTRACT.ordinal();
    }
    tags.putInt(NBT, type);
    wand.setTag(tags);
  }
}
