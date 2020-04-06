package com.lothrazar.cyclic.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class Textbox extends TextFieldWidget {

  public Textbox(FontRenderer fontIn, int xIn, int yIn, int widthIn, int heightIn, String msg) {
    super(fontIn, xIn, yIn, widthIn, heightIn, msg);
  }

  @Override
  public boolean charTyped(char chr, int p) {
    if (!Character.isDigit(chr)) {
      return false;
    }
    return super.charTyped(chr, p);
  }

  private int maxVal;
  private int minVal;
  private int tileFieldId;

  public int getMaxVal() {
    return maxVal;
  }

  public void setMaxVal(int maxVal) {
    this.maxVal = maxVal;
  }

  public int getMinVal() {
    return minVal;
  }

  public void setMinVal(int minVal) {
    this.minVal = minVal;
  }

  public int getTileFieldId() {
    return tileFieldId;
  }

  public void setTileFieldId(int tileFieldId) {
    this.tileFieldId = tileFieldId;
  }

  public int getCurrent() {
    try {
      return Integer.parseInt(this.getText());
    }
    catch (Exception e) {
      return 0;
    }
  }
}
