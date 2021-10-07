package com.lothrazar.cyclic.base;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IHasClickToggle {

  static final String NBT_STATUS = "onoff";

  void toggle(Player player, ItemStack held);

  boolean isOn(ItemStack held);
}
