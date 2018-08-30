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
package com.lothrazar.cyclicmagic.gui.button;

import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.core.GuiButtonTexture;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiButtonTogglePreview extends GuiButtonTexture {

  private BlockPos pos;
  private int fieldId;

  public GuiButtonTogglePreview(int buttonId, int x, int y, BlockPos p, int field) {
    super(buttonId, x, y);
    this.pos = p;
    fieldId = field;
    this.setTextureIndex(3);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      ModCyclic.network.sendToServer(new PacketTileIncrementField(pos, fieldId, 1));
    }
    return pressed;
  }

  public void setStateOn() {
    this.setTextureIndex(3);
    this.setTooltips(Arrays.asList(UtilChat.lang("tile.preview.button.on")));
  }

  public void setStateOff() {
    this.setTextureIndex(4);
    this.setTooltips(Arrays.asList(UtilChat.lang("tile.preview.button.off")));
  }
}
