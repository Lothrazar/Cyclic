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
package com.lothrazar.cyclicmagic.block.fan;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFan extends GuiBaseContainer {

  private TileEntityFan tile;
  boolean debugLabels = false;
  //private ButtonIncrementField btnHeightDown;
  //  private ButtonIncrementField btnHeightUp;
  private int xRange = 176 - 25;
  private int yHeightTxtbox = 38;
  //  private ButtonFan btnTogglePrt;
  private ButtonTileEntityField btnTogglePush;

  public GuiFan(InventoryPlayer inventoryPlayer, TileEntityFan tileEntity) {
    super(new ContainerFan(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityFan.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityFan.Fields.PARTICLES.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    int w = 18, h = 10;
    int yOffset = 14;
    int x = this.guiLeft + 30;
    int y = this.guiTop + 22;
    int field = TileEntityFan.Fields.RANGE.ordinal();
    GuiSliderInteger sliderDelay = new GuiSliderInteger(tile, id++, x, y, 130, h, 1, TileEntityFan.MAX_RANGE,
        field, true);
    sliderDelay.setTooltip("button.fan.range.tooltip");
    this.addButton(sliderDelay);
    //    

    ///////////////// SPEED BUTTONS
    y += 18;
    field = TileEntityFan.Fields.SPEED.ordinal();
    GuiSliderInteger sliderOffset = new GuiSliderInteger(tile, id++, x, y, 130, h, 1, TileEntityFan.MAX_SPEED,
        field, true);
    sliderOffset.setTooltip("button.fan.speed.tooltip");
    this.addButton(sliderOffset);

    //the big push pull toggle button
    w = 70;
    h = 20;
    x = this.guiLeft + 50;
    y = this.guiTop + 58;
    btnTogglePush = new ButtonTileEntityField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.PUSHPULL.ordinal(), +1, w, h);
    this.addButton(btnTogglePush);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFan.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFan.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    //    btnTogglePrt.updateDisplayStringWith(tile);
    //    btnTogglePush.updateDisplayStringWith(tile);
    btnTogglePush.displayString = UtilChat.lang("button.fan.pushpull" + tile.getField(TileEntityFan.Fields.PUSHPULL.ordinal()));
    //    String display = "" + this.tile.getRange();
    //    int x = (display.length() > 1) ? xRange + 2 : xRange + 3;
    //    this.drawString(display, x, yHeightTxtbox);
    //    display = "" + this.tile.getSpeed();
    //    x -= 20;
    //    this.drawString(display, x, yHeightTxtbox);
    //    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileMachineHarvester.Fields.SIZE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
