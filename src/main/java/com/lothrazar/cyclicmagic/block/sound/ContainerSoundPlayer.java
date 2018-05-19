package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerSoundPlayer extends ContainerBaseMachine {

  public ContainerSoundPlayer(InventoryPlayer inventoryPlayer, TileEntitySoundPlayer te) {
    super(te);
    //    this.setScreenSize(ScreenSize.LARGE);
    this.bindPlayerHotbar(inventoryPlayer);
  }

}
