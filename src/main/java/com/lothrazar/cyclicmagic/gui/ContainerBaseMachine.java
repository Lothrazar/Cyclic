package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.component.autouser.TileEntityUser;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBaseMachine extends ContainerBase {
  public int playerOffsetX = 8;
  public int playerOffsetY = 84;
  private TileEntityBaseMachineInvo te;
  private int[] tileMap;
  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
            playerOffsetX + j * Const.SQ, /// X
            playerOffsetY + i * Const.SQ// Y
        ));
      }
    }
    bindPlayerHotbar(inventoryPlayer);
  }
  protected void setTile(TileEntityBaseMachineInvo tile) {
    this.te = tile;
    this.tileMap = new int[te.getFieldOrdinals().length];
  }
  protected void bindPlayerHotbar(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, playerOffsetX + i * Const.SQ, playerOffsetY + Const.PAD / 2 + 3 * Const.SQ));
    }
  }
  protected void syncFields() {
    int fieldId;
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = this.listeners.get(i);
      for (int j = 0; j < te.getFieldOrdinals().length; j++) {
        fieldId = te.getFieldOrdinals()[j];
        if (this.tileMap[j] != this.te.getField(fieldId)) {
          icontainerlistener.sendProgressBarUpdate(this, fieldId, this.te.getField(fieldId));
        }
      }
    }
    for (int j = 0; j < te.getFieldOrdinals().length; j++) {
      fieldId = te.getFieldOrdinals()[j];
      this.tileMap[j] = this.te.getField(fieldId);
    }
  }
  @Override
  public void detectAndSendChanges() {
    this.syncFields();
    super.detectAndSendChanges();
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return ItemStack.EMPTY;
  }
}
