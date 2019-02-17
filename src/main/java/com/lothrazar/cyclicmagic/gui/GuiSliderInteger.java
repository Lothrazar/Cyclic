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
package com.lothrazar.cyclicmagic.gui;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiSliderInteger extends GuiButtonExt implements ITooltipButton {

  private float sliderPosition = 1.0F;
  public boolean isMouseDown;
  private final int min;
  private final int max;
  private final TileEntityBaseMachineInvo responder;
  private int responderField;
  private boolean appendPlusSignLabel = true;
  private List<String> tooltip = new ArrayList<String>();
  private String tooltipOriginal;

  public GuiSliderInteger(TileEntityBaseMachineInvo guiResponder, int idIn, int x, int y,
      int widthIn, int heightIn,
      final int minIn, final int maxIn, int fieldId) {
    this(guiResponder, idIn, x, y, widthIn, heightIn, minIn, maxIn, fieldId, "");
  }

  /**
   * mimic of net.minecraft.client.gui.GuiSlider; uses integers instead of float
   * 
   * for input responder , we basically just need an IInventory & getPos()
   */
  public GuiSliderInteger(TileEntityBaseMachineInvo guiResponder, int idIn, int x, int y,
      int widthIn, int heightIn,
      final int minIn, final int maxIn, int fieldId, String tooltip) {
    super(idIn, x, y, widthIn, heightIn, "");
    this.updateDisplay();
    responder = guiResponder;
    this.min = minIn;
    this.max = maxIn;
    this.responderField = fieldId;
    appendPlusSignLabel = (getMin() < 0);//if it can be negative, we should distinguish
    this.setSliderValue(responder.getField(responderField), false);
    tooltipOriginal = tooltip;
  }

  public void setSliderValue(float value, boolean notifyResponder) {
    this.sliderPosition = (value - this.getMin()) / (this.getMax() - this.getMin());
    if (sliderPosition < 0) {
      sliderPosition = 0;
    }
    if (sliderPosition > this.getMax()) {
      sliderPosition = this.getMax();
    }
    this.updateDisplay();
    if (notifyResponder) {
      notifyResponder();
    }
  }

  private void notifyResponder() {
    int val = (int) this.getSliderValue();
    this.responder.setField(this.responderField, val);
    ModCyclic.logger.log("guiSlidInt setfield " + this.responderField + ", " + sliderPosition);
    ModCyclic.network.sendToServer(new PacketTileSetField(this.responder.getPos(), this.responderField, (int) this.getSliderValue()));
  }

  public float getSliderValue() {
    float val = this.getMin() + (this.getMax() - this.getMin()) * this.sliderPosition;
    return MathHelper.floor(val);
  }

  private void updateDisplay() {
    int val = (int) this.getSliderValue();
    if (val > 0 && appendPlusSignLabel) {
      this.displayString = "+" + val;
    }
    else {
      // zero is just "0", negavite sign is automatic
      this.displayString = "" + val;
    }
  }

  public void setTooltip(final String t) {
    tooltipOriginal = t;
    List<String> remake = new ArrayList<String>();
    remake.add(UtilChat.lang(t));
    if (this.isMouseOver())
      remake.add(TextFormatting.GRAY + UtilChat.lang("pump.secondline") + amt());
    tooltip = remake;
  }

  @Override
  public List<String> getTooltips() {
    return tooltip;
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    if (super.mousePressed(mc, mouseX, mouseY)) {
      this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
      if (this.sliderPosition < 0.0F) {
        this.sliderPosition = 0.0F;
      }
      if (this.sliderPosition > 1.0F) {
        this.sliderPosition = 1.0F;
      }
      this.updateDisplay();
      this.notifyResponder();
      this.isMouseDown = true;
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
    super.mouseDragged(mc, mouseX, mouseY);
    if (this.visible) {
      if (this.isMouseDown) {
        this.sliderPosition = (float) (mouseX - (this.x + 4)) / (float) (this.width - 8);
        if (this.sliderPosition < 0.0F) {
          this.sliderPosition = 0.0F;
        }
        if (this.sliderPosition > 1.0F) {
          this.sliderPosition = 1.0F;
        }
        this.updateDisplay();
        this.notifyResponder();
      }
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      this.drawTexturedModalRect(this.x + (int) (this.sliderPosition * (this.width - 8)), this.y, 0, 66, 4, height);
      this.drawTexturedModalRect(this.x + (int) (this.sliderPosition * (this.width - 8)) + 4, this.y, 196, 66, 4, height);
    }
  }

  @Override
  public void mouseReleased(int mouseX, int mouseY) {
    super.mouseReleased(mouseX, mouseY);
    this.isMouseDown = false;
  }

  @Override
  protected int getHoverState(boolean mouseOver) {
    return 0;
  }

  public int getMax() {
    return max;
  }

  public int getMin() {
    return min;
  }

  public int amt() {
    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
      return 5;
    }
    if (Keyboard.isKeyDown(Keyboard.KEY_RMENU) || Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
      return 25;
    }
    return 1;
  }

  public void keyTyped(char typedChar, int keyCode) {
    if (this.isMouseOver()) {
      //left is 30 or 203
      //right is 205 32
      int dir = 0;
      if (keyCode == 30 || keyCode == 203) {
        dir = -1;
      }
      else if (keyCode == 32 || keyCode == 205) {
        dir = 1;
      }
      if (dir != 0 && this.getSliderValue() + dir * this.amt() <= this.getMax()) {
        ModCyclic.logger.error("keyTyped setvalue gui slider integer ");
        this.setSliderValue(this.getSliderValue() + dir * this.amt(), true);
      }
    }
  }

  public void updateScreen() {
    this.setTooltip(tooltipOriginal);
  }
}
