package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiSoundPlayer extends GuiBaseContainer {

  public GuiSoundPlayer(InventoryPlayer inventoryPlayer, TileEntitySoundPlayer tile) {
    super(new ContainerSoundPlayer(inventoryPlayer, tile), tile);

  }
}
