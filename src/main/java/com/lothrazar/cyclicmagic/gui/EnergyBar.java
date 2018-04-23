package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;

public class EnergyBar {

  private boolean renderOutside = true;
  protected int fuelX, fuelY, fuelXE, fuelYE;
  private GuiBaseContainer parent;
  private EnergyRenderType type;
  enum EnergyRenderType {
    OUTER_VERTICAL, OUTER_HORIZ, INNER_VERTICAL;
  }

  public EnergyBar(GuiBaseContainer p) {
    parent = p;
    type = EnergyRenderType.OUTER_VERTICAL;
  }

  public boolean doesRenderOutside() {
    return renderOutside;
  }

  public void renderOutside() {
    this.renderOutside = true;
  }

  public void renderInside() {
    this.renderOutside = false;
  }

  public void setSize(int fuelX, int fuelY, int fuelXE, int fuelYE) {
    this.fuelX = fuelX;
    this.fuelY = fuelY;
    this.fuelXE = fuelXE;
    this.fuelYE = fuelYE;
  }

  //  public ResourceLocation getOuterTexture() {}
  //
  //  public ResourceLocation getInnerTexture() {
  //    //outer vertical
  ////    Const.Res.FUEL_CTRVERT
  //    
  //  }
}
