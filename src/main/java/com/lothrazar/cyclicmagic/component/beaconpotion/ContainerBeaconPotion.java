package com.lothrazar.cyclicmagic.component.beaconpotion;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotPotion;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBeaconPotion extends ContainerBaseMachine {
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 52;
  public ContainerBeaconPotion(InventoryPlayer inventoryPlayer, TileEntityBeaconPotion te) {
    this.setTile(te);
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new SlotPotion(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    }
    super.bindPlayerInventory(inventoryPlayer);
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
