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
package com.lothrazar.cyclicmagic.block.screentype;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.screentype.TileEntityScreen.Fields;
import com.lothrazar.cyclicmagic.data.ITileTextbox;
import com.lothrazar.cyclicmagic.gui.GuiSliderInteger;
import com.lothrazar.cyclicmagic.gui.button.ButtonTileEntityField;
import com.lothrazar.cyclicmagic.gui.core.GuiBaseContainer;
import com.lothrazar.cyclicmagic.net.PacketTileTextbox;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiScreenBlock extends GuiBaseContainer {

  private List<GuiTextField> lines = new ArrayList<>();
  TileEntityScreen screen;
  private ButtonTileEntityField btnToggle;
  private GuiSliderInteger sliderR;
  private GuiSliderInteger sliderG;
  private GuiSliderInteger sliderB;
  private GuiSliderInteger sliderPadding;

  public GuiScreenBlock(InventoryPlayer inventoryPlayer, TileEntityScreen tileEntity) {
    super(new ContainerScreen(inventoryPlayer, tileEntity), tileEntity);
    screen = tileEntity;
    setScreenSize(ScreenSize.STANDARDPLAIN);
  }

  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int id = 0;
    int h = 18;
    int width = 144;
    //    int xCenter = (xSize / 2 - width / 2);
    int x = 26;
    int y = Const.PAD / 2;
    for (int i = 0; i < 4; i++) {
      GuiTextField txtInput = new GuiTextField(id, fontRenderer, x, y, ScreenTESR.SCREEN_WIDTH, h);// new GuiTextField(id, this.fontRenderer, x, y, ScreenTESR.SCREEN_WIDTH, 60);
      //    txtInput.setMaxStringLength(1230);
      txtInput.setText(screen.getText(i));
      txtInput.setFocused(i == 0);
      lines.add(txtInput);
      y += h + Const.PAD / 4;
      id++;
    }
    //    txtInput.setCursorPosition(tile.getField(Fields.CURSORPOS.ordinal()));
    // hmm multi lines are better? 
    h = 12;
    x = guiLeft + 26;
    y = this.guiTop + y;
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
    sliderPadding = new GuiSliderInteger(tile, id, x, y, width, h, 0, 60, Fields.PADDING.ordinal());
    sliderPadding.setTooltip("screen.padding");
    this.addButton(sliderPadding);
    //text box of course
    id++;
    btnToggle = new ButtonTileEntityField(id++,
        this.guiLeft + 4,
        this.guiTop + Const.PAD / 2, this.tile.getPos(), Fields.JUSTIFICATION.ordinal(), 1);
    btnToggle.setTooltip("screen.justification");
    btnToggle.width = 20;
    this.addButton(btnToggle);
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
    for (GuiTextField txtInput : lines) {
      //      tile.setField(Fields.CURSORPOS.ordinal(), this.txtInput.getCursorPosition());
      //      ModCyclic.network.sendToServer(new PacketTileSetField(tile.getPos(), Fields.CURSORPOS.ordinal(), this.txtInput.getCursorPosition()));
    }
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    for (GuiTextField txtInput : lines) {
      txtInput.drawTextBox();
      txtInput.setTextColor(screen.getColor());
    }
    btnToggle.setTextureIndex(8 + tile.getField(Fields.JUSTIFICATION.ordinal()));
  }

  // http://www.minecraftforge.net/forum/index.php?topic=22378.0
  // below is all the stuff that makes the text box NOT broken
  @Override
  public void updateScreen() {
    super.updateScreen();
    for (GuiTextField txtInput : lines) {
      txtInput.updateCursorCounter();
    }
    sliderR.updateScreen();
    sliderG.updateScreen();
    sliderB.updateScreen();
    sliderPadding.updateScreen();
  }

  @Override
  protected void keyTyped(char typedChar, int keyCode) throws IOException {
    sliderR.keyTyped(typedChar, keyCode);
    sliderG.keyTyped(typedChar, keyCode);
    sliderB.keyTyped(typedChar, keyCode);
    sliderPadding.keyTyped(typedChar, keyCode);
    if (this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode) == false) {
      super.keyTyped(typedChar, keyCode);
    }
    if (keyCode == 15) {// TAB  
      int lastFocus = 0;
      for (GuiTextField txtInput : lines) {
        if (txtInput.isFocused()) {
          lastFocus = txtInput.getId();
          txtInput.setFocused(false);
        }
      }
      lastFocus = (lastFocus > 2) ? 0 : lastFocus + 1;
      lines.get(lastFocus).setFocused(true);
    }
    for (GuiTextField txtInput : lines)
      if (txtInput.isFocused()) {
        txtInput.textboxKeyTyped(typedChar, keyCode);
        ((ITileTextbox) tile).setText(txtInput.getId(), txtInput.getText());
        ModCyclic.network.sendToServer(new PacketTileTextbox(txtInput.getText(), tile.getPos(), txtInput.getId()));
      }
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int btn) throws IOException {
    super.mouseClicked(mouseX, mouseY, btn);// x/y pos is 33/30
    for (GuiTextField txtInput : lines) {

      txtInput.mouseClicked(mouseX, mouseY, btn);

      boolean isMouseover = mouseX >= txtInput.x + guiLeft && mouseX < txtInput.x + guiLeft + txtInput.width
          && mouseY >= txtInput.y + guiTop && mouseY < txtInput.y + txtInput.height + guiTop;
      txtInput.setFocused(isMouseover);
    }
  }
  // ok end of textbox fixing stuff
}
