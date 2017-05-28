package com.lothrazar.cyclicmagic.component.harvester;
import com.lothrazar.cyclicmagic.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHarvester extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs

  public static final int SLOTX_FUEL = 8 * Const.SQ + Const.PAD;
  public static final int SLOTY_FUEL = Const.PAD;
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 40;
  public static final int SQ = 18;
 // protected TileEntityHarvester tileEntity;
  public ContainerHarvester(InventoryPlayer inventoryPlayer, TileEntityHarvester te) {
    //tileEntity = te;
    this.setTile(te);
    bindPlayerInventory(inventoryPlayer);
    addSlotToContainer(new SlotFurnaceFuel(tile, 0, SLOTX_FUEL, SLOTY_FUEL));
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
