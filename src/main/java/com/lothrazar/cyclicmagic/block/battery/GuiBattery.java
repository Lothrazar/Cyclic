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
package com.lothrazar.cyclicmagic.block.battery;

import java.util.HashMap;
import java.util.Map;
import com.lothrazar.cyclicmagic.block.battery.TileEntityBattery.Fields;
import com.lothrazar.cyclicmagic.core.gui.CheckboxFacingComponent;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;

public class GuiBattery extends GuiBaseContainer {

  CheckboxFacingComponent checkboxes;
  public GuiBattery(InventoryPlayer inventoryPlayer, TileEntityBattery te) {
    super(new ContainerBattery(inventoryPlayer, te), te);
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(16).setY(8).setX(150);
    checkboxes = new CheckboxFacingComponent(this);
    checkboxes.setX(40);
    checkboxes.setY(20);
    Map<EnumFacing, Integer> facingFields = new HashMap<EnumFacing, Integer>();
    for (EnumFacing side : EnumFacing.values()) {
      switch (side) {
        case DOWN:
          facingFields.put(side, Fields.D.ordinal());
        break;
        case EAST:
          facingFields.put(side, Fields.E.ordinal());
        break;
        case NORTH:
          facingFields.put(side, Fields.N.ordinal());
        break;
        case SOUTH:
          facingFields.put(side, Fields.S.ordinal());
        break;
        case UP:
          facingFields.put(side, Fields.U.ordinal());
        break;
        case WEST:
          facingFields.put(side, Fields.W.ordinal());
        break;
      }
    }
    checkboxes.setFacingFields(facingFields);
  }

  @Override
  public void initGui() {
    super.initGui();
    checkboxes.initGui();
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

  }
}
