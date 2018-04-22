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
package com.lothrazar.cyclicmagic.block.vector;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.vector.TileEntityVector.Fields;
import com.lothrazar.cyclicmagic.gui.GuiTextFieldInteger;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.data.Const.ScreenSize;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiVector extends GuiBaseContainer {

  private static final int SOUTH = 0;
  private static final int NORTH = 180;
  private static final int EAST = 270;
  private static final int WEST = 90;
  private TileEntityVector tile;
  private int xAngle = 14;
  private int yAngle = 56;//aka pitch
  private int xPower = 14;
  private int yPower = 98;
  private int xYaw = 116;
  private int yYaw = 60;
  private ButtonVector soundBtn;
  private GuiTextFieldInteger txtYaw;
  private GuiTextFieldInteger txtAngle;

  public GuiVector(InventoryPlayer inventoryPlayer, TileEntityVector tileEntity) {
    super(new ContainerVector(inventoryPlayer, tileEntity), tileEntity);
    tile = tileEntity;
    screenSize = ScreenSize.STANDARDPLAIN;
    this.fieldRedstoneBtn = TileEntityVector.Fields.REDSTONE.ordinal();
  }

  //  public String getTitle() {
  //    return "tile.plate_vector.name";
  //  }
  @Override
  public void initGui() {
    super.initGui();
    Keyboard.enableRepeatEvents(true);
    int id = 2;
    //angle text box
    txtAngle = addTextbox(id++, xAngle, yAngle, tile.getAngle() + "", 2);
    txtAngle.setFocused(true);//default
    txtAngle.setMaxVal(TileEntityVector.MAX_ANGLE);
    txtAngle.setMinVal(0);
    txtAngle.setTileFieldId(TileEntityVector.Fields.ANGLE.ordinal());
    //then the power text box
    GuiTextFieldInteger txtPower = addTextbox(id++, xPower, yPower, tile.getPower() + "", 3);
    txtPower.setMaxVal(TileEntityVector.MAX_POWER);
    txtPower.setMinVal(1);
    txtPower.setTileFieldId(TileEntityVector.Fields.POWER.ordinal());
    // yaw text box
    txtYaw = addTextbox(id++, xYaw, yYaw, tile.getYaw() + "", 3);
    txtYaw.setMaxVal(TileEntityVector.MAX_YAW);
    txtYaw.setMinVal(0);
    txtYaw.setTileFieldId(TileEntityVector.Fields.YAW.ordinal());
    //now the YAW buttons
    int btnYawSpacing = 22;
    addButtonAt(id++, xYaw + 5, yYaw + btnYawSpacing, SOUTH, Fields.YAW.ordinal()).displayString = "S";
    addButtonAt(id++, xYaw + 5, yYaw - btnYawSpacing, NORTH, Fields.YAW.ordinal()).displayString = "N";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw, EAST, Fields.YAW.ordinal()).displayString = "E";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw, WEST, Fields.YAW.ordinal()).displayString = "W";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw - btnYawSpacing, (NORTH + EAST) / 2, Fields.YAW.ordinal()).displayString = "NE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw - btnYawSpacing, (NORTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "NW";
    addButtonAt(id++, xYaw + btnYawSpacing + 10, yYaw + btnYawSpacing, (360 + EAST) / 2, Fields.YAW.ordinal()).displayString = "SE";
    addButtonAt(id++, xYaw - btnYawSpacing, yYaw + btnYawSpacing, (SOUTH + WEST) / 2, Fields.YAW.ordinal()).displayString = "SW";
    soundBtn = addButtonAt(id++, 130, 110, 0, Fields.SOUND.ordinal());
    soundBtn.setWidth(38);
    //angle buttons
    addButtonAt(id++, xAngle, yAngle - btnYawSpacing, 90, Fields.ANGLE.ordinal()).displayString = "^";
    addButtonAt(id++, xAngle + 24, yAngle - btnYawSpacing, 45, Fields.ANGLE.ordinal()).displayString = "/";
    addButtonAt(id++, xAngle + 24, yAngle, 0, Fields.ANGLE.ordinal()).displayString = "->";
  }

  @Override
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }

  private ButtonVector addButtonAt(int id, int x, int y, int val, int f) {
    ButtonVector btn = new ButtonVector(tile.getPos(), id,
        this.guiLeft + x,
        this.guiTop + y,
        val, f);
    this.buttonList.add(btn);
    return btn;
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);
    if (button instanceof ButtonVector) {
      ButtonVector btn = (ButtonVector) button;
      if (btn.getFieldId() == Fields.SOUND.ordinal()) {//workaround so we can use the same button for sound as for others
        int newVal = (this.tile.getField(Fields.SOUND.ordinal()) + 1) % 2;
        ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), newVal, Fields.SOUND.ordinal()));
      }
      else {
        for (GuiTextField t : txtBoxes) { //push value to the matching textbox
          GuiTextFieldInteger txt = (GuiTextFieldInteger) t;
          if (txt.getTileFieldId() == btn.getFieldId()) {
            txt.setText(btn.getValue() + "");
          }
        }
      }
    }
  }

  private GuiTextFieldInteger addTextbox(int id, int x, int y, String text, int maxLen) {
    int width = 10 * maxLen, height = 20;
    GuiTextFieldInteger txt = new GuiTextFieldInteger(id, this.fontRenderer, x, y, width, height);
    txt.setMaxStringLength(maxLen);
    txt.setText(text);
    txtBoxes.add(txt);
    return txt;
  }

  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    for (GuiButton btn : this.buttonList) {
      if (btn instanceof ButtonVector) {
        ButtonVector btnv = (ButtonVector) btn;
        if (btnv.getFieldId() == Fields.YAW.ordinal()) {
          btnv.enabled = !txtYaw.getText().equals(btnv.getValue() + "");//tile.getYaw();
        }
        else if (btnv.getFieldId() == Fields.ANGLE.ordinal()) {
          btnv.enabled = !txtAngle.getText().equals(btnv.getValue() + "");
        }
      }
    }
    soundBtn.displayString = UtilChat.lang("tile.plate_vector.gui.sound" + tile.getField(Fields.SOUND.ordinal()));
    renderString("tile.plate_vector.gui.power", xPower + 14, yPower + 26);
    renderString("tile.plate_vector.gui.angle", xAngle + 18, yAngle + 26);
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  private void renderString(String s, int x, int y) {
    String str = UtilChat.lang(s);
    int strWidth = this.fontRenderer.getStringWidth(str);
    this.fontRenderer.drawString(str, x - strWidth / 2, y, 4210752);
  }

  @Override
  public void updateScreen() { // http://www.minecraftforge.net/forum/index.php?topic=22378.0
    super.updateScreen();
    for (GuiTextField txt : txtBoxes) {
      if (txt != null) {
        txt.updateCursorCounter();
      }
    }
  }

  @Override
  protected void keyTyped(char pchar, int keyCode) throws IOException {
    super.keyTyped(pchar, keyCode);
    for (GuiTextField t : txtBoxes) {
      GuiTextFieldInteger txt = (GuiTextFieldInteger) t;
      String oldval = txt.getText();
      txt.textboxKeyTyped(pchar, keyCode);
      String newval = txt.getText();
      boolean yes = false;
      try {
        int val = Integer.parseInt(newval);
        if (val <= txt.getMaxVal() && val >= txt.getMinVal()) {
          yes = true;
          //also set it clientisde to hopefully prevent desycn
          tile.setField(txt.getTileFieldId(), val);
          ModCyclic.network.sendToServer(new PacketTileVector(tile.getPos(), val, txt.getTileFieldId()));
        }
      }
      catch (NumberFormatException e) {}
      if (!yes && !newval.isEmpty()) {//allow empty string in case user is in middle of deleting all and retyping
        txt.setText(oldval);//rollback to the last valid value. ex if they type 'abc' revert to valid 
      }
    }
  }
}
