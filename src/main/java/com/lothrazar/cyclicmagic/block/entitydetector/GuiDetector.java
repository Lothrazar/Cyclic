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
package com.lothrazar.cyclicmagic.block.entitydetector;

import com.lothrazar.cyclicmagic.block.entitydetector.TileEntityDetector.CompareType;
import com.lothrazar.cyclicmagic.block.entitydetector.TileEntityDetector.EntityType;
import com.lothrazar.cyclicmagic.block.entitydetector.TileEntityDetector.Fields;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.ButtonTriggerWrapper.ButtonTriggerType;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiDetector extends GuiBaseContainer {

  static final int GUI_ROWS = 2;
  private TileEntityDetector tile;
  private int leftColX;
  private int sizeY;
  private int limitColX;
  private int[] yRows = new int[3];
  private ButtonTileEntityField greaterLessBtn;
  private ButtonTileEntityField entityBtn;

  public GuiDetector(InventoryPlayer inventoryPlayer, TileEntityDetector tileEntity) {
    super(new ContainerDetector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldPreviewBtn = TileEntityDetector.Fields.RENDERPARTICLES.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 1;
    int vButtonSpacing = 12;
    sizeY = 58;//save now as reuse for textbox
    leftColX = 176 - 148;
    limitColX = leftColX + 108;
    addPatternButtonAt(id++, limitColX, sizeY - vButtonSpacing, true, Fields.LIMIT);
    ButtonTileEntityField btnDown = addPatternButtonAt(id++, limitColX, sizeY + vButtonSpacing, false, Fields.LIMIT);
    this.registerButtonDisableTrigger(btnDown, ButtonTriggerType.EQUAL, Fields.LIMIT.ordinal(), 0);
    int x = leftColX + 40;
    int y = sizeY - 5;
    this.greaterLessBtn = addPatternButtonAt(id++, x, y, true, Fields.GREATERTHAN, 60, 20);
    this.entityBtn = addPatternButtonAt(id++, x, 18, true, Fields.ENTITYTYPE, 60, 20);
    int xOffset = 18;
    int yOffset = 12;
    yRows[0] = 30 + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[0], true, Fields.RANGEX);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[0], false, Fields.RANGEX);
    yRows[1] = yRows[0] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[1], true, Fields.RANGEY);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[1], false, Fields.RANGEY);
    yRows[2] = yRows[1] + yOffset;
    addPatternButtonAt(id++, leftColX + xOffset, yRows[2], true, Fields.RANGEZ);
    addPatternButtonAt(id++, leftColX - xOffset - 4, yRows[2], false, Fields.RANGEZ);
    //TODO: PREVIEW BUTTON
  }

  private ButtonTileEntityField addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f) {
    return this.addPatternButtonAt(id, x, y, isUp, f, 15, 10);
  }

  private ButtonTileEntityField addPatternButtonAt(int id, int x, int y, boolean isUp, Fields f, int w, int h) {
    int val = (isUp) ? 1 : -1;
    ButtonTileEntityField btn = new ButtonTileEntityField(id,
        this.guiLeft + x,
        this.guiTop + y,
        tile.getPos(),
        f.ordinal(), val, w, h);
    btn.displayString = (isUp) ? "+" : "-";
    String ud = "";
    if (f != Fields.ENTITYTYPE && f != Fields.GREATERTHAN) {
      ud = (isUp) ? "up" : "down";
    }
    btn.setTooltip("tile.entity_detector." + f.name().toLowerCase() + ud);
    this.buttonList.add(btn);
    return btn;
  }

  private void drawFieldAt(int x, int y, Fields f) {
    this.drawFieldAt(x, y, f.ordinal());
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    //draw all text fields
    drawFieldAt(limitColX + 3, sizeY, Fields.LIMIT);
    drawFieldAt(leftColX, yRows[0], Fields.RANGEX);
    drawFieldAt(leftColX, yRows[1], Fields.RANGEY);
    drawFieldAt(leftColX, yRows[2], Fields.RANGEZ);
    //update button text
    EntityType t = this.tile.getEntityType();
    this.entityBtn.displayString = UtilChat.lang("tile.entity_detector." + t.name().toLowerCase());
    int greater = this.tile.getField(Fields.GREATERTHAN);
    String dir = CompareType.values()[greater].name().toLowerCase();
    this.greaterLessBtn.displayString = UtilChat.lang("tile.entity_detector." + dir);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
