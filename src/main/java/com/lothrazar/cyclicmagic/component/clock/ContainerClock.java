package com.lothrazar.cyclicmagic.component.clock;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerClock extends ContainerBaseMachine {
  public ContainerClock(InventoryPlayer inventoryPlayer, TileEntityClock te) {
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
  }
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
