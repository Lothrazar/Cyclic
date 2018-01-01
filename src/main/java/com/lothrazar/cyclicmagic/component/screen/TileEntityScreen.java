package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.block.base.ITileTextbox;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityScreen extends TileEntityBaseMachineInvo implements ITileTextbox {
  private String text = "";
  private int red = 100;
  private int green = 100;
  private int blue = 100;
  private int cursorPos = 0;
  private int padding = 10;
  // TODO: save both
  private Justification justif = Justification.LEFT;
  public static enum Justification {
    LEFT, CENTER, RIGHT;
  }
  public static enum Fields {
    RED, GREEN, BLUE, CURSORPOS, JUSTIFICATION, PADDING;
  }
  public TileEntityScreen() {
    super(0);
  }
  @Override
  public String getText() {
    return text;
  }
  @Override
  public void setText(String s) {
    text = s;
  }
  public int getPadding() {
    return padding;
  }
  public int getColor() {
    //TODO: fix maybe? IllegalArgumentException: Color parameter outside of expected range
    //    return new java.awt.Color(red, green, blue).getRGB();
    return (((int) red & 0xFF) << 16) | //red
        (((int) green & 0xFF) << 8) | //green
        (((int) blue & 0xFF) << 0);
  }
  @Override
  public int[] getFieldOrdinals() {
    return super.getFieldArray(Fields.values().length);
  }
  @Override
  public int getFieldCount() {
    return Fields.values().length;
  }
  @Override
  public void readFromNBT(NBTTagCompound tags) {
    super.readFromNBT(tags);
    text = tags.getString("text");
    red = tags.getInteger("red");
    green = tags.getInteger("green");
    blue = tags.getInteger("blue");
    cursorPos = tags.getInteger("cursorPos");
    padding = tags.getInteger("padding");
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString("text", text);
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
    tags.setInteger("cursorPos", cursorPos);
    tags.setInteger("padding", padding);
    return super.writeToNBT(tags);
  }
  @Override
  public int getField(int id) {
    switch (Fields.values()[id]) {
      case BLUE:
        return blue;
      case GREEN:
        return green;
      case RED:
        return red;
      case CURSORPOS:
        return cursorPos;
      case JUSTIFICATION:
        return this.justif.ordinal();
      case PADDING:
        return this.padding;
    }
    return 0;
  }
  @Override
  public void setField(int id, int value) {
    switch (Fields.values()[id]) {
      case BLUE:
        blue = value;
      break;
      case GREEN:
        green = value;
      break;
      case RED:
        red = value;
      break;
      case CURSORPOS:
        cursorPos = value;
      break;
      case JUSTIFICATION:
        int val = value % Justification.values().length;
        justif = Justification.values()[val];
      break;
      case PADDING:
        padding = value;
      break;
    }
  }
  public Justification getJustification() {
    return this.justif;
  }
}
