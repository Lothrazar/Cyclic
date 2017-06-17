package com.lothrazar.cyclicmagic.component.entitydetector;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerDetector extends ContainerBaseMachine {
  public static final int SLOTX_START = 8;
  public static final int SLOTY_START = 90;
  protected TileEntityDetector tileEntity;
  public ContainerDetector(InventoryPlayer inventoryPlayer, TileEntityDetector te) {
    tileEntity = te;
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
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
