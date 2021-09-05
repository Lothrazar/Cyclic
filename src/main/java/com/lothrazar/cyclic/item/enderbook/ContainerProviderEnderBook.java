package com.lothrazar.cyclic.item.enderbook;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ContainerProviderEnderBook implements INamedContainerProvider {

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("item.cyclic.ender_book");
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new ContainerEnderBook(i, playerInventory, player);
  }
}
