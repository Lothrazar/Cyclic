package com.lothrazar.cyclicmagic.gui.villager;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;

public class ButtonVillagerPurchase extends GuiButton implements ITooltipButton {
  private final List<String> tooltips = new ArrayList<String>();
  public ButtonVillagerPurchase(int buttonId, int x, int y) {
    super(buttonId, x, y, 20, 20, UtilChat.lang("tile.tool_trade.button.name"));
    tooltips.add(UtilChat.lang("tile.tool_trade.button.tooltip"));
  }
  @Override
  public List<String> getTooltips() {
    return tooltips;
  }
}
