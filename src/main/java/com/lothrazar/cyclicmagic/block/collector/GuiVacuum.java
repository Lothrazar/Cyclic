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
package com.lothrazar.cyclicmagic.block.collector;

import com.lothrazar.cyclicmagic.core.ITileStackWrapper;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.StackWrapper;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiVacuum extends GuiBaseContainer {

  ITileStackWrapper te;
  private GuiButtonToggleSize btnSize;

  public GuiVacuum(InventoryPlayer inventoryPlayer, TileEntityVacuum tileEntity) {
    super(new ContainerVacuum(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityVacuum.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityVacuum.Fields.RENDERPARTICLES.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 2;
    int x = this.guiLeft + 28;
    int y = this.guiTop + 32;
    btnSize = new GuiButtonToggleSize(id++, x, y, this.tile.getPos());
    this.buttonList.add(btnSize);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    int xCenter = this.xSize / 2;
    String s = UtilChat.lang("tile.block_vacuum.filter");
    this.drawString(s, xCenter - 30 - this.fontRenderer.getStringWidth(s) / 2, 20);
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityVacuum.Fields.SIZE.ordinal()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int i = 0; i < TileEntityVacuum.ROWS; i++) {
      for (int j = 0; j < TileEntityVacuum.COLS; j++) {
        Gui.drawModalRectWithCustomSizedTexture(
            this.guiLeft + Const.PAD + j * Const.SQ - 1,
            this.guiTop + 72 + (i - 1) * Const.SQ - 1,
            u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    int slotNum = 0;
    for (int k = 0; k < te.getWrapperCount() / 2; k++) {
      x = this.guiLeft + Const.PAD + (k + 4) * Const.SQ - 1;
      y = this.guiTop + Const.SQ - 2;
      Gui.drawModalRectWithCustomSizedTexture(
          x, y,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      StackWrapper wrap = te.getStackWrapper(slotNum);
      wrap.setX(x);
      wrap.setY(y);
      slotNum++;
    }
    for (int k = te.getWrapperCount() / 2; k < te.getWrapperCount(); k++) {
      x = this.guiLeft + Const.PAD + (k - 1) * Const.SQ - 1;
      y = this.guiTop + 2 * Const.SQ - 2;
      Gui.drawModalRectWithCustomSizedTexture(
          x, y,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      StackWrapper wrap = te.getStackWrapper(slotNum);
      wrap.setX(x);
      wrap.setY(y);
      slotNum++;
    }
    this.renderStackWrappers(te);
  }
}
