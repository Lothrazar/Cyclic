package com.lothrazar.cyclicmagic.component.enderbook;
import net.minecraft.client.gui.GuiButton;

public class ButtonWaypointDelete extends GuiButton {
  private int bookSlot;
  public int getSlot() {
    return bookSlot;
  }
  public ButtonWaypointDelete(int id, int x, int y, int w, int h, String txt, int slot) {
    super(id, x, y, w, h, txt);
    bookSlot = slot;
  }
  private String tooltip = null;
  public void setTooltip(String s) {
    tooltip = s;
  }
  public String getTooltip() {
    return tooltip;
  }
}
