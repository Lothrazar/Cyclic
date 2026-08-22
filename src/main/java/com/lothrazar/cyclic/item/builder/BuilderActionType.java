package com.lothrazar.cyclic.item.builder;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public enum BuilderActionType {

  SINGLE, X3, X5, X7, X9, X91, X19;

  public static final String NBTBLOCKSTATE = "blockstate";
  private static final String NBT = "ActionType";
  private static final String NBTTIMEOUT = "timeout";

  public static int getTimeout(ItemStack wand) {
    return wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr(NBTTIMEOUT, 0);
  }

  public static void setTimeout(ItemStack wand) {
    CompoundTag tag = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.putInt(NBTTIMEOUT, 15);
    wand.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)); //less than one tick
  }

  public static void tickTimeout(ItemStack wand) {
    CompoundTag tags = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    int t = tags.getIntOr(NBTTIMEOUT, 0);
    if (t > 0) {
      CompoundTag tag = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      tag.putInt(NBTTIMEOUT, t - 1);
      wand.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
  }

  public static int get(ItemStack wand) {
    if (wand.isEmpty()) {
      return 0;
    }
    CompoundTag tags = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    return tags.getIntOr(NBT, 0);
  }

  public static String getName(ItemStack wand) {
    try {
      CompoundTag tags = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
      return "tool.action." + values()[tags.getIntOr(NBT, 0)].toString().toLowerCase();
    } catch (Exception e) {
      return "tool.action." + SINGLE.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    CompoundTag tags = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    int type = tags.getIntOr(NBT, 0);
    type++;
    if (type >= values().length) {
      type = SINGLE.ordinal();
    }
    tags.putInt(NBT, type);
    wand.set(DataComponents.CUSTOM_DATA, CustomData.of(tags));
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundTag encoded = NbtUtils.writeBlockState(target);
    CompoundTag tag = wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    tag.put(NBTBLOCKSTATE, encoded);
    wand.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
  }

  public static BlockState getBlockState(Level level, ItemStack wand) {
    if (!wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(NBTBLOCKSTATE)) {
      return null;
    }
    return NbtUtils.readBlockState(BuiltInRegistries.BLOCK, wand.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getCompoundOrEmpty(NBTBLOCKSTATE));
  }
}
