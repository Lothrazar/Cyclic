package com.lothrazar.cyclicmagic.component.clock;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerClock extends ContainerBaseMachine {

  public ContainerClock(InventoryPlayer inventoryPlayer, TileEntityClock te) {
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
  }

}
