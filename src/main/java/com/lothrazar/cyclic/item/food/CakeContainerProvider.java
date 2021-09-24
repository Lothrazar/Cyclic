package com.lothrazar.cyclic.item.food;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CakeContainerProvider implements INamedContainerProvider {

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("item.cyclic.inventory.name");
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new CakeContainer(i, playerInventory, player);
  }
}
