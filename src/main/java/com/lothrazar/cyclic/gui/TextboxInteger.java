package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TextboxInteger extends TextFieldWidget implements IHasTooltip {

  private static final int KEY_DELETE = 261;
  private static final int KEY_BACKSPACE = 259;
  private BlockPos pos;
  private int tileFieldId;
  private List<ITextComponent> tooltip;

  public TextboxInteger(FontRenderer fontIn, int xIn, int yIn, int widthIn,
      BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, null);
    this.setMaxStringLength(2);
    this.setEnableBackgroundDrawing(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
  }

  @Override
  protected void onFocusedChanged(boolean p_onFocusedChanged_1_) {
    super.onFocusedChanged(p_onFocusedChanged_1_);
    saveValue();
  }

  @Override
  public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
    if (key == KEY_BACKSPACE || key == KEY_DELETE) {
      saveValue();
    }
    return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
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
  public List<ITextComponent> getTooltip() {
    return tooltip;
  }

  @Override
  public void setTooltip(String tt) {
    if (tooltip == null) {
      tooltip = new ArrayList<>();
    }
    this.tooltip.add(new TranslationTextComponent(tt));
  }

  public int getCurrent() {
    try {
      return Integer.parseInt(this.getText());
    }
    catch (Exception e) {
      return 0;
    }
  }
}
