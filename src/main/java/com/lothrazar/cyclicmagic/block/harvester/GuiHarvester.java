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
package com.lothrazar.cyclicmagic.block.harvester;

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.core.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiHarvester extends GuiBaseContainer {

  boolean debugLabels = false;
  private ButtonTileEntityField btnSize;
  private GuiButtonTooltip btnSpray;

  public GuiHarvester(InventoryPlayer inventoryPlayer, TileEntityHarvester tileEntity) {
    super(new ContainerHarvester(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = TileEntityHarvester.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityHarvester.Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10,
        ContainerHarvester.SLOTY + 3 * Const.SQ + 4, TileEntityHarvester.Fields.TIMER.ordinal(), TileEntityHarvester.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
    energyBar.setWidth(10).setHeight(42).setX(156).setY(8);
  }

  @Override
  public void initGui() {
    super.initGui();
    int btnId = 20;
    int x = this.guiLeft + Const.PAD + 22;
    int y = this.guiTop + Const.PAD * 3 + 2;
    btnSize = new ButtonTileEntityField(btnId++,
        x, y, this.tile.getPos(), TileEntityHarvester.Fields.SIZE.ordinal());
    btnSize.width = 44;
    btnSize.setTooltip("button.size.tooltip");
    this.addButton(btnSize);
    int w = 58, h = 20;
    x += 40 + Const.PAD;
    btnSpray = new GuiButtonTooltip(btnId++,
        x, y,
        w, h, "");
    btnSpray.setTooltip("button.harvester.mode.tooltip");
    this.addButton(btnSpray);
  }

  @Override
  protected void actionPerformed(GuiButton button) {
    if (button.id == btnSpray.id) {
      ModCyclic.network.sendToServer(new PacketTileIncrementField(tile.getPos(), TileEntityHarvester.Fields.HARVESTMODE.ordinal()));
    }
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < 9; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerHarvester.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerHarvester.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 9; k < 18; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerHarvester.SLOTX_START - 1 + (k - 9) * Const.SQ,
          this.guiTop + Const.SQ + ContainerHarvester.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 18; k < 27; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerHarvester.SLOTX_START - 1 + (k - 18) * Const.SQ,
          this.guiTop + 2 * Const.SQ + ContainerHarvester.SLOTY - 1,
          u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    } //, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityHarvester.Fields.SIZE.ordinal()));
    btnSpray.displayString = UtilChat.lang("button.harvester.mode" + tile.getField(TileEntityHarvester.Fields.HARVESTMODE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
