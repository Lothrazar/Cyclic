package com.lothrazar.cyclic.gui;

import net.minecraft.client.gui.components.Button.OnPress;
public class ButtonMachine extends ButtonTextured {

  private int tileField;

  public ButtonMachine(int xPos, int yPos, int width, int height, String displayString, OnPress handler) {
    super(xPos, yPos, width, height, displayString, handler);
  }

  public ButtonMachine(int xPos, int yPos, int width, int height, TextureEnum texture, int field, OnPress handler) {
    super(xPos, yPos, width, height, "", handler);
    this.tileField = field;
    this.setTextureId(texture);
  }

  public int getTileField() {
    return tileField;
  }

  public void setTileField(int tileField) {
    this.tileField = tileField;
  }
}
