package com.lothrazar.cyclicmagic.core.gui;

import java.util.Map;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.gui.button.ButtonCheckboxTileField;
import net.minecraft.util.EnumFacing;

public class CheckboxFacingComponent {

  private GuiBaseContainer parent;
  private int xCenter;
  private int yCenter;
  
  public CheckboxFacingComponent(GuiBaseContainer parent) {
    this.parent = parent;
  }

  //each side needs a TE field to save data
  private Map<EnumFacing, Integer> facingFields;

  public Map<EnumFacing, Integer> getFacingFields() {
    return facingFields;
  }

  public void setX(int x) {
    xCenter = x;
  }

  public void setY(int y) {
    yCenter = y;
  }

  public void setFacingFields(Map<EnumFacing, Integer> facingFields) {
    this.facingFields = facingFields;
  }

  public void initGui() {
    for (EnumFacing f : EnumFacing.values()) {
      addButtonFacing(f);
    }
  }

  private void addButtonFacing(EnumFacing side) {
    int x = 0, y = 0;

    int spacing = 14;
    int field = 0;
    //MAP side -> field
    field = facingFields.get(side);
    switch (side) {
      case EAST:
        x = xCenter + spacing;
        y = yCenter;
      break;
      case NORTH:
        x = xCenter;
        y = yCenter - spacing;
      break;
      case SOUTH:
        x = xCenter;
        y = yCenter + spacing;
      break;
      case WEST:
        x = xCenter - spacing;
        y = yCenter;
      break;
      case UP:
        x = xCenter + spacing;
        y = yCenter + Const.PAD * 4;
      break;
      case DOWN:
        x = xCenter - spacing;
        y = yCenter + Const.PAD * 4;
      break;
    }
    int w = 18, h = 15;
    ButtonCheckboxTileField btn = new ButtonCheckboxTileField(field + 30,
        parent.getGuiLeft() + x,
        parent.getGuiTop() + y, parent.tile.getPos(), field, w, h);
    //    tile.setField(field, (tile.getField(field) + 1) % 2);
    btn.setIsChecked(parent.tile.getField(field) == 1);
    //    btn.setIsChecked(tileClock.getSideHasPower(side));
    btn.setTooltip("tile.clock.facing." + side.name().toLowerCase());
    parent.insertButton(btn);
  }
}
