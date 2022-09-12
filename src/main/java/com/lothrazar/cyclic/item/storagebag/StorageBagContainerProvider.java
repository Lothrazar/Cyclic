package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class StorageBagContainerProvider implements MenuProvider {

  private int slot;

  public StorageBagContainerProvider(int s) {
    this.slot = s;
  }

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("cyclic.items.storage_bag");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerStorageBag(i, playerInventory, player, slot);
  }
}
