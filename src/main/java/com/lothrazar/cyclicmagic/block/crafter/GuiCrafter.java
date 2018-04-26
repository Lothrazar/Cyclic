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

import java.io.IOException;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiCrafter extends GuiBaseContainer {

  public GuiCrafter(InventoryPlayer inventoryPlayer, TileEntityCrafter tileEntity) {
    super(new ContainerCrafter(inventoryPlayer, tileEntity), tileEntity);
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityCrafter.Fields.REDSTONE.ordinal();
    this.progressBar = new ProgressBar(this, 10, 6 * Const.SQ + 10, TileEntityCrafter.Fields.TIMER.ordinal(), TileEntityCrafter.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
  }

  @Override
  public void initGui() {
    super.initGui();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
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
    rows = cols = 3;
    xPrefix = (getScreenSize().width() / 2 - (Const.SQ * 3) / 2);//calculate exact center
    yPrefix = ContainerCrafter.SLOTY + Const.SQ;


    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        x = guiLeft + xPrefix - 1 + j * Const.SQ;
        y = guiTop + yPrefix - 1 + i * Const.SQ;
        Gui.drawModalRectWithCustomSizedTexture(x,
            y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //output
    xPrefix = 134;
    yPrefix = ContainerCrafter.SLOTY;
    rows = TileEntityCrafter.ROWS;
    cols = TileEntityCrafter.COLS;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + xPrefix - 1 + j * Const.SQ,
            this.guiTop + yPrefix - 1 + i * Const.SQ, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    //GRID GHOST SLOTS OO YEAH
    TileEntityCrafter crafter = (TileEntityCrafter) tile;
    rows = cols = 3;
    xPrefix = (getScreenSize().width() / 2 - (Const.SQ * 3) / 2);//calculate exact center
    yPrefix = ContainerCrafter.SLOTY + Const.SQ;
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        x = guiLeft + xPrefix - 1 + j * Const.SQ;
        y = guiTop + yPrefix - 1 + i * Const.SQ;
        //        Gui.drawModalRectWithCustomSizedTexture(x,
        //            y, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
        //maybe we even draw the fakey
        StackWrapper wrap = crafter.getFilter().get(i + j);
        ItemStack s = wrap == null ? null : wrap.getStack();
        int num = wrap == null ? 0 : wrap.getSize();
        //        s = new ItemStack(Blocks.STONEBRICK);
        //        num = 1;
        if (num > 0 && s.isEmpty() == false) {
          GlStateManager.pushMatrix();
          //          GlStateManager.enableBlend();
          RenderHelper.enableGUIStandardItemLighting();
          mc.getRenderItem().renderItemAndEffectIntoGUI(s, x + 1, y + 1);
          //          mc.getRenderItem().renderItemOverlayIntoGUI(fontRenderer, s, x + 1, y + 1, "1");


          GlStateManager.popMatrix();
        }
      }
    }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    //IF I CLICK OVER A crafting slot THEN
    //toggle that duder
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
