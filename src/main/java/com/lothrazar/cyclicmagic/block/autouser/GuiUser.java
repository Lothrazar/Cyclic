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
package com.lothrazar.cyclicmagic.block.autouser;

import com.lothrazar.cyclicmagic.block.autouser.TileEntityUser.Fields;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.Const.ScreenSize;
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

public class GuiUser extends GuiBaseContainer {

  private ButtonTileEntityField actionBtn;
  private GuiButtonToggleSize btnSize;
  private ButtonTileEntityField yOffsetBtn;

  public GuiUser(InventoryPlayer inventoryPlayer, TileEntityUser tileEntity) {
    super(new ContainerUser(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    this.fieldRedstoneBtn = Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerUser.SLOTY + 22, Fields.TIMER.ordinal(), 1);
    this.energyBar = new EnergyBar(this);
    energyBar.setHeight(50).setX(156).setY(6);
  }

  @Override
  public void initGui() {
    super.initGui();
    int btnId = 3;
    btnSize = new GuiButtonToggleSize(btnId++,
        this.guiLeft + 24 + Const.PAD,
        this.guiTop + 36, this.tile.getPos());
    this.addButton(btnSize);
    actionBtn = new ButtonTileEntityField(btnId++,
        this.guiLeft + 24 + Const.PAD,
        btnSize.y + 22, this.tile.getPos(), Fields.LEFTRIGHT.ordinal());
    actionBtn.width = 44;
    actionBtn.setTooltip("tile.block_user.action");
    this.addButton(actionBtn);
    yOffsetBtn = new ButtonTileEntityField(btnId++,
        this.guiLeft + Const.PAD / 2,
        this.guiTop + Const.PAD * 6, this.tile.getPos(), Fields.Y_OFFSET.ordinal());
    yOffsetBtn.width = Const.SQ;
    yOffsetBtn.setTooltip("tile.block_user.yoffset");
    this.addButton(yOffsetBtn);
    GuiSliderInteger sliderDelay = new GuiSliderInteger(tile, btnId++,
        this.guiLeft + 28,
        this.guiTop + 22, 122, 10, 1, TileEntityUser.MAX_SPEED,
        Fields.SPEED.ordinal());
    sliderDelay.setTooltip("tile.block_user.speed.tooltip");
    this.addButton(sliderDelay);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < 3; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 3; k < 6; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + (k + 3) * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    for (int k = 6; k < 9; k++) {
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + ContainerUser.SLOTX_START - 1 + k * Const.SQ,
          this.guiTop + ContainerUser.SLOTY - 1 - Const.SQ, u, v,
          Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    this.drawString("tile.block_user.input", 12, 82);
    this.drawString("tile.block_user.output", 122, 64);
    actionBtn.displayString = UtilChat.lang("tile.block_user.action" + tile.getField(Fields.LEFTRIGHT.ordinal()));
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(Fields.SIZE.ordinal()));
    yOffsetBtn.displayString = tile.getField(Fields.Y_OFFSET.ordinal()) + "";
    //max value is dynamic in this case
    progressBar.setMaxValue(tile.getField(Fields.SPEED.ordinal()));
  }
}
