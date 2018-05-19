package com.lothrazar.cyclicmagic.block.voidshelf;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerVoidAnvil extends ContainerBaseMachine {

  public ContainerVoidAnvil(InventoryPlayer inventoryPlayer, TileEntityVoidAnvil te) {
    super(te);
    //    this.setScreenSize(ScreenSize.LARGE);
    //    for (int i = 0; i < tile.getSizeInventory(); i++) {
    //      addSlotToContainer(new SlotOnlyBlocks(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    //    }
    bindPlayerInventory(inventoryPlayer);
  }

}
