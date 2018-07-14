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
package com.lothrazar.cyclicmagic.block.cablepump.energy;

import com.lothrazar.cyclicmagic.block.cable.TileEntityCableBase;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import net.minecraft.entity.player.InventoryPlayer;

public class GuiEnergyPump extends GuiBaseContainer {

  public GuiEnergyPump(InventoryPlayer inventoryPlayer, TileEntityEnergyPump tileEntity) {
    super(new ContainerEnergyPump(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityEnergyPump.Fields.REDSTONE.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int width = 164;
    int h = 20;
    int x = this.guiLeft + 6;
    int y = this.guiTop + 28;
    //not more than the cable can handle
    GuiSliderInteger sliderDelay = new GuiSliderInteger(tile, id++, x, y, width, h, 1, TileEntityCableBase.TRANSFER_ENERGY_PER_TICK,
        TileEntityEnergyPump.Fields.TRANSFER_RATE.ordinal());
    sliderDelay.setTooltip("pump.rate");
    this.addButton(sliderDelay);
  }
}
