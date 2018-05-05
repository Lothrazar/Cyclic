package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;

public class EnergyBar {

  //  public boolean isOutsideContainer = true;
  private int x = 156;
  private int y = 16;
  private int width = 10;//inner
  private int height = 60;//inner
  private int border = 1;
  private GuiBaseContainer parent;

  public EnergyBar(GuiBaseContainer p) {
    parent = p;
  }

  public int getX() {
    return x;
  }

  public EnergyBar setX(int x) {
    this.x = x;
    return this;
  }

  public int getY() {
    return y;
  }

  public EnergyBar setY(int y) {
    this.y = y;
    return this;
  }

  public int getWidth() {
    return width;
  }

  public EnergyBar setWidth(int width) {
    this.width = width;
    return this;
  }

  public int getHeight() {
    return height;
  }

  public EnergyBar setHeight(int height) {
    this.height = height;
    return this;
  }

  public int getBorder() {
    return border;
  }

  public EnergyBar setBorder(int border) {
    this.border = border;
    return this;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return parent.getGuiLeft() + getX() < mouseX && mouseX < parent.getGuiLeft() + getX() + getWidth()
        && parent.getGuiTop() + getY() < mouseY && mouseY < parent.getGuiTop() + getY() + getHeight();
  }

}
