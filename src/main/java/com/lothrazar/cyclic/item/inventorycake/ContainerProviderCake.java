package com.lothrazar.cyclic.item.inventorycake;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class ContainerProviderCake implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("item.cyclic.inventory_cake");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerCake(i, playerInventory, player);
  }
}
