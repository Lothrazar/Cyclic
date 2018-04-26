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
package com.lothrazar.cyclicmagic.block.disenchanter;

import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDisenchanter extends GuiBaseContainer {

  private TileEntityDisenchanter tile;

  public GuiDisenchanter(InventoryPlayer inventoryPlayer, TileEntityDisenchanter tileEntity) {
    super(new ContainerDisenchanter(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.xSize = getScreenSize().width();
    this.ySize = getScreenSize().height();
    this.fieldRedstoneBtn = TileEntityDisenchanter.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 6 * Const.SQ + 10, TileEntityDisenchanter.Fields.TIMER.ordinal(), TileEntityDisenchanter.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
    energyBar.setX(156).setY(22);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

    int u = 0, v = 0;
    int x = 0, y = 0, ystart = 20, spacing = 26;
    for (int i = 0; i < tile.getSizeInventory(); i++) {
      switch (i) {
        case TileEntityDisenchanter.SLOT_BOOK://center center
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT_BOOK);
          x = getScreenSize().width() / 2;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_GLOWSTONE://left mid
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GLOWSTONE);
          x = getScreenSize().width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_BOTTLE://bottom center
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT_EBOTTLE);
          x = getScreenSize().width() / 2;
          y = ystart + 2 * spacing;
        break;
        case TileEntityDisenchanter.SLOT_REDSTONE:// right mid
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT_REDST);
          x = getScreenSize().width() - getScreenSize().width() / 4;
          y = ystart + spacing;
        break;
        case TileEntityDisenchanter.SLOT_INPUT://top center
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
          x = getScreenSize().width() / 2;
          y = ystart;
        break;
        default:
          this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
          x = Const.PAD + (i - 5) * Const.SQ;
          y = ystart + 3 * spacing - 1;
        break;
      }
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft - 1 + x, this.guiTop - 1 + y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    //DRAW ENERGY BAR
    fuelX = this.guiLeft + 154;
    fuelY = this.guiTop + 17;
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
