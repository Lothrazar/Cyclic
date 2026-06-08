package com.lothrazar.cyclic.item.datacard.fluid;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ContainerProviderFluidFilterCard implements MenuProvider {

  @Override
  public Component getDisplayName() {
    return Component.translatable("item.cyclic.fluid_data");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    return new ContainerFluidFilterCard(i, playerInventory, player);
  }
}
