package com.lothrazar.cyclicmagic.gui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

public class GuiTextFieldInteger extends GuiTextField {
  public GuiTextFieldInteger(int id, FontRenderer fontrendererObj, int x, int y, int w, int h) {
    super(id, fontrendererObj, x, y, w, h);
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
