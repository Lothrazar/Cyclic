package com.lothrazar.cyclic.util;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

public class UtilItemStack {

  public static void dropAll(IItemHandler items, World world, BlockPos pos) {
    if (items == null) {
      return;
    }
    for (int i = 0; i < items.getSlots(); i++) {
      InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), items.getStackInSlot(i));
    }
  }

  public static void repairItem(ItemStack s) {
    s.setDamage(s.getDamage() - 1);
  }

  public static void damageItem(ItemStack s) {
    s.setDamage(s.getDamage() + 1);
  }

  public static void drop(World world, BlockPos pos, ItemStack drop) {
    if (!world.isRemote)
      world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), drop));
  }

  public static float getBlockHardness(BlockState state, World world, BlockPos pos) {
    return state.getBlock().getBlockHardness(state, world, pos);
  }
}
