package com.lothrazar.cyclicmagic.component.pylonexp;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;

public class ButtonExpPylon extends GuiButtonTooltip {
  private int value = 0;
  public ButtonExpPylon(int buttonId, int x, int y, int w, int h, String s) {
    super(buttonId, x, y, w, h, s);
  }
  public int getValue() {
    return value;
  }
  public void setValue(int value) {
    this.value = value;
  }
}
