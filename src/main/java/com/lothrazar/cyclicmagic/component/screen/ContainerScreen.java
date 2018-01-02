package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerScreen extends ContainerBaseMachine {
  public ContainerScreen(InventoryPlayer inventoryPlayer, TileEntityScreen te) {
    this.setTile(te);
    bindPlayerHotbar(inventoryPlayer);
  }
  //TODO: why are these not working from baseclass IDK
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tile.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tile);
  }
}
