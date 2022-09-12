package com.lothrazar.cyclic.item.crafting.simple;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
    return new TranslatableComponent("item.cyclic.crafting_stick");
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {
    //    FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
    //    buf.writeInt(this.slot);
    return new CraftingStickContainer(i, playerInventory, player, this.slot);
  }
}
