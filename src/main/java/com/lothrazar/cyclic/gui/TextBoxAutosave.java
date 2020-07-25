package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.net.PacketTileString;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;

public class TextBoxAutosave extends TextFieldWidget {

  private BlockPos pos;
  private TileEntityBase tile;

  public TextBoxAutosave(FontRenderer fontIn, int xIn, int yIn, int widthIn,
      BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, null);
    this.setMaxStringLength(2);
    this.setEnableBackgroundDrawing(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
    this.tile = (TileEntityBase) ModCyclic.proxy.getClientWorld().getTileEntity(pos);
  }

  @Override
  protected void onFocusedChanged(boolean p_onFocusedChanged_1_) {
    super.onFocusedChanged(p_onFocusedChanged_1_);
    saveValue();
  }

  @Override
  public boolean keyPressed(int key, int p_keyPressed_2_, int p_keyPressed_3_) {
    saveValue();
    return super.keyPressed(key, p_keyPressed_2_, p_keyPressed_3_);
  }

  private void saveValue() {
    tile.setFieldString(tileFieldId, getText());
    PacketRegistry.INSTANCE.sendToServer(new PacketTileString(this.tileFieldId, this.getText(), pos));
  }

  private int tileFieldId;
  private String tooltip;

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
}
