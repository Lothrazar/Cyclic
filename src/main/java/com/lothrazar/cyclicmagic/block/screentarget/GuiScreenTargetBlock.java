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
package com.lothrazar.cyclicmagic.block.screentarget;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.block.screentarget.TileEntityScreenTarget.Fields;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.component.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.container.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenTargetBlock extends GuiBaseContainer {

  TileEntityScreenTarget screen;
  private GuiSliderInteger sliderR;
  private GuiSliderInteger sliderG;
  private GuiSliderInteger sliderB;
  private ButtonTileEntityField btnStyle;
  private ButtonTileEntityField btnToggle;
  private GuiSliderInteger sliderFont;
  private GuiSliderInteger sliderX;
  private GuiSliderInteger sliderY;

  public GuiScreenTargetBlock(InventoryPlayer inventoryPlayer, TileEntityScreenTarget tileEntity) {
    super(new ContainerScreenTarget(inventoryPlayer, tileEntity), tileEntity);
    screen = tileEntity;
    setScreenSize(ScreenSize.STANDARDPLAIN);
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int id = 1;
    int x = guiLeft + Const.PAD;
    int y = guiTop + 16;
    int h = 12;
    btnToggle = new ButtonTileEntityField(id++,
        x, y,
        this.tile.getPos(), Fields.SHOWTYPE.ordinal());
    btnToggle.width = 50;
    btnToggle.setTooltip("button.screen.type");
    this.addButton(btnToggle);
    x += btnToggle.width + 2;
    btnStyle = new ButtonTileEntityField(id++,
        x, y, tile.getPos(), Fields.STYLE.ordinal());
    btnStyle.setTooltip("screen." + Fields.STYLE.toString().toLowerCase());
    this.addButton(btnStyle);
    x = guiLeft + Const.PAD;
    int width = 160;
    h = 12;
    y += btnToggle.height + 4;
    sliderR = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, Fields.RED.ordinal());
    sliderR.setTooltip("screen.red");
    this.addButton(sliderR);
    y += h + 2;
    sliderG = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, Fields.GREEN.ordinal());
    sliderG.setTooltip("screen.green");
    this.addButton(sliderG);
    y += h + 2;
    sliderB = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 255, Fields.BLUE.ordinal());
    sliderB.setTooltip("screen.blue");
    this.addButton(sliderB);
    y += h + 2;
    sliderX = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 100, Fields.XPADDING.ordinal());
    sliderX.setTooltip("screen.x");
    this.addButton(sliderX);
    y += h + 2;
    sliderY = new GuiSliderInteger(tile, id++, x, y, width, h, 0, 100, Fields.YPADDING.ordinal());
    sliderY.setTooltip("screen.y");
    this.addButton(sliderY);
    y += h + 1;
    sliderFont = new GuiSliderInteger(tile, id++, x, y, width, h, 10, 110, Fields.FONT.ordinal());
    sliderFont.setTooltip("screen.fontsize");
    this.addButton(sliderFont);
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    btnToggle.displayString = UtilChat.lang("button.screen." + screen.showType().toString().toLowerCase());
    btnStyle.displayString = UtilChat.lang("screen.style" + screen.getField(Fields.STYLE.ordinal()));
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    int x = this.guiLeft + 150;
    int y = this.guiTop + 16;
    Gui.drawModalRectWithCustomSizedTexture(
        x, y,
        0, 0, Const.SQ, Const.SQ, Const.SQ, Const.SQ);
  }

  @Override
  public void updateScreen() {
    super.updateScreen();
    sliderR.updateScreen();
    sliderG.updateScreen();
    sliderB.updateScreen();
    sliderFont.updateScreen();
    sliderX.updateScreen();
    sliderY.updateScreen();
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    sliderR.keyTyped(typedChar, keyCode);
    sliderG.keyTyped(typedChar, keyCode);
    sliderB.keyTyped(typedChar, keyCode);
    sliderFont.keyTyped(typedChar, keyCode);
    sliderX.keyTyped(typedChar, keyCode);
    sliderY.keyTyped(typedChar, keyCode);
    //  if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
    super.keyTyped(typedChar, keyCode);
    //   }
  }
  // ok end of textbox fixing stuff
}
