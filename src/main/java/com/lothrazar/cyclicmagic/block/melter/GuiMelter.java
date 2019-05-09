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
package com.lothrazar.cyclicmagic.block.melter;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineFluid;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.FluidBar;
import com.lothrazar.cyclicmagic.gui.component.ProgressBar;
import com.lothrazar.cyclicmagic.gui.container.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMelter extends GuiBaseContainer {

  private ButtonTileEntityField btnToggle;

  public GuiMelter(InventoryPlayer inventoryPlayer, TileMelter tileEntity) {
    super(new ContainerMelter(inventoryPlayer, tileEntity), tileEntity);
    this.fieldRedstoneBtn = TileMelter.Fields.REDSTONE.ordinal();
    this.fluidBar = new FluidBar(this, 150, 16);
    this.fluidBar.setCapacity(TileMelter.TANK_FULL);
    this.progressBar = new ProgressBar(this, 8, 70, TileMelter.Fields.TIMER.ordinal(), TileMelter.TIMER_FULL);
    this.progressBar.setWidth(136);
  }

  @Override
  public void initGui() {
    super.initGui();
    int btnId = 3;
    btnToggle = new ButtonTileEntityField(btnId++,
        this.guiLeft + 26,
        this.guiTop + Const.PAD / 2, this.tile.getPos(), TileMelter.Fields.RECIPELOCKED.ordinal());
    btnToggle.width = btnToggle.height = 20;
    this.addButton(btnToggle);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (tile.getField(TileMelter.Fields.RECIPELOCKED.ordinal()) == 1) {
      btnToggle.setTextureIndex(5);
      btnToggle.setTooltip("tile.hydrator.locked.tooltip");
    }
    else {
      btnToggle.setTextureIndex(6);
      btnToggle.setTooltip("tile.hydrator.unlocked.tooltip");
    }
    int heat = tile.getField(TileMelter.Fields.HEATLEVEL.ordinal());
    this.drawString(UtilChat.lang("tile.heat.level") + heat, 86, 30);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    int x = this.guiLeft + ContainerMelter.SLOTX_START - 1;
    int y = this.guiTop + ContainerMelter.SLOTY - 1;
    int s = TileMelter.RECIPE_SIZE;
    int spac = Const.SQ;
    for (int k = 0; k < s; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          x + k % 2 * spac,
          y + k / 2 * spac,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    fluidBar.draw(((TileEntityBaseMachineFluid) tile).getCurrentFluidStack());
  }
}
