package com.lothrazar.cyclic.item.crafting.simple;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CraftingStickContainerProvider implements MenuProvider {

  private int slot;

  public CraftingStickContainerProvider(int s) {
    this.slot = s;
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("item.cyclic.crafting_stick");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new CraftingStickContainer(i, playerInventory, player, this.slot);
  }
}
