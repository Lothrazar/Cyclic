package com.lothrazar.cyclicmagic.item.storagesack;

import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum StoragePickupType {

  NOTHING, FILTER, EVERYTHING;

  private final static String NBT = "deposit";

  public static int get(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    return tags.getInteger(NBT);
  }

  public static String getName(ItemStack wand) {
    try {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      return "item.storage_bag.pickup." + StoragePickupType.values()[tags.getInteger(NBT)].toString().toLowerCase();
    }
    catch (Exception e) {
      return "item.storage_bag.pickup." + NOTHING.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
    int type = tags.getInteger(NBT);
    type++;
    if (type > EVERYTHING.ordinal()) {
      type = NOTHING.ordinal();
    }
    tags.setInteger(NBT, type);
    wand.setTagCompound(tags);
  }
}