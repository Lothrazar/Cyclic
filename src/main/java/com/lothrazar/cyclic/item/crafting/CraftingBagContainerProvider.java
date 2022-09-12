package com.lothrazar.cyclic.item.crafting;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CraftingBagContainerProvider implements MenuProvider {

  private int slot;

  public CraftingBagContainerProvider(int s) {
    this.slot = s;
  }

  @Override
  public Component getDisplayName() {
    return new TranslatableComponent("item.cyclic.crafting_bag");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new CraftingBagContainer(i, playerInventory, player, slot);
  }
}
