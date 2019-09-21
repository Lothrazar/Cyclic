package com.lothrazar.cyclic.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class UtilItemStack {

  public static void repairItem(ItemStack s) {
    s.setDamage(s.getDamage() - 1);
  }

  public static void damageItem( ItemStack s) {
    s.setDamage(s.getDamage() + 1);
  }
}
