package com.lothrazar.cyclicmagic.component.screen;

import com.lothrazar.cyclicmagic.component.vector.TileEntityVector;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerScreen extends ContainerBaseMachine {
  
  
  public ContainerScreen(InventoryPlayer inventoryPlayer, TileEntityScreen te) {
 
    this.setTile(te);
    bindPlayerHotbar(inventoryPlayer);
  }
  
}
