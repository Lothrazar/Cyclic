package com.lothrazar.cyclic.item.builder;

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
    return wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getInt(NBTTIMEOUT);
  }

  public static void setTimeout(ItemStack wand) {
    net.minecraft.nbt.CompoundTag tag = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag(); tag.putInt(NBTTIMEOUT, 15); wand.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag)); //less than one tick
  }

  public static void tickTimeout(ItemStack wand) {
    CompoundTag tags = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    int t = tags.getInt(NBTTIMEOUT);
    if (t > 0) {
      net.minecraft.nbt.CompoundTag tag = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag(); tag.putInt(NBTTIMEOUT, t - 1); wand.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
    }
  }

  public static int get(ItemStack wand) {
    if (wand.isEmpty()) {
      return 0;
    }
    CompoundTag tags = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    return tags.getInt(NBT);
  }

  public static String getName(ItemStack wand) {
    try {
      CompoundTag tags = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
      return "tool.action." + values()[tags.getInt(NBT)].toString().toLowerCase();
    }
    catch (Exception e) {
      return "tool.action." + SINGLE.toString().toLowerCase();
    }
  }

  public static void toggle(ItemStack wand) {
    CompoundTag tags = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag();
    int type = tags.getInt(NBT);
    type++;
    if (type >= values().length) {
      type = SINGLE.ordinal();
    }
    tags.putInt(NBT, type);
    wand.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tags));
  }

  public static void setBlockState(ItemStack wand, BlockState target) {
    CompoundTag encoded = NbtUtils.writeBlockState(target);
    net.minecraft.nbt.CompoundTag tag = wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag(); tag.put(NBTBLOCKSTATE, encoded); wand.set(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.of(tag));
  }

  public static BlockState getBlockState(Level level, ItemStack wand) {
    if (!wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().contains(NBTBLOCKSTATE)) {
      return null;
    }
    return NbtUtils.readBlockState(net.minecraft.core.registries.BuiltInRegistries.BLOCK.asLookup(), wand.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getCompound(NBTBLOCKSTATE));
  }
}
