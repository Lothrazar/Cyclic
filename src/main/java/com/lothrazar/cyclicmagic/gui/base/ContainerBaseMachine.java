/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.gui.base;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.slot.SlotFuel;
import com.lothrazar.cyclicmagic.gui.slot.SlotOutputOnly;
import com.lothrazar.cyclicmagic.net.PacketGuiShortOverride;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
  public ContainerBaseMachine(TileEntityBaseMachineInvo t) {
    this.setTile(t);
  }
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
          if ((this.tile.getField(fieldId) > Short.MAX_VALUE ||
              this.tile.getField(fieldId) < Short.MIN_VALUE)
              && icontainerlistener instanceof EntityPlayerMP) {
            //minecraft truncates int into short
            ModCyclic.network.sendTo(
                new PacketGuiShortOverride(fieldId, this.tile.getField(fieldId)), ((EntityPlayerMP) icontainerlistener));
          }
          else {
            icontainerlistener.sendWindowProperty(this, fieldId, this.tile.getField(fieldId));
          }
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
    if (tile != null) {
      this.syncFields();
    }
    super.detectAndSendChanges();
  }
  @Override
  public ItemStack transferStackInSlot(EntityPlayer player, int slot) {
    return ItemStack.EMPTY;
  }
  //cant have no slots. breaks shiftclick
  public void addFurnaceFuelSlot(int slotxFuel, int slotyFuel) {
    Slot fuel;
    if (tile.doesUseFuel()) {
      fuel = new SlotFuel(tile, tile.getSizeInventory() - 1, slotxFuel, slotyFuel);
    }
    else {
      fuel = new SlotOutputOnly(tile, tile.getSizeInventory() - 1, slotxFuel, slotyFuel + 9999);//LITERALLY OFF SCREEN
    }
    addSlotToContainer(fuel);
  }
}
