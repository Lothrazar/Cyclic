package com.lothrazar.cyclic.item.craftingsimple;

import javax.annotation.Nullable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CraftingStickContainerProvider implements INamedContainerProvider {

  private Hand hand;

  public CraftingStickContainerProvider(Hand handIn) {
    this.hand = handIn;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new TranslationTextComponent("item.cyclic.crafting_stick");
  }

  @Nullable
  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity player) {
    return new CraftingStickContainer(i, playerInventory, player, hand);
  }
}
