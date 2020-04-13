package com.lothrazar.cyclicmagic.item.storagesack;

import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum StorageActionType {

  NOTHING, MERGE, DEPOSIT;

  private final static String NBT_OPEN = "isOpen";
  private final static String NBT_COLOUR = "COLOUR";
  private final static String NBT = "build";
  private final static String NBTTIMEOUT = "timeout";

  public static int getTimeout(ItemStack wand) {
    return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
  }

  public static void setTimeout(ItemStack wand) {
    UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
  }

  public static void tickTimeout(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    int t = tags.getInteger(NBTTIMEOUT);
    if (t > 0) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
    }
  }

  public static int get(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    return tags.getInteger(NBT);
  }

  public static String getName(ItemStack wand) {
    try {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return "item.storage_bag." + StorageActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
    }
    catch (Exception e) {
      return "item.storage_bag." + NOTHING.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    int type = tags.getInteger(NBT);
    type++;
    if (type > DEPOSIT.ordinal()) {
      type = NOTHING.ordinal();
    }
    tags.setInteger(NBT, type);
    wand.setTagCompound(tags);
  }

  public static int getColour(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    if (tags.hasKey(NBT_COLOUR) == false) {
      return EnumDyeColor.BROWN.getColorValue();
    }
    return tags.getInteger(NBT_COLOUR);
  }

  public static boolean getIsOpen(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    if (tags.hasKey(NBT_OPEN) == false) {
      return false;
    }
    return tags.getBoolean(NBT_OPEN);
  }

  public static void setIsOpen(ItemStack wand, boolean s) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    tags.setBoolean(NBT_OPEN, s);
  }

  public static void setColour(ItemStack wand, int color) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    //      int type = tags.getInteger(NBT_COLOUR);
    //      type++;
    //      if (type > EnumDyeColor.values().length) {
    //        type = EnumDyeColor.BLACK.getDyeDamage();
    //      }
    tags.setInteger(NBT_COLOUR, color);
    wand.setTagCompound(tags);
    // TODO Auto-generated method stub
  }
}