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
package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class GuiSliderInteger extends ExtendedButton {

  private float sliderPosition = 1.0F;
  public boolean isMouseDown;
  private final int min;
  private final int max;
  private final TileEntityBase responder;
  private int responderField;
  private boolean appendPlusSignLabel = true;
  private List<String> tooltip = new ArrayList<String>();
  private String tooltipOriginal;

  public GuiSliderInteger(TileEntityBase guiResponder, int x, int y,
      int widthIn, int heightIn,
      final int minIn, final int maxIn, int fieldId) {
    this(guiResponder, x, y, widthIn, heightIn, minIn, maxIn, fieldId, "");
  }

  /**
   * mimic of net.minecraft.client.gui.GuiSlider; uses integers instead of float
   * 
   * for input responder , we basically just need an IInventory & getPos()
   */
  public GuiSliderInteger(TileEntityBase guiResponder, int x, int y,
      int widthIn, int heightIn,
      final int minIn, final int maxIn, int fieldId, String tooltip) {
    super(x, y, widthIn, heightIn, "", (p) -> {
      //pressed
    });
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
    PacketRegistry.INSTANCE.sendToServer(
        new PacketTileData(this.responderField, (int) this.getSliderValue(), this.responder.getPos()));
  }

  public float getSliderValue() {
    float val = this.getMin() + (this.getMax() - this.getMin()) * this.sliderPosition;
    return MathHelper.floor(val);
  }

  private void updateDisplay() {
    int val = (int) this.getSliderValue();
    if (val > 0 && appendPlusSignLabel) {
      this.setMessage("+" + val);
    }
    else {
      // zero is just "0", negavite sign is automatic
      this.setMessage("" + val);
    }
  }
  //  public void setTooltip(final String t) {
  //    tooltipOriginal = t;
  //    List<String> remake = new ArrayList<String>();
  //    remake.add(UtilChat.lang(t)); 
  ////    if (this.isMouseOver())
  ////      remake.add(TextFormatting.GRAY + UtilChat.lang("pump.secondline") + amt());
  //    tooltip = remake;
  //  }
  //
  //  @Override
  //  public List<String> getTooltips() {
  //    return tooltip;
  //  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int b) {
    if (super.mouseClicked(mouseX, mouseY, b)) {
      this.sliderPosition = (float) (mouseX - (this.x + 4)) / (this.width - 8);
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
  public boolean mouseDragged(double mouseX, double mouseY, int btn, double p_mouseDragged_6_, double p_mouseDragged_8_) {
    boolean og = super.mouseDragged(mouseX, mouseY, btn, p_mouseDragged_6_, p_mouseDragged_8_);
    if (this.visible) {
      if (this.isMouseDown) {
        this.sliderPosition = (float) (mouseX - (this.x + 4)) / (this.width - 8);
        if (this.sliderPosition < 0.0F) {
          this.sliderPosition = 0.0F;
        }
        if (this.sliderPosition > 1.0F) {
          this.sliderPosition = 1.0F;
        }
        this.updateDisplay();
        this.notifyResponder();
      }
      //      GlStateManager.colorf(1.0F, 1.0F, 1.0F, 1.0F);
      this.blit(this.x + (int) (this.sliderPosition * (this.width - 8)), this.y, 0, 66, 4, height);
      this.blit(this.x + (int) (this.sliderPosition * (this.width - 8)) + 4, this.y, 196, 66, 4, height);
      return true;
    }
    return og;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int b) {
    this.isMouseDown = false;
    return super.mouseReleased(mouseX, mouseY, b);
  }
  //  @Override
  //  protected int getHoverState(boolean mouseOver) {
  //    return 0;
  //  }

  public int getMax() {
    return max;
  }

  public int getMin() {
    return min;
  }

  public int amt() {
    //    if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
    //      return 5;
    //    }
    //    if (Keyboard.isKeyDown(Keyboard.KEY_RMENU) || Keyboard.isKeyDown(Keyboard.KEY_LMENU)) {
    //      return 25;
    //    }
    return 1;
  }
  //  public void keyTyped(char typedChar, int keyCode) {
  ////    this.isMouseOver(p_isMouseOver_1_, p_isMouseOver_3_)
  //    if (this.isMouseOver()) {
  //      //left is 30 or 203
  //      //right is 205 32
  //      int dir = 0;
  //      if (keyCode == 30 || keyCode == 203) {
  //        dir = -1;
  //      }
  //      else if (keyCode == 32 || keyCode == 205) {
  //        dir = 1;
  //      }
  //      if (dir != 0 && this.getSliderValue() + dir * this.amt() <= this.getMax()) {
  //        this.setSliderValue(this.getSliderValue() + dir * this.amt(), true);
  //      }
  //    }
  //  }
  //
  //  public void updateScreen() {
  //    this.setTooltip(tooltipOriginal);
  //  }
}
