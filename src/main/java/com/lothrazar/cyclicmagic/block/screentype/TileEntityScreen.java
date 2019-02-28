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
package com.lothrazar.cyclicmagic.block.screentype;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.ITileTextbox;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityScreen extends TileEntityBaseMachineInvo implements ITileTextbox {

  private String[] text = new String[4];
  private int red = 100;
  private int green = 100;
  private int blue = 100;
  private int padding = 0;
  private int font = 0;

  public static enum Fields {
    RED, GREEN, BLUE, PADDING, FONT;
  }

  public TileEntityScreen() {
    super(0);
  }

  @Override
  public String getText() {
    return text[0];
  }

  @Override
  public void setText(String s) {
    text[0] = s;
  }

  @Override
  public String getText(int i) {
    return text[i];
  }

  @Override
  public void setText(int i, String s) {
    text[i] = s;
  }

  public int getPadding() {
    return padding;
  }

  public int getColor() {
    //TODO: fix maybe? IllegalArgumentException: Color parameter outside of expected range
    //    return new java.awt.Color(red, green, blue).getRGB();
    return ((red & 0xFF) << 16) | //red
        ((green & 0xFF) << 8) | //green
        ((blue & 0xFF) << 0);
  }

  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }

  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }

  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    text = new String[4];
    for (int i = 0; i < 4; i++) {
      text[i] = tags.getString("text" + i);
    }
    red = tags.getInteger("red");
    green = tags.getInteger("green");
    blue = tags.getInteger("blue");
    padding = tags.getInteger("padding");
    font = tags.getInteger("font");
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    for (int i = 0; i < 4; i++) {
      if (text[i] != null) {
        tags.setString("text" + i, text[i]);
      }
    }
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("padding", padding);
    tags.setInteger("font", font);
    return super.writeToNBT(tags);
  }

  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case BLUE:
        return blue;
      case GREEN:
        return green;
      case RED:
        return red;
      case PADDING:
        return this.padding;
      case FONT:
        return this.font;
    }
    return 0;
  }

  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case BLUE:
        blue = value;
      break;
      case GREEN:
        green = value;
      break;
      case RED:
        red = value;
      break;
      case PADDING:
        padding = value;
      break;
      case FONT:
        font = value;
      break;
    }
  }
}
