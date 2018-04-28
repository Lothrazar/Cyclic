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
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.block.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiSliderInteger extends GuiButtonExt implements ITooltipButton {

  private float sliderPosition = 1.0F;
  public boolean isMouseDown;
  private final int min;
  private final int max;
  private final TileEntityBaseMachineInvo responder;
  private int responderField;
  private boolean appendPlusSignLabel = true;

  /**
   * mimic of net.minecraft.client.gui.GuiSlider; uses integers instead of float
   * 
   * for input responder , we basically just need an IInventory & getPos()
   */
  public GuiSliderInteger(TileEntityBaseMachineInvo guiResponder, int idIn, int x, int y,
      int widthIn, int heightIn,
      final int minIn, final int maxIn, int fieldId, boolean plusLabels) {
    super(idIn, x, y, widthIn, heightIn, "");
    this.updateDisplay();
    responder = guiResponder;
    this.min = minIn;
    this.max = maxIn;
    this.responderField = fieldId;
    appendPlusSignLabel = plusLabels;
    this.setSliderValue(responder.getField(responderField), false);
  }

  public void setSliderValue(float value, boolean notifyResponder) {
    this.sliderPosition = (value - this.min) / (this.max - this.min);
    this.updateDisplay();
    if (notifyResponder) {
      notifyResponder();
    }
  }

  private void notifyResponder() {
    int val = (int) this.getSliderValue();
    this.responder.setField(this.responderField, val);
    ModCyclic.network.sendToServer(new PacketTileSetField(this.responder.getPos(), this.responderField, val));
  }

  public float getSliderValue() {
    float val = this.min + (this.max - this.min) * this.sliderPosition;
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
    List<String> remake = new ArrayList<String>();
    remake.add(UtilChat.lang(t));
    tooltip = remake;
  }

  private List<String> tooltip = new ArrayList<String>();

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
}
