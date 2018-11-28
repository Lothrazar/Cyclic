package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.gui.core.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFireStarter extends ContainerBaseMachine {

  public ContainerFireStarter(InventoryPlayer inventoryPlayer, TileEntityFireStarter te) {
    super(te);
    //    this.setScreenSize(ScreenSize.LARGE);
    //    for (int i = 0; i < tile.getSizeInventory(); i++) {
    //      addSlotToContainer(new SlotOnlyBlocks(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    //    }
    bindPlayerInventory(inventoryPlayer);
  }
}
