package com.lothrazar.cyclic.gui;

import java.util.List;
import net.minecraft.util.text.ITextComponent;

public interface IHasTooltip {

  public List<ITextComponent> getTooltip();

  public void setTooltip(String tt);
}
