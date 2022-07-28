package com.lothrazar.cyclic.item.datacard.filter;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderFilterCard implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return Component.translatable("item.cyclic.filter_data");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerFilterCard(i, playerInventory, player);
  }
}
