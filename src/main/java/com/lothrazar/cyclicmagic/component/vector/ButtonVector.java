package com.lothrazar.cyclicmagic.component.vector;
import com.lothrazar.cyclicmagic.gui.GuiButtonTooltip;
import net.minecraft.util.math.BlockPos;

public class ButtonVector extends GuiButtonTooltip {
  private int fieldId;
  private int value;
  public ButtonVector(BlockPos current, int buttonId, int x, int y, int val, int t) {
    super(buttonId, x, y, 20, 20, "");
    setTooltip("tile.plate_vector.tooltip.button");
    setFieldId(t);
    setValue(val);
  }
  public int getFieldId() {
    return fieldId;
  }
  public void setFieldId(int fieldId) {
    this.fieldId = fieldId;
  }
  public int getValue() {
    return value;
  }
  public void setValue(int value) {
    this.value = value;
  }
}
