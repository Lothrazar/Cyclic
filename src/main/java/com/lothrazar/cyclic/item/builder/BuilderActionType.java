package com.lothrazar.cyclic.item.builder;

import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;

public enum BuilderActionType {

  SINGLE, X3, X5, X7, X9, X91, X19;

  public static final String NBTBLOCKSTATE = "blockstate";
  private final static String NBT = "ActionType";
  private final static String NBTTIMEOUT = "timeout";

  public static int getTimeout(ItemStack wand) {
    return wand.getOrCreateTag().getInt(NBTTIMEOUT);
  }

  public static void setTimeout(ItemStack wand) {
    wand.getOrCreateTag().putInt(NBTTIMEOUT, 15);//less than one tick
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

  public static String getName(ItemStack wand) {
    try {
      CompoundNBT tags = wand.getOrCreateTag();
      return "tool.action." + values()[tags.getInt(NBT)].toString().toLowerCase();
    }
    catch (Exception e) {
      return "tool.action." + SINGLE.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    CompoundNBT tags = wand.getOrCreateTag();
    int type = tags.getInt(NBT);
    type++;
    if (type >= values().length) {
      type = SINGLE.ordinal();
    }
    tags.putInt(NBT, type);
    wand.setTag(tags);
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundNBT encoded = NBTUtil.writeBlockState(target);
    wand.getOrCreateTag().put(NBTBLOCKSTATE, encoded);
  }

  @Nullable
  public static BlockState getBlockState(ItemStack wand) {
    if (!wand.getOrCreateTag().contains(NBTBLOCKSTATE)) {
      return null;
    }
    return NBTUtil.readBlockState(wand.getOrCreateTag().getCompound(NBTBLOCKSTATE));
  }
}