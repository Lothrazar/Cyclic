package com.lothrazar.cyclicmagic.block.firestarter;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiFireStarter extends GuiBaseContainer {

  public GuiFireStarter(InventoryPlayer inventoryPlayer, TileEntityFireStarter tile) {
    super(new ContainerFireStarter(inventoryPlayer, tile), tile);
    this.fieldRedstoneBtn = TileEntityFireStarter.Fields.REDSTONE.ordinal();

  }
}
