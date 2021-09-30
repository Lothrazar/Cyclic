package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.net.PacketTileString;
import com.lothrazar.cyclic.registry.PacketRegistry;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TextBoxAutosave extends TextFieldWidget {

  private static final int KEY_DELETE = 261;
  private static final int KEY_BACKSPACE = 259;
  private BlockPos pos;
  private TileEntityBase tile;

  public TextBoxAutosave(FontRenderer fontIn, int xIn, int yIn, int widthIn, BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, null);
    this.setEnableBackgroundDrawing(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
    this.tile = (TileEntityBase) Minecraft.getInstance().world.getTileEntity(pos);
  }

  @Override
  protected void onFocusedChanged(boolean onFocusedChanged) {
    super.onFocusedChanged(onFocusedChanged);
    saveValue();
  }

  @Override
  public boolean charTyped(char chr, int p) {
    boolean worked = super.charTyped(chr, p);
    if (worked) {
      saveValue();
    }
    return worked;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    boolean spr = super.keyPressed(keyCode, scanCode, modifiers);
    if (keyCode == KEY_BACKSPACE || keyCode == KEY_DELETE) {
      saveValue();
    }
    return spr;
  }

  private void saveValue() {
    String current = getText();
    tile.setFieldString(tileFieldId, current);
    PacketRegistry.INSTANCE.sendToServer(new PacketTileString(this.tileFieldId, current, pos));
  }

  private int tileFieldId;
  private List<ITextComponent> tooltip;

  public List<ITextComponent> getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tt) {
    if (tooltip == null) {
      tooltip = new ArrayList<>();
    }
    this.tooltip.add(new TranslationTextComponent(tt));
  }
}
