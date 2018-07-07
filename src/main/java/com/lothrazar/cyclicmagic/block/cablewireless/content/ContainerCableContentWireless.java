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
package com.lothrazar.cyclicmagic.block.cablewireless.content;

import com.lothrazar.cyclicmagic.core.gui.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.slot.SlotCheckTileValid;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerCableContentWireless extends ContainerBaseMachine {

  public ContainerCableContentWireless(InventoryPlayer inventoryPlayer, TileCableContentWireless te) {
    super(te);
    this.setScreenSize(ScreenSize.LARGE);

    int x = 43;
    int y = 43;
    addSlotToContainer(new SlotCheckTileValid(te, TileCableContentWireless.SLOT_TRANSFER, x, y));
    //other three slots 
    y = 87;
    addSlotToContainer(new SlotCheckTileValid(te, TileCableContentWireless.SLOT_CARD_ITEM, x, y));
    x += 72;
    addSlotToContainer(new SlotCheckTileValid(te, TileCableContentWireless.SLOT_CARD_FLUID, x, y));


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
  //TODO: transfer sstack without deleting cards

}
