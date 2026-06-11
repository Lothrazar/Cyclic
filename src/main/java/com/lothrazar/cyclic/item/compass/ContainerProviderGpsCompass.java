package com.lothrazar.cyclic.item.compass;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderGpsCompass implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return Component.translatable("item.cyclic.compass_gps");
  }

  @Override
  public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player player) {
    return new ContainerGpsCompass(windowId, playerInventory, player);
  }
}
