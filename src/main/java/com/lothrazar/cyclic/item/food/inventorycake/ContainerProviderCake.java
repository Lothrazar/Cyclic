package com.lothrazar.cyclic.item.food.inventorycake;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderCake implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return Component.translatable("item.cyclic.inventory_cake");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerCake(i, playerInventory, player);
  }
}
