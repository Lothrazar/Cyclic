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
package com.lothrazar.cyclicmagic.component.controlledminer;

import com.lothrazar.cyclicmagic.component.controlledminer.TileEntityControlledMiner.Fields;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.data.Const.ScreenSize;
import com.lothrazar.cyclicmagic.gui.ProgressBar;
import com.lothrazar.cyclicmagic.gui.base.ContainerBaseMachine;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.button.GuiButtonToggleSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMinerSmart extends GuiBaseContainer {

  private TileEntityControlledMiner tile;
  private int xHeightTextbox = 100;
  private int yHeightTxtbox = 38;
  private ButtonTileEntityField btnHeightDown;
  private ButtonTileEntityField btnHeightUp;
  private GuiButtonToggleSize btnSize;
  private ButtonTileEntityField btnWhitelist;

  public GuiMinerSmart(InventoryPlayer inventoryPlayer, TileEntityControlledMiner tileEntity) {
    super(new ContainerMinerSmart(inventoryPlayer, tileEntity), tileEntity);
    setScreenSize(ScreenSize.LARGE);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityControlledMiner.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityControlledMiner.Fields.RENDERPARTICLES.ordinal();
    this.progressBar = new ProgressBar(this, 10, ContainerMinerSmart.SLOTY + 22, TileEntityControlledMiner.Fields.TIMER.ordinal(), TileEntityControlledMiner.TIMER_FULL);
    this.setFieldFuel(TileEntityControlledMiner.Fields.FUEL.ordinal());
  }

  @Override
  public void initGui() {
    super.initGui();
    //first the main top left type button
    int id = 2;
    int yOffset = 16;
    int bSize = 14;
    btnHeightDown = new ButtonTileEntityField(
        id++,
        this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox + yOffset,
        tile.getPos(), TileEntityControlledMiner.Fields.HEIGHT.ordinal(), -1, bSize, bSize);
    btnHeightDown.setTooltip("button.height.down");
    btnHeightDown.displayString = "-";
    this.buttonList.add(btnHeightDown);
    this.registerButtonDisableTrigger(btnHeightDown, ButtonTriggerType.EQUAL, TileEntityControlledMiner.Fields.HEIGHT.ordinal(), 1);
    btnHeightUp = new ButtonTileEntityField(
        id++, this.guiLeft + xHeightTextbox,
        this.guiTop + yHeightTxtbox - yOffset - Const.PAD / 2,
        tile.getPos(), TileEntityControlledMiner.Fields.HEIGHT.ordinal(), +1, bSize, bSize);
    btnHeightUp.setTooltip("button.height.up");
    btnHeightUp.displayString = "+";
    this.buttonList.add(btnHeightUp);
    this.registerButtonDisableTrigger(btnHeightUp, ButtonTriggerType.EQUAL, TileEntityControlledMiner.Fields.HEIGHT.ordinal(), TileEntityControlledMiner.maxHeight);
    int x = this.guiLeft + ContainerMinerSmart.SLOTX_START + 24;
    int y = this.guiTop + ContainerMinerSmart.SLOTY - 24;
    btnWhitelist = new ButtonTileEntityField(id++,
        x, y,
        tile.getPos(), TileEntityControlledMiner.Fields.LISTTYPE.ordinal(), +1, bSize, bSize);
    btnWhitelist.width = 50;
    btnWhitelist.height = 20;
    this.buttonList.add(btnWhitelist);
    x = this.guiLeft + Const.PAD * 4;
    y = this.guiTop + Const.PAD * 3 + 2;
    btnSize = new GuiButtonToggleSize(id++,
        x, y, this.tile.getPos());
    this.buttonList.add(btnSize);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < ContainerMinerSmart.SLOTID_EQUIP; k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerMinerSmart.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
    Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerMinerSmart.SLOTEQUIP_X - 1, this.guiTop + ContainerMinerSmart.SLOTEQUIP_Y - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    //    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_COAL);
    super.tryDrawFuelSlot(ContainerBaseMachine.SLOTX_FUEL - 1, ContainerBaseMachine.SLOTY_FUEL - 1);//, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    btnSize.displayString = UtilChat.lang("button.harvester.size" + tile.getField(TileEntityControlledMiner.Fields.SIZE.ordinal()));
    btnWhitelist.displayString = UtilChat.lang("button.miner.whitelist." + tile.getField(Fields.LISTTYPE.ordinal()));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    //    String s = UtilChat.lang("tile.block_miner_smart.blacklist");
    //    int x = ContainerMinerSmart.SLOTX_START - 2, 
    //    this.fontRendererObj.drawString(s, x, y, 4210752);
    int x = ContainerMinerSmart.SLOTEQUIP_X - 3;
    int y = ContainerMinerSmart.SLOTEQUIP_Y - 14;
    this.drawString("tile.block_miner_smart.tool", x, y);
    String display = "" + this.tile.getHeight();
    //move it over if more than 1 digit
    x = (display.length() > 1) ? xHeightTextbox + 2 : xHeightTextbox + 3;
    this.drawString(display, x, yHeightTxtbox);
  }
}
