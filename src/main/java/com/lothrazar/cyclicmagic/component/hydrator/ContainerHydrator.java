package com.lothrazar.cyclicmagic.component.hydrator;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerHydrator extends ContainerBaseMachine {
  // tutorial used: http://www.minecraftforge.net/wiki/Containers_and_GUIs
  public static final int SLOTX_START = 8;
  public static final int SLOTY = 30;
  public static final int SQ = 18;
  public ContainerHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator te) {
    this.setTile(te); 
    bindPlayerInventory(inventoryPlayer);
    int slotNum = 0;
    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new Slot(tile, slotNum,
          SLOTX_START + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    }
    for (int i = 0; i < 4; i++) {
      addSlotToContainer(new SlotOutputOnly(tile, slotNum,
          132 + i / 2 * Const.SQ,
          SLOTY + i % 2 * Const.SQ));
      slotNum++;
    } 
    addSlotToContainer(new Slot(tile, slotNum,
        60,
        20));
    slotNum++;
    addSlotToContainer(new SlotOutputOnly(tile, slotNum,
        60,
        52));
    slotNum++;
    //    for (int i = 0; i < 9; i++) {
    //      addSlotToContainer(new Slot(tile, i, SLOTX_START + i * Const.SQ, SLOTY));
    //    }
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
