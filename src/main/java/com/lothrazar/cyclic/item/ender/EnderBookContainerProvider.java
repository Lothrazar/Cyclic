package com.lothrazar.cyclic.item.ender;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class EnderBookContainerProvider implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("item.cyclic.ender_book");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new EnderBookContainer(i, playerInventory, player);
  }
}
