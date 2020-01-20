package com.lothrazar.cyclic.util;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilItemStack {

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
}
