package com.lothrazar.cyclicmagic.component.screen;
import com.lothrazar.cyclicmagic.block.base.ITileTextbox;
import com.lothrazar.cyclicmagic.block.base.TileEntityBaseMachineInvo;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityScreen extends TileEntityBaseMachineInvo implements ITileTextbox {
  private String text = "write a book and put it in your pocket and try to split based";
  private int red = 0;
  private int green = 0;
  private int blue = 0;
  public static enum Fields {
    RED, GREEN, BLUE;
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
  }
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound tags) {
    tags.setString(text, "text");
    tags.setInteger("red", red);
    tags.setInteger("green", green);
    tags.setInteger("blue", blue);
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
    }
  }
}
