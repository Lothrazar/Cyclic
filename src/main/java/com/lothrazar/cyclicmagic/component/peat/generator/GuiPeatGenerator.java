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
package com.lothrazar.cyclicmagic.component.peat.generator;

import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class GuiPeatGenerator extends GuiBaseContainer {

  public GuiPeatGenerator(InventoryPlayer inventoryPlayer, TileEntityPeatGenerator te) {
    super(new ContainerPeatGenerator(inventoryPlayer, te), te);
    this.progressBar = new ProgressBar(this, 10, 72, TileEntityPeatGenerator.Fields.TIMER.ordinal(), TileEntityPeatGenerator.TIMER_FULL);
  }

  @Override
  public void initGui() {
    super.initGui();
    // BUTTONS! 
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    Gui.drawModalRectWithCustomSizedTexture(
        this.width / 2 - 9,
        this.guiTop + 34 - 1,
        u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    //DRAW ENERGY BAR
    //    ITileFuel tileFuel = (ITileFuel) this.tile;
    //    int u = 0, v = 0;
    IEnergyStorage energy = tile.getCapability(CapabilityEnergy.ENERGY, EnumFacing.UP);
    float percent = ((float) energy.getEnergyStored()) / ((float) energy.getMaxEnergyStored());
    int outerLength = 66, outerWidth = 16;
    int innerLength = 64, innerWidth = 14;
    int fuelX = this.guiLeft + screenSize.width() - 25;
    int fuelXE = fuelX + innerWidth;
    int fuelY = this.guiTop + Const.PAD;
    int fuelYE = fuelY + innerLength;
    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_CTR);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + screenSize.width() - 26,
        this.guiTop + 4, u, v,
        outerWidth, outerLength,
        outerWidth, outerLength);
    this.mc.getTextureManager().bindTexture(Const.Res.ENERGY_INNER);
    Gui.drawModalRectWithCustomSizedTexture(
        fuelX,
        fuelY - 3, u, v,
        innerWidth, (int) (innerLength * percent),
        innerWidth, innerLength);
  }
}
