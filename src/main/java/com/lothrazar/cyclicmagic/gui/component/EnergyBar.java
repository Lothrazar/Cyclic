package com.lothrazar.cyclicmagic.gui.component;

import com.lothrazar.cyclicmagic.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyBar {

  private int x = 156;
  private int y = 16;
  private int width = 10;//inner
  private int height = 60;//inner
  private int border = 1;
  private GuiBaseContainer parent;
  private boolean visible = true;

  public EnergyBar(GuiBaseContainer p, boolean alwaysVisible) {
    parent = p;
    visible = alwaysVisible;
  }

  public EnergyBar(GuiBaseContainer p, int cost) {
    parent = p;
    visible = (cost > 0);
  }

  public EnergyBar setX(int x) {
    this.x = x;
    return this;
  }

  public EnergyBar setY(int y) {
    this.y = y;
    return this;
  }

  public EnergyBar setWidth(int width) {
    this.width = width;
    return this;
  }

  public EnergyBar setHeight(int height) {
    this.height = height;
    return this;
  }

  public EnergyBar setBorder(int border) {
    this.border = border;
    return this;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    if (!visible) {
      return false;
    }
    return parent.getGuiLeft() + this.x < mouseX && mouseX < parent.getGuiLeft() + this.x + width
        && parent.getGuiTop() + this.y < mouseY && mouseY < parent.getGuiTop() + this.y + height;
  }

  public void draw(IEnergyStorage energy) {
    if (!visible) {
      return;
    }
    int u = 0, v = 0;
    float percent = ((float) energy.getEnergyStored()) / ((float) energy.getMaxEnergyStored());
    //border area 
    int outerLength = this.height + 2 * this.border;
    int outerWidth = this.width + 2 * this.border;
    //draw the outer container
    parent.mc.getTextureManager().bindTexture(Const.Res.ENERGY_CTR);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + this.x,
        parent.getGuiTop() + this.y, u, v,
        outerWidth, outerLength,
        outerWidth, outerLength);
    //draw the inner
    parent.mc.getTextureManager().bindTexture(Const.Res.ENERGY_INNER);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + this.x + this.border,
        parent.getGuiTop() + this.y + this.border, u, v,
        this.width, (int) (this.height * percent),
        this.width, this.height);
  }
}
