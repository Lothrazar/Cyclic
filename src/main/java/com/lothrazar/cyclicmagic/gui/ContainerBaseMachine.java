package com.lothrazar.cyclicmagic.gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerBaseMachine extends ContainerBase {
  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
            8 + j * 18, /// X
            84 + i * 18// Y
        ));
      }
    }
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
    }
  }
}
