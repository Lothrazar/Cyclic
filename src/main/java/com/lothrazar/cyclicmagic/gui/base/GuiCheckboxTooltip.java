package com.lothrazar.cyclicmagic.gui.base;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiCheckBox;

public class GuiCheckboxTooltip extends GuiCheckBox implements ITooltipButton {
  public GuiCheckboxTooltip(int buttonId, int x, int y, String buttonText, boolean ch) {
    super(buttonId, x, y, buttonText, ch);
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
