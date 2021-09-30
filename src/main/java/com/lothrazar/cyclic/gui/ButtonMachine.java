package com.lothrazar.cyclic.gui;
public class ButtonMachine extends ButtonTextured {

  /**
   * TODO: this is rarely used.
   * 
   * make a constructor that takes this instead of IPressable so that not every screen has to send its own manual packet?
   * 
   * if we do this refactor, take the pos back from redstone button too
   */
  private int tileField;

  public ButtonMachine(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
    super(xPos, yPos, width, height, displayString, handler);
  }

  public ButtonMachine(int xPos, int yPos, int width, int height, TextureEnum texture, int field, IPressable handler) {
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
