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
package com.lothrazar.cyclicmagic.gui.core;

import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public class ContainerBase extends Container {

  private Const.ScreenSize screenSize = ScreenSize.STANDARD;

  protected void setScreenSize(Const.ScreenSize ss) {
    this.screenSize = ss;
  }

  public Const.ScreenSize getScreenSize() {
    return screenSize;
  }

  @Override
  public boolean canInteractWith(EntityPlayer playerIn) {
    return true;
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

  protected void bindPlayerHotbar(InventoryPlayer inventoryPlayer) {
    for (int i = 0; i < 9; i++) {
      addSlotToContainer(new Slot(inventoryPlayer, i, getScreenSize().playerOffsetX() + i * Const.SQ, getScreenSize().playerOffsetY() + Const.PAD / 2 + 3 * Const.SQ));
    }
  }
}
