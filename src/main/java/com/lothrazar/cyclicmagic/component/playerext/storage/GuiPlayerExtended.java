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
package com.lothrazar.cyclicmagic.component.playerext.storage;

import com.lothrazar.cyclicmagic.component.playerext.ButtonToggleHotbar;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.util.ResourceLocation;

public class GuiPlayerExtended extends InventoryEffectRenderer {

  //TODO: in const repo
  public static final ResourceLocation background = new ResourceLocation(Const.MODID, "textures/gui/inventory.png");
  public static final ResourceLocation armor = new ResourceLocation(Const.MODID, "textures/gui/armor.png");

  public GuiPlayerExtended(ContainerPlayerExtended ctr) {
    super(ctr);
    this.allowUserInput = true;
  }

  @Override
  public void updateScreen() {
    this.updateActivePotionEffects();
  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 7, x = this.guiLeft + 168, y = this.guiTop + Const.PAD - 1, w = Const.PAD, h = Const.SQ, row = 1;
    ButtonToggleHotbar btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    row++;
    id++;
    y += Const.SQ;
    btn = new ButtonToggleHotbar(id, x, y, w, h, row);
    this.buttonList.add(btn);
    //armor button
    id++;
    x = this.guiLeft;
    y = this.guiTop + Const.PAD;
    btn = new ButtonToggleHotbar(id, x, y, w, 4 * Const.SQ);
    this.buttonList.add(btn);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    ITooltipButton btn;
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
        btn = (ITooltipButton) buttonList.get(i);
        if (btn.getTooltips() != null) {
          drawHoveringText(btn.getTooltips(), mouseX, mouseY);
        }
        break;// cant hover on 2 at once
      }
    }
    this.renderHoveredToolTip(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    this.drawDefaultBackground();//dim the background as normal
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(background);
    int k = this.guiLeft;
    int l = this.guiTop;
    this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    //28x85
    this.mc.getTextureManager().bindTexture(armor);
    int u = 0, v = 0, w = 28, h = 85;
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft - w,
        this.guiTop, u, v,
        w, h, w, h);
  }
}
