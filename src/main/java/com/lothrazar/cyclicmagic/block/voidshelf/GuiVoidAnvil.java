package com.lothrazar.cyclicmagic.block.voidshelf;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiVoidAnvil extends GuiBaseContainer {

  public GuiVoidAnvil(InventoryPlayer inventoryPlayer, TileEntityVoidAnvil tile) {
    super(new ContainerVoidAnvil(inventoryPlayer, tile), tile);

  }
}
