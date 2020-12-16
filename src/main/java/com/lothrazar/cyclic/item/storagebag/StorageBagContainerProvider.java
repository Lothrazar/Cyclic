package com.lothrazar.cyclic.item.storagebag;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class StorageBagContainerProvider implements INamedContainerProvider {
  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("cyclic.items.storage_bag");
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new StorageBagContainer(i, playerInventory, player);
  }
}
