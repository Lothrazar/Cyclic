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
package com.lothrazar.cyclicmagic.block.crafter;

import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class GuiCrafter extends GuiBaseContainer {

  TileEntityCrafter tileCrafter;

  public GuiCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter tileEntity) {
    super(new ContainerCrafter(inventoryPlayer, tileEntity), tileEntity);
    tileCrafter = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityCrafter.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 6 * Const.SQ + 10, TileEntityCrafter.Fields.TIMER.ordinal(), TileEntityCrafter.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
    energyBar.setHeight(82).setWidth(14).setY(28).setX(150);
  }

  @Override
  public void initGui() {
    super.initGui();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    //input
    int xPrefix = Const.PAD, yPrefix = ContainerCrafter.SLOTY;
    int rows = TileEntityCrafter.ROWS;
    int cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //grid 
    //grid
    rows = cols = 3;
    xPrefix = (getScreenSize().width() / 2 - 40);//calculate exact center
    yPrefix = ContainerCrafter.SLOTY + 2 * Const.SQ;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //output
    xPrefix = 106;
    yPrefix = ContainerCrafter.SLOTY;
    rows = TileEntityCrafter.ROWS;
    cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //phantom recipe output
    ItemStack recipeOutput = this.tileCrafter.getRecipeResult();
    if (!recipeOutput.isEmpty()) {
      int x = guiLeft + 67;
      int y = guiTop + 30;
      GlStateManager.pushMatrix();
      RenderHelper.enableGUIStandardItemLighting();
      mc.getRenderItem().renderItemAndEffectIntoGUI(recipeOutput, x, y);
      //keep this render quantity for later
      if (recipeOutput.getCount() > 1)
        mc.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, recipeOutput, x + 1, y + 1, recipeOutput.getCount() + "");
      GlStateManager.popMatrix();
    }
  }
}
