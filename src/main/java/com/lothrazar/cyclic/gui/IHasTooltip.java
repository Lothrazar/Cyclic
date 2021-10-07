package com.lothrazar.cyclic.gui;

import java.util.List;
import net.minecraft.network.chat.Component;

public interface IHasTooltip {

  public List<Component> getTooltip();

  public void setTooltip(String tt);
}
