package com.lothrazar.cyclic.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IHasClickToggle {

  static final String NBT_STATUS = "onoff";

  /**
   * Item will be toggled between on <-> off when handling a @GuiScreenEvent.MouseClickedEvent.Pre
   * 
   * @param player
   *          using mouse
   * @param held
   *          itemstack where its item is one of this
   */
  void toggle(Player player, ItemStack held);

  /**
   * Check if the stack is currently on, based on its toggle state
   * 
   * @param held
   *          itemstack where its item is one of this
   * @return true if its toggle is on, false otherwise
   */
  boolean isOn(ItemStack held);
}
