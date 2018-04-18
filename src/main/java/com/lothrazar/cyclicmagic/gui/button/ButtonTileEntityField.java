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

import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTexture;
import com.lothrazar.cyclicmagic.net.PacketTileIncrementField;
import com.lothrazar.cyclicmagic.net.PacketTileSetField;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Re-used by many tile entities used to increment fields using packets
 * 
 * @author Sam
 *
 */
public class ButtonTileEntityField extends GuiButtonTexture {

  private BlockPos pos;
  private int field;
  private int value;

  public static enum ButtonMode {
    INCREMENT, SET;
  }

  public ButtonMode buttonMode = ButtonMode.INCREMENT;

  public ButtonTileEntityField(int buttonId, int x, int y, BlockPos p, int fld) {
    this(buttonId, x, y, p, fld, 1);
  }

  public ButtonTileEntityField(int buttonId, int x, int y, BlockPos p, int fld, int diff) {
    this(buttonId, x, y, p, fld, diff, 40, 20);
  }

  public ButtonTileEntityField(int buttonId, int x, int y, BlockPos p,
      int fld, int diff,
      int w, int h) {
    super(buttonId, x, y, w, h);
    this.pos = p;
    field = fld;
    this.value = diff;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
    boolean pressed = super.mousePressed(mc, mouseX, mouseY);
    if (pressed) {
      switch (this.buttonMode) {
        case INCREMENT:
          ModCyclic.network.sendToServer(new PacketTileIncrementField(pos, field, value));
        break;
        case SET:
          ModCyclic.network.sendToServer(new PacketTileSetField(pos, field, value));
        break;
      }
    }
    return pressed;
  }

  public int getFieldId() {
    return field;
  }

  public int getValue() {
    return value;
  }
}
