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
package com.lothrazar.cyclicmagic.core.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class GuiButtonTooltip extends GuiButtonExt implements ITooltipButton {

  private boolean allowPressedIfDisabled = false;

  public GuiButtonTooltip(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
    super(buttonId, x, y, widthIn, heightIn, buttonText);
  }

  private List<String> tooltip = new ArrayList<String>();

  @Override
  public List<String> getTooltips() {
    return tooltip;
  }

  public void setTooltips(List<String> t) {
    tooltip = t;
  }

  public GuiButtonTooltip setTooltip(final String t) {
    List<String> remake = new ArrayList<String>();
    remake.add(UtilChat.lang(t));
    tooltip = remake;
    return this;
  }

  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    if (isAllowPressedIfDisabled())
      return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    return super.mousePressed(mc, mouseX, mouseY);
  }

  public boolean isAllowPressedIfDisabled() {
    return allowPressedIfDisabled;
  }

  public GuiButtonTooltip allowPressedIfDisabled() {
    this.allowPressedIfDisabled = true;
    return this;
  }
}
