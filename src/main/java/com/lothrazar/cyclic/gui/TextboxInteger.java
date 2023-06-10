package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.api.IHasTooltip;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

public class TextboxInteger extends EditBox implements IHasTooltip {

  private static final int KEY_DELETE = 261;
  private static final int KEY_BACKSPACE = 259;
  private BlockPos pos;
  private int tileFieldId;
  private List<Component> tooltip;

  public TextboxInteger(Font fontIn, int xIn, int yIn, int widthIn,
      BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, null);
    this.setMaxLength(2);
    this.setBordered(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
  }

  @Override
  public void setFocused(boolean onFocusedChanged) {
    super.setFocused(onFocusedChanged);
    saveValue();
  }

  @Override
  public boolean keyPressed(int key, int mx, int my) {
    if (key == KEY_BACKSPACE || key == KEY_DELETE) {
      saveValue();
    }
    return super.keyPressed(key, mx, my);
  }

  private void saveValue() {
    PacketRegistry.INSTANCE.sendToServer(new PacketTileData(this.tileFieldId, this.getCurrent(), pos));
  }

  @Override
  public boolean charTyped(char chr, int p) {
    if (!Character.isDigit(chr)) {
      return false;
    }
    boolean worked = super.charTyped(chr, p);
    if (worked) {
      saveValue();
    }
    return worked;
  }

  @Override
  public List<Component> getTooltip() {
    return tooltip;
  }

  @Override
  public void setTooltip(String tt) {
    tooltip = new ArrayList<>();
    addTooltip(tt);
  }

  @Override
  public void addTooltip(String ttIn) {
    tooltip.add(Component.translatable(ttIn));
  }

  public int getCurrent() {
    try {
      return Integer.parseInt(this.getValue());
    }
    catch (Exception e) {
      return 0;
    }
  }
}
