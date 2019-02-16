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
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenTargetBlock extends GuiBaseContainer {

  TileEntityScreenTarget screen;
  private GuiSliderInteger sliderR;
  private GuiSliderInteger sliderG;
  private GuiSliderInteger sliderB;
  private GuiSliderInteger sliderPadding;
  private GuiSliderInteger sliderPaddingY;

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
    int width = 144;
    //    int xCenter = (xSize / 2 - width / 2);
    int x = 26;
    int y = Const.PAD / 2;
    int h = 12;
    id++;
    x = guiLeft + 26;
    y = this.guiTop + 23 + Const.PAD;
    sliderR = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.RED.ordinal());
    sliderR.setTooltip("screen.red");
    this.addButton(sliderR);
    id++;
    y += h + 1;
    sliderG = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.GREEN.ordinal());
    sliderG.setTooltip("screen.green");
    this.addButton(sliderG);
    id++;
    y += h + 1;
    sliderB = new GuiSliderInteger(tile, id, x, y, width, h, 0, 255, Fields.BLUE.ordinal());
    sliderB.setTooltip("screen.blue");
    this.addButton(sliderB);
    id++;
    y += h + 1;
    sliderPadding = new GuiSliderInteger(tile, id, x, y, width, h, 0, 60, Fields.PADDINGX.ordinal());
    sliderPadding.setTooltip("screen.padding");
    this.addButton(sliderPadding);
    id++;
    y += h + 1;
    sliderPaddingY = new GuiSliderInteger(tile, id, x, y, width, h, 0, 60, Fields.PADDINGY.ordinal());
    sliderPaddingY.setTooltip("screen.paddingy");
    this.addButton(sliderPaddingY);
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    this.mc.getTextureManager().bindTexture(Const.Res.SLOT_GPS);
    int x = this.guiLeft + 8;
    int y = this.guiTop + 86;
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
    if (sliderPadding != null)
      sliderPadding.updateScreen();
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    sliderR.keyTyped(typedChar, keyCode);
    sliderG.keyTyped(typedChar, keyCode);
    sliderB.keyTyped(typedChar, keyCode);
    sliderPadding.keyTyped(typedChar, keyCode);
    //  if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
    super.keyTyped(typedChar, keyCode);
    //   }
  }
  // ok end of textbox fixing stuff
}
