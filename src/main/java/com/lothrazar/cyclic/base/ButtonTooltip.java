package com.lothrazar.cyclic.base;

import net.minecraftforge.fml.client.config.GuiButtonExt;

public class ButtonTooltip extends GuiButtonExt {

  private String tooltip;

  public ButtonTooltip(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
    super(xPos, yPos, width, height, displayString, handler);
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
}
