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
package com.lothrazar.cyclicmagic.block.fan;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.container.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiFan extends GuiBaseContainer {

  private TileEntityFan tile;
  boolean debugLabels = false;
  private ButtonTileEntityField btnTogglePush;
  private GuiSliderInteger sliderRange;
  private GuiSliderInteger sliderSpeed;
  private ButtonTileEntityField btnSound;

  public GuiFan(InventoryPlayer inventoryPlayer, TileEntityFan tileEntity) {
    super(new ContainerFan(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    this.fieldRedstoneBtn = TileEntityFan.Fields.REDSTONE.ordinal();
    this.fieldPreviewBtn = TileEntityFan.Fields.PARTICLES.ordinal();
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int id = 1;
    int w = 18, h = 18;
    int x = this.guiLeft + 4;
    int y = this.guiTop + 48;
    // silent or sound
    btnSound = new ButtonTileEntityField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.SILENT.ordinal(), +1, w, h);
    this.addButton(btnSound);
    btnSound.setTooltip("button.fan.sound.tooltip");
    h = 10;
    x = this.guiLeft + 30;
    y = this.guiTop + 22;
    int field = TileEntityFan.Fields.RANGE.ordinal();
    sliderRange = new GuiSliderInteger(tile, id++, x, y, 130, h, 1, TileEntityFan.MAX_RANGE,
        field);
    sliderRange.setTooltip("button.fan.range.tooltip");
    this.addButton(sliderRange);
    //    
    ///////////////// SPEED BUTTONS
    y += 18;
    field = TileEntityFan.Fields.SPEED.ordinal();
    sliderSpeed = new GuiSliderInteger(tile, id++, x, y, 130, h, 1, TileEntityFan.MAX_SPEED,
        field);
    sliderSpeed.setTooltip("button.fan.speed.tooltip");
    this.addButton(sliderSpeed);
    //the big push pull toggle button
    w = 70;
    h = 20;
    x = this.guiLeft + 50;
    y = this.guiTop + 58;
    btnTogglePush = new ButtonTileEntityField(id++, x, y, tile.getPos(),
        TileEntityFan.Fields.PUSHPULL.ordinal(), +1, w, h);
    this.addButton(btnTogglePush);
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    super.keyTyped(typedChar, keyCode);
    sliderRange.keyTyped(typedChar, keyCode);
    sliderSpeed.keyTyped(typedChar, keyCode);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    sliderRange.updateScreen();
    sliderSpeed.updateScreen();
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT);
    for (int k = 0; k < this.tile.getSizeInventory(); k++) {
      Gui.drawModalRectWithCustomSizedTexture(this.guiLeft + ContainerFan.SLOTX_START - 1 + k * Const.SQ, this.guiTop + ContainerFan.SLOTY - 1, u, v, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    int fld = TileEntityFan.Fields.SILENT.ordinal();
    btnSound.setTextureIndex(14 + this.tile.getField(fld));
    fld = TileEntityFan.Fields.PUSHPULL.ordinal();
    btnTogglePush.displayString = UtilChat.lang("button.fan.pushpull" + tile.getField(fld));
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }
}
