package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class StorageBagContainerProvider implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return Component.translatable("cyclic.items.storage_bag");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerStorageBag(i, playerInventory, player);
  }
}
