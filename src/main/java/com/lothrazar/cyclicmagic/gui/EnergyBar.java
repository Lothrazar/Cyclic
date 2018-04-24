package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;

public class EnergyBar {

  public boolean isOutsideContainer = true;
  public int posX;
  public int posY;
  public int width;
  public int height;
  public boolean isVertical;
  private GuiBaseContainer parent;

  public EnergyBar(GuiBaseContainer p) {
    parent = p;
    //    type = EnergyRenderType.OUTER_VERTICAL;
  }

  //public ResourceLocation get

}
