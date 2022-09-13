package com.lothrazar.cyclic.item.crafting;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CraftingBagContainerProvider implements INamedContainerProvider {

  private int slot;

  public CraftingBagContainerProvider(int s) {
    this.slot = s;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("item.cyclic.crafting_bag");
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new CraftingBagContainer(i, playerInventory, player, slot);
  }
}
