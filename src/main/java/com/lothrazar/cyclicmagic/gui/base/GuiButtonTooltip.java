package com.lothrazar.cyclicmagic.gui.base;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public abstract class GuiButtonTooltip extends GuiButtonExt implements ITooltipButton {
  public GuiButtonTooltip(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
    super(buttonId, x, y, widthIn, heightIn, buttonText);
  }
  private List<String> tooltip = new ArrayList<String>();
  @Override
  public List<String> getTooltips() {
    return tooltip;
  }
  public void setTooltips(List<String> t) {
    tooltip = t;
  }
  public void setTooltip(final String t) {
    List<String> remake = new ArrayList<String>();
    remake.add(UtilChat.lang(t));
    tooltip = remake;
  }
}
