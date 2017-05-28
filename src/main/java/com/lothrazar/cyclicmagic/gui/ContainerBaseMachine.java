package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBaseMachine extends ContainerBase { 
  public static final int SLOTX_FUEL = 8 * Const.SQ + Const.PAD;
  public static final int SLOTY_FUEL = Const.PAD;
  private int[] tileMap;
  protected TileEntityBaseMachineInvo tile;
  protected Const.ScreenSize screenSize = ScreenSize.STANDARD;
  public ContainerBaseMachine() {}
  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
            screenSize.playerOffsetX() + j * Const.SQ, /// X
            screenSize.playerOffsetY() + i * Const.SQ// Y
        ));
      }
    }
    bindPlayerHotbar(inventoryPlayer);
  }
  protected void setTile(TileEntityBaseMachineInvo tile) {
    this.tile = tile;
    this.tileMap = new int[tile.getFieldOrdinals().length];
  }
  protected void bindPlayerHotbar(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, screenSize.playerOffsetX() + i * Const.SQ, screenSize.playerOffsetY() + Const.PAD / 2 + 3 * Const.SQ));
    }
  }
  protected void syncFields() {
    int fieldId;
    for (int i = 0; i < this.listeners.size(); ++i) {
      IContainerListener icontainerlistener = this.listeners.get(i);
      for (int j = 0; j < tile.getFieldOrdinals().length; j++) {
        fieldId = tile.getFieldOrdinals()[j];
        if (this.tileMap[j] != this.tile.getField(fieldId)) {
          icontainerlistener.sendProgressBarUpdate(this, fieldId, this.tile.getField(fieldId));
        }
      }
    }
    for (int j = 0; j < tile.getFieldOrdinals().length; j++) {
      fieldId = tile.getFieldOrdinals()[j];
      this.tileMap[j] = this.tile.getField(fieldId);
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
