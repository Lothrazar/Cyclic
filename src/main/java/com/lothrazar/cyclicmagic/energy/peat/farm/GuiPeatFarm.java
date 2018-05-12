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
package com.lothrazar.cyclicmagic.energy.peat.farm;

import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.FluidBar;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiPeatFarm extends GuiBaseContainer {

  public GuiPeatFarm(InventoryPlayer inventoryPlayer, TileEntityPeatFarm te) {
    super(new ContainerPeatFarm(inventoryPlayer, te), te);
    this.fieldRedstoneBtn = TileEntityPeatFarm.Fields.REDSTONE.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setX(152);
    this.fluidBar = new FluidBar(this, 120, 16);
    fluidBar.setCapacity(TileEntityPeatFarm.TANK_FULL);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int rowSize = 6;
    for (int i = 0; i < rowSize; i++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerPeatFarm.SLOTX_START + i * Const.SQ - 1,
          this.guiTop + ContainerPeatFarm.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int i = rowSize; i < 2 * rowSize; i++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerPeatFarm.SLOTX_START + (i - rowSize) * Const.SQ - 1,
          this.guiTop + ContainerPeatFarm.SLOTY - 1 + Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    fluidBar.draw(
        ((TileEntityBaseMachineFluid) tile).getCurrentFluidStackAmount(),
        Const.Res.FLUID_WATER);
  }
}
