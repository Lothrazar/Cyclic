package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class StorageBagContainerProvider implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("cyclic.items.storage_bag");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerStorageBag(i, playerInventory, player);
  }
}
