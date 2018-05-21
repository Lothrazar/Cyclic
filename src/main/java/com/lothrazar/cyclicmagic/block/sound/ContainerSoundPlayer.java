package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerSoundPlayer extends ContainerBaseMachine {

  public ContainerSoundPlayer(InventoryPlayer inventoryPlayer, TileEntitySoundPlayer te) {
    super(te);
    this.setScreenSize(ScreenSize.PLAINWIDE);
    this.bindPlayerHotbar(inventoryPlayer);
  }

}
