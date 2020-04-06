package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.net.PacketTileData;
import com.lothrazar.cyclic.registry.PacketRegistry;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.math.BlockPos;

public class TextboxInteger extends TextFieldWidget {

  private BlockPos pos;

  public TextboxInteger(FontRenderer fontIn, int xIn, int yIn, int widthIn,
      BlockPos pos, int field) {
    super(fontIn, xIn, yIn, widthIn, 16, "");
    this.setMaxStringLength(2);
    this.setEnableBackgroundDrawing(true);
    this.setVisible(true);
    this.setTextColor(16777215);
    this.pos = pos;
    this.tileFieldId = field;
  }

  @Override
  public boolean charTyped(char chr, int p) {
    if (!Character.isDigit(chr)) {
      return false;
    }
    boolean worked = super.charTyped(chr, p);
    if (worked) {
      // save to machine
      PacketRegistry.INSTANCE.sendToServer(new PacketTileData(this.tileFieldId,
          this.getCurrent(), pos));
    }
    return worked;
  }

  private int tileFieldId;
  //  private int maxVal;
  //  private int minVal;
  //
  //  public int getMaxVal() {
  //    return maxVal;
  //  }
  //
  //  public void setMaxVal(int maxVal) {
  //    this.maxVal = maxVal;
  //  }
  //
  //  public int getMinVal() {
  //    return minVal;
  //  }
  //
  //  public void setMinVal(int minVal) {
  //    this.minVal = minVal;
  //  }

  public int getCurrent() {
    try {
      return Integer.parseInt(this.getText());
    }
    catch (Exception e) {
      return 0;
    }
  }
}
