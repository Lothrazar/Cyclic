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
package com.lothrazar.cyclicmagic.block.screen;

import com.lothrazar.cyclicmagic.block.core.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.data.ITileTextbox;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityScreen extends TileEntityBaseMachineInvo implements ITileTextbox {

  private String text = "";
  private int red = 100;
  private int green = 100;
  private int blue = 100;
  private int cursorPos = 0;
  private int padding = 0;
  private Justification justif = Justification.LEFT;

  public static enum Justification {
    LEFT, CENTER, RIGHT;
  }

  public static enum Fields {
    RED, GREEN, BLUE, CURSORPOS, JUSTIFICATION, PADDING;
  }

  public TileEntityScreen() {
    super(0);
  }

  @Override
  public String getText() {
    return text;
  }

  @Override
  public void setText(String s) {
    text = s;
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
    text = tags.getString("text");
    red = tags.getInteger("red");
    green = tags.getInteger("green");
    blue = tags.getInteger("blue");
    cursorPos = tags.getInteger("cursorPos");
    padding = tags.getInteger("padding");
    int just = tags.getInteger("justif");
    if (just < Justification.values().length) {
      this.justif = Justification.values()[just];
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString("text", text);
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("cursorPos", cursorPos);
    tags.setInteger("padding", padding);
    tags.setInteger("justif", justif.ordinal());
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
      case CURSORPOS:
        return cursorPos;
      case JUSTIFICATION:
        return this.justif.ordinal();
      case PADDING:
        return this.padding;
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
      case CURSORPOS:
        cursorPos = value;
      break;
      case JUSTIFICATION:
        int val = value % Justification.values().length;
        justif = Justification.values()[val];
      break;
      case PADDING:
        padding = value;
      break;
    }
  }

  public Justification getJustification() {
    return this.justif;
  }
}
