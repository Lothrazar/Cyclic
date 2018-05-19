package com.lothrazar.cyclicmagic.block.voidshelf;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;

public class ContainerVoidAnvil extends ContainerBaseMachine {

  public ContainerVoidAnvil(InventoryPlayer inventoryPlayer, TileEntityVoidAnvil te) {
    super(te);
    //    this.setScreenSize(ScreenSize.LARGE);
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      addSlotToContainer(new Slot(tile, i, 40 + i * Const.SQ * 2, 60));
    }
    bindPlayerInventory(inventoryPlayer);
  }

}
