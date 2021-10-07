package com.lothrazar.cyclic.item.enderbook;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ContainerProviderEnderBook implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("item.cyclic.ender_book");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerEnderBook(i, playerInventory, player);
  }
}
