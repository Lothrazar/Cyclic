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
package com.lothrazar.cyclicmagic.core.gui;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.net.PacketGuiShortOverride;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBaseMachine extends ContainerBase {

  public static final int SLOTX_FUEL = 8 * Const.SQ + Const.PAD;
  public static final int SLOTY_FUEL = Const.PAD;
  private int[] tileMap;
  protected TileEntityBaseMachineInvo tile;
  private Const.ScreenSize screenSize = ScreenSize.STANDARD;
  private boolean hasTile;

  public ContainerBaseMachine() {
    this.hasTile = false;
  }

  public ContainerBaseMachine(TileEntityBaseMachineInvo t) {
    this.setTile(t);
  }

  protected void setScreenSize(Const.ScreenSize ss) {
    this.screenSize = ss;
  }

  public Const.ScreenSize getScreenSize() {
    return screenSize;
  }

  protected void bindPlayerInventory(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 9; j++) {
        addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9,
            getScreenSize().playerOffsetX() + j * Const.SQ, /// X
            getScreenSize().playerOffsetY() + i * Const.SQ// Y
        ));
      }
    }
    bindPlayerHotbar(inventoryPlayer);
  }

  private void setTile(TileEntityBaseMachineInvo tile) {
    this.hasTile = true;
    this.tile = tile;
    this.tileMap = new int[tile.getFieldOrdinals().length];
  }

  protected void bindPlayerHotbar(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, getScreenSize().playerOffsetX() + i * Const.SQ, getScreenSize().playerOffsetY() + Const.PAD / 2 + 3 * Const.SQ));
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

  @Override
  @SideOnly(Side.CLIENT)
  public void updateProgressBar(int id, int data) {
    tile.setField(id, data);
  }

  @Override
  public void addListener(IContainerListener listener) {
    super.addListener(listener);
    listener.sendAllWindowProperties(this, tile);
  }

  @Override
  public boolean canInteractWith(EntityPlayer player) {
    if (this.hasTile == false) {
      return super.canInteractWith(player);
    }
    return player.getDistanceSq(this.tile.getPos().getX(), this.tile.getPos().getY(), this.tile.getPos().getZ()) <= 32
        && this.tile.isValid() && this.tile.getWorld().getTileEntity(this.tile.getPos()) == tile;
  }
}
