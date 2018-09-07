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
package com.lothrazar.cyclicmagic.gui.core;

import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTexture extends GuiButtonTooltip {

  private static final ResourceLocation icon = new ResourceLocation(Const.MODID, "textures/gui/buttons.png");
  private int textureIndex = 0;
  private int textureSize = 16;

  public GuiButtonTexture(int buttonId, int x, int y, int w, int h) {
    super(buttonId, x, y, w, h, "");
    this.textureIndex = -1;
  }

  public GuiButtonTexture(int buttonId, int x, int y) {
    this(buttonId, x, y, 18, 20);
  }

  public void setTextureIndex(int i) {
    textureIndex = i;
  }

  @Override
  public void drawButton(Minecraft mc, int mouseX, int mouseY, float p) {
    if (this.visible) {
      super.drawButton(mc, mouseX, mouseY, p);
      if (this.textureIndex >= 0) {
        mc.getTextureManager().bindTexture(icon);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(this.x + 1, // +1 since button is 18 wide and texture is 16
            this.y,
            textureIndex * textureSize, 0,
            textureSize, textureSize);
      }
      this.mouseDragged(mc, mouseX, mouseY);
    }
  }
}
