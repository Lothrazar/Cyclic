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
package com.lothrazar.cyclicmagic.block.fluiddrain;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.component.EnergyBar;
import com.lothrazar.cyclicmagic.gui.component.FluidBar;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDrain extends GuiBaseContainer {

  boolean debugLabels = true;

  public GuiDrain(InventoryPlayer inventoryPlayer, TileEntityFluidDrain tileEntity) {
    super(new ContainerDrain(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityFluidDrain.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityFluidDrain.Fields.RENDERPARTICLES.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setX(152).setY(18).setWidth(14);
    this.fluidBar = new FluidBar(this, 126, 18);
    fluidBar.setCapacity(TileEntityFluidDrain.TANK_FULL);
  }

  @Override
  public void initGui() {
    super.initGui();
    this.previewBtn.x = this.guiLeft + Const.PAD / 2 + 20;
    this.previewBtn.y = this.guiTop + Const.PAD / 2;
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int u = 0, v = 0, x, y, rows = 3, cols = 6;
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        x = this.guiLeft + 7 + j * Const.SQ;
        y = this.guiTop + 27 + i * Const.SQ;
        Gui.drawModalRectWithCustomSizedTexture(
            x,
            y,
            u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
      }
    }
    fluidBar.draw(((TileEntityBaseMachineFluid) tile).getCurrentFluidStack());
  }
}
