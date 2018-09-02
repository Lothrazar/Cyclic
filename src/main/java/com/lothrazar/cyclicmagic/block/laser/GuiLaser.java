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

import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiLaser extends GuiBaseContainer {

  private ButtonTileEntityField btnPulsing;
  private ButtonTileEntityField btnExtending;
  private ButtonTileEntityField btnX;
  private ButtonTileEntityField btnY;
  private ButtonTileEntityField btnZ;

  public GuiLaser(InventoryPlayer inventoryPlayer, TileEntityLaser te) {
    super(new ContainerLaser(inventoryPlayer, te), te);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityLaser.Fields.REDSTONE.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 0, x = guiLeft + 36, y = guiTop + 16, width = 120, h = 12;
    GuiSliderInteger sliderX = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, TileEntityLaser.Fields.R.ordinal());
    sliderX.setTooltip("screen.red");
    this.addButton(sliderX);
    y += h + 4;
    sliderX = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, TileEntityLaser.Fields.G.ordinal());
    sliderX.setTooltip("screen.green");
    this.addButton(sliderX);
    y += h + 4;
    sliderX = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, TileEntityLaser.Fields.B.ordinal());
    sliderX.setTooltip("screen.blue");
    this.addButton(sliderX);
    y += h + 4;
    sliderX = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 100, TileEntityLaser.Fields.ALPHA.ordinal());
    sliderX.setTooltip("screen.alpha");
    this.addButton(sliderX);
    //
    x -= Const.PAD;
    y += 16;
    btnPulsing = new ButtonTileEntityField(id++, x, y, this.tile.getPos(), TileEntityLaser.Fields.PULSE.ordinal());
    btnPulsing.width = 64;
    btnPulsing.setTooltip("button.pulsing.tooltip");
    this.addButton(btnPulsing);
    x += btnPulsing.width + Const.PAD;
    btnExtending = new ButtonTileEntityField(id++, x, y, this.tile.getPos(), TileEntityLaser.Fields.EXTENDING.ordinal());
    btnExtending.width = 64;
    btnExtending.setTooltip("button.extending.tooltip");
    this.addButton(btnExtending);
    y += 24;
    // three ctrls
    x = guiLeft + 29;
    int wid = 42;
    btnX = new ButtonTileEntityField(id++, x, y, this.tile.getPos(), TileEntityLaser.Fields.XOFF.ordinal());
    btnX.width = wid;
    btnX.setTooltip("button.offsetx.tooltip");
    this.addButton(btnX);
    x += wid + Const.PAD / 2;
    btnY = new ButtonTileEntityField(id++, x, y, this.tile.getPos(), TileEntityLaser.Fields.YOFF.ordinal());
    btnY.width = wid;
    btnY.setTooltip("button.offsety.tooltip");
    this.addButton(btnY);
    x += wid + Const.PAD / 2;
    btnZ = new ButtonTileEntityField(id++, x, y, this.tile.getPos(), TileEntityLaser.Fields.ZOFF.ordinal());
    btnZ.width = wid;
    btnZ.setTooltip("button.offsetz.tooltip");
    this.addButton(btnZ);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    btnPulsing.displayString = UtilChat.lang("button.pulsing.name" + this.tile.getField(TileEntityLaser.Fields.PULSE.ordinal()));
    btnExtending.displayString = UtilChat.lang("button.extending.name" + this.tile.getField(TileEntityLaser.Fields.EXTENDING.ordinal()));
    btnX.displayString = UtilChat.lang("button.offsetblock.name" + this.tile.getField(TileEntityLaser.Fields.XOFF.ordinal()));
    btnY.displayString = UtilChat.lang("button.offsetblock.name" + this.tile.getField(TileEntityLaser.Fields.YOFF.ordinal()));
    btnZ.displayString = UtilChat.lang("button.offsetblock.name" + this.tile.getField(TileEntityLaser.Fields.ZOFF.ordinal()));
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      x = this.guiLeft + 7;
      y = this.guiTop + 42 + i * Const.SQ;
      Gui.drawModalRectWithCustomSizedTexture(
          x, y,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }
}
