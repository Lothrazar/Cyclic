package com.lothrazar.cyclicmagic.component.pylonexp;
import com.lothrazar.cyclicmagic.gui.base.GuiButtonTooltip;
import net.minecraft.util.math.BlockPos;

public class ButtonExpPylon extends GuiButtonTooltip {
  //private int fieldId;
  private int value = 0;
  public ButtonExpPylon( int buttonId, int x, int y,int w,int h,String s) {
    super(buttonId, x, y, w,h, s);
  //  setTooltip("tile.plate_vector.tooltip.button");
  }
//  public int getFieldId() {
//    return fieldId;
//  }
//  public void setFieldId(int fieldId) {
//    this.fieldId = fieldId;
//  }
  public int getValue() {
    return value;
  }
  public void setValue(int value) {
    this.value = value;
  }
}
