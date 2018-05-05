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

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

public class ProgressBar {

  public static final int WIDTH = 156;
  public static final int HEIGHT = 7;
  public int xOffset;
  public int yOffset;
  public int fieldId;
  public int maxValue;
  public ResourceLocation asset = Const.Res.PROGRESS;
  private GuiBaseContainer parent;

  public ProgressBar(GuiBaseContainer p, int x, int y, int f, int max) {
    parent = p;
    this.xOffset = x;
    this.yOffset = y;
    this.fieldId = f;
    this.maxValue = max;
  }

  public int getProgressCurrent() {
    //parent and tile should never be null, just dont ever add a progress bar without a TE
    return parent.tile.getField(fieldId);
  }

  public ResourceLocation getProgressCtrAsset() {
    return Const.Res.PROGRESSCTR;
  }

  public ResourceLocation getProgressAsset() {
    return asset;
  }

  public void draw() {
    int u = 0, v = 0;
    parent.mc.getTextureManager().bindTexture(this.getProgressCtrAsset());
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + xOffset,
        parent.getGuiTop() + yOffset, u, v,
        ProgressBar.WIDTH, ProgressBar.HEIGHT,
        ProgressBar.WIDTH, ProgressBar.HEIGHT);
    if (getProgressCurrent() > 0) {
      parent.mc.getTextureManager().bindTexture(getProgressAsset());
      float percent = ((float) getProgressCurrent()) / ((float) maxValue);
      Gui.drawModalRectWithCustomSizedTexture(
          parent.getGuiLeft() + xOffset,
          parent.getGuiTop() + yOffset,
          u, v,
          (int) (ProgressBar.WIDTH * percent),
          ProgressBar.HEIGHT, ProgressBar.WIDTH, ProgressBar.HEIGHT);
    }
  }
}
