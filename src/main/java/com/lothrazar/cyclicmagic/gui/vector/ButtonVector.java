package com.lothrazar.cyclicmagic.gui.vector;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.gui.ITooltipButton;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.math.BlockPos;

public class ButtonVector extends GuiButton implements ITooltipButton {

  private final List<String> tooltips = new ArrayList<String>();
  private int fieldId;
  private int value;
  public ButtonVector(BlockPos current, int buttonId, int x, int y,int val, int t) {
    super(buttonId, x, y, 20, 20, "");
    tooltips.add(UtilChat.lang("tile.plate_vector.tooltip.button"));
    setFieldId(t);
    setValue(val);
 }
  @Override
  public List<String> getTooltips() {
    return tooltips;
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
