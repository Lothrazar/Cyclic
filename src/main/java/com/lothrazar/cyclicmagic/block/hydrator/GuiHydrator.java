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
package com.lothrazar.cyclicmagic.block.hydrator;

import com.lothrazar.cyclicmagic.block.hydrator.TileEntityHydrator.Fields;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.FluidBar;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHydrator extends GuiBaseContainer {

  boolean debugLabels = true;
  private ButtonTileEntityField btnToggle;

  public GuiHydrator(InventoryPlayer inventoryPlayer, TileEntityHydrator tileEntity) {
    super(new ContainerHydrator(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileEntityHydrator.Fields.REDSTONE.ordinal();
    this.energyBar = new EnergyBar(this);
    energyBar.setX(70).setY(16).setWidth(14);
    this.fluidBar = new FluidBar(this, 98, 16);
    fluidBar.setCapacity(TileEntityHydrator.TANK_FULL);
  }

  @Override
  public void initGui() {
    super.initGui();
    int btnId = 3;
    btnToggle = new ButtonTileEntityField(btnId++,
        this.guiLeft + 26,
        this.guiTop + Const.PAD / 2, this.tile.getPos(), Fields.RECIPELOCKED.ordinal());
    btnToggle.width = btnToggle.height = 20;
    this.addButton(btnToggle);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (tile.getField(Fields.RECIPELOCKED.ordinal()) == 1) {
      btnToggle.setTextureIndex(5);
      btnToggle.setTooltip("tile.hydrator.locked.tooltip");
    }
    else {
      btnToggle.setTextureIndex(6);
      btnToggle.setTooltip("tile.hydrator.unlocked.tooltip");
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int x = this.guiLeft + ContainerHydrator.SLOTX_START - 1;
    int y = this.guiTop + ContainerHydrator.SLOTY - 1;
    int s = TileEntityHydrator.RECIPE_SIZE;
    for (int k = 0; k < s; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          x + k % 2 * Const.SQ,
          y + k / 2 * Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    x = this.guiLeft + ContainerHydrator.MID_SPACING;
    y = this.guiTop + ContainerHydrator.SLOTY - 1;
    for (int k = 0; k < s; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          x + k % 2 * Const.SQ,
          y + k / 2 * Const.SQ,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    fluidBar.draw(((TileEntityBaseMachineFluid) tile).getCurrentFluidStack());
  }
}
