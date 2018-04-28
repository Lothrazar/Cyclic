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
package com.lothrazar.cyclicmagic.block.controlledminer;

import com.lothrazar.cyclicmagic.block.controlledminer.TileEntityControlledMiner.Fields;
import com.lothrazar.cyclicmagic.core.ITileStackWrapper;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.StackWrapper;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.EnergyBar;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {

  private GuiButtonToggleSize btnSize;
  private ButtonTileEntityField btnWhitelist;
  ITileStackWrapper te;

  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity), tileEntity);
    te = tileEntity;
    this.fieldRedstoneBtn = TileEntityControlledMiner.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityControlledMiner.Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, 72, TileEntityControlledMiner.Fields.TIMER.ordinal(), TileEntityControlledMiner.TIMER_FULL);
    this.energyBar = new EnergyBar(this);
    energyBar.setHeight(50).setY(12);
  }

  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int id = 2, x, y;

    btnWhitelist = new ButtonTileEntityField(id++,
        guiLeft + 4, guiTop + Const.PAD + 40,
        tile.getPos(), TileEntityControlledMiner.Fields.LISTTYPE.ordinal(), +1);
    btnWhitelist.width = 18;
    this.buttonList.add(btnWhitelist);
    x = this.guiLeft + Const.PAD * 4;
    y = this.guiTop + Const.PAD * 3 + 2;
    btnSize = new GuiButtonToggleSize(id++,
        x, y, this.tile.getPos());
    this.buttonList.add(btnSize);

    x = this.guiLeft + 38;
    y = this.guiTop + 15;
    GuiSliderInteger sliderDelay = new GuiSliderInteger(tile, id++, x, y, 100, 10, 1, TileEntityControlledMiner.maxHeight,
        TileEntityControlledMiner.Fields.HEIGHT.ordinal(), true);
    sliderDelay.setTooltip("button.miner.height");
    this.addButton(sliderDelay);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0, x, y;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_LARGE);
    //tool slot
    int size = 26;
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTEQUIP_X - 5, this.guiTop + ContainerMinerSmart.SLOTEQUIP_Y - 5, u, v, size, size, size, size);
    for (int slotNum = 0; slotNum < te.getWrapperCount(); slotNum++) {
      x = this.guiLeft + 25 + slotNum * Const.SQ;
      y = this.guiTop + 50;
      StackWrapper wrap = te.getStackWrapper(slotNum);
      wrap.setX(x);
      wrap.setY(y);
    }
    this.renderStackWrappers(te);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityControlledMiner.Fields.SIZE.ordinal()));
    int filterType = tile.getField(Fields.LISTTYPE.ordinal());
    btnWhitelist.setTooltip(UtilChat.lang("button.miner.whitelist." + filterType));
    btnWhitelist.setTextureIndex(11 + filterType);

  }
}
