package com.lothrazar.cyclic.item.builder;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public enum BuilderActionType {

  SINGLE, X3, X5, X7, X9, X91, X19;

  public static final String NBTBLOCKSTATE = "blockstate";
  private static final String NBT = "ActionType";
  private static final String NBTTIMEOUT = "timeout";

  public static int getTimeout(ItemStack wand) {
    return wand.getOrCreateTag().getInt(NBTTIMEOUT);
  }

  public static void setTimeout(ItemStack wand) {
    wand.getOrCreateTag().putInt(NBTTIMEOUT, 15); //less than one tick
  }

  public static void tickTimeout(ItemStack wand) {
    CompoundTag tags = wand.getOrCreateTag();
    int t = tags.getInt(NBTTIMEOUT);
    if (t > 0) {
      wand.getOrCreateTag().putInt(NBTTIMEOUT, t - 1);
    }
  }

  public static int get(ItemStack wand) {
    if (wand.isEmpty()) {
      return 0;
    }
    CompoundTag tags = wand.getOrCreateTag();
    return tags.getInt(NBT);
  }

  public static String getName(ItemStack wand) {
    try {
      CompoundTag tags = wand.getOrCreateTag();
      return "tool.action." + values()[tags.getInt(NBT)].toString().toLowerCase();
    }
    catch (Exception e) {
      return "tool.action." + SINGLE.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    CompoundTag tags = wand.getOrCreateTag();
    int type = tags.getInt(NBT);
    type++;
    if (type >= values().length) {
      type = SINGLE.ordinal();
    }
    tags.putInt(NBT, type);
    wand.setTag(tags);
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundTag encoded = NbtUtils.writeBlockState(target);
    wand.getOrCreateTag().put(NBTBLOCKSTATE, encoded);
  }

  public static BlockState getBlockState(Level level, ItemStack wand) {
    if (!wand.getOrCreateTag().contains(NBTBLOCKSTATE)) {
      return null;
    }
    return NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), wand.getOrCreateTag().getCompound(NBTBLOCKSTATE));
  }
}
