package com.lothrazar.cyclic.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IHasClickToggle {

  static final String NBT_STATUS = "onoff";

  void toggle(PlayerEntity player, ItemStack held);

  boolean isOn(ItemStack held);
}
