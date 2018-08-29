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
package com.lothrazar.cyclicmagic.block.laser;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiLaser extends GuiBaseContainer {

  public GuiLaser(InventoryPlayer inventoryPlayer, TileEntityLaser te) {
    super(new ContainerLaser(inventoryPlayer, te), te);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityLaser.Fields.REDSTONE.ordinal();

  }

  @Override
  public void initGui() {
    super.initGui();
    //SLIDERS
    //    int y = 106;
    //    int size = Const.SQ;
    //    GuiButtonTooltip btnSize = new GuiButtonTooltip(TileCableContentWireless.SLOT_CARD_ITEM,
    //        this.guiLeft + colLeft,
    //        this.guiTop + y, size, size, "?");
    //    btnSize.setTooltip("wireless.target");
    //    this.addButton(btnSize);
    //    btnSize = new GuiButtonTooltip(TileCableContentWireless.SLOT_CARD_FLUID,
    //        this.guiLeft + colRight,
    //        this.guiTop + y, size, size, "?");
    //    btnSize.setTooltip("wireless.target");
    //    this.addButton(btnSize);
  }



  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      x = this.guiLeft + 30;
      y = this.guiTop + 42 + i * Const.SQ;
      Gui.drawModalRectWithCustomSizedTexture(
          x, y,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }

  }
}
