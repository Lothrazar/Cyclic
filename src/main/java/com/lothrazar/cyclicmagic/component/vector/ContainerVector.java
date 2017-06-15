package com.lothrazar.cyclicmagic.component.vector;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerVector extends ContainerBaseMachine {
  protected TileEntityVector tileEntity;
  public ContainerVector(InventoryPlayer inventoryPlayer, TileEntityVector te) {
    tileEntity = te;
    this.setTile(te);
    bindPlayerHotbar(inventoryPlayer);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    this.tileEntity.setField(id, data);
  }
  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, this.tileEntity);
  }
}
