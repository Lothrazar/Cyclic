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
package com.lothrazar.cyclicmagic.item.storagesack;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.gui.GuiButtonTooltip;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiStorage extends GuiBaseContainer {

  private static final ResourceLocation BACKGROUND = new ResourceLocation(Const.MODID, "textures/gui/inventory_storage.png");
  static final int texture_width = 212;// 176;
  static final int texture_height = 212;
  private GuiButtonTooltip buttonToggle;

  public GuiStorage(ContainerStorage containerItem) {
    super(containerItem);
    this.xSize = texture_width;
    this.ySize = texture_height;

  }

  @Override
  public void initGui() {
    super.initGui();
    int id = 0;
    int y = this.guiTop;
    int x = this.guiLeft;
    buttonToggle = new GuiButtonTooltip(id++, x, y, 10, 10, "");
    buttonToggle.setTooltip("item.storage_bag.toggle");
    this.addButton(buttonToggle);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    if (button.id == this.buttonToggle.id) {
      // packet 
      ModCyclic.network.sendToServer(new PacketStorageBag());
    }
  }

  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    //ITem stack holder to update text
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    //    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    this.drawDefaultBackground();//dim the background as normal
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(BACKGROUND);
    int thisX = (this.width - this.xSize) / 2;
    int thisY = (this.height - this.ySize) / 2;
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, texture_width, texture_height, texture_width, texture_height);
  }

  //  @Override
  //  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
  //    super.drawScreen(mouseX, mouseY, partialTicks);
  //    this.renderHoveredToolTip(mouseX, mouseY);
  //  }
}
