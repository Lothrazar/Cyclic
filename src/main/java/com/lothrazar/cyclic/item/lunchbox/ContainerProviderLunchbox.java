package com.lothrazar.cyclic.item.lunchbox;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderLunchbox implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("item.cyclic.lunchbox");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerLunchbox(i, playerInventory, player);
  }
}
