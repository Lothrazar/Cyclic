package com.lothrazar.cyclicmagic.gui.waypoints;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;

public class ButtonWaypointNew extends GuiButton {
  private int bookSlot;
  public int getSlot() {
    return bookSlot;
  }
  public ButtonWaypointNew(int id, int x, int y, int w, int h, int slot) {
    super(id, x, y, w, h, UtilChat.lang("gui.enderbook.new"));
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
