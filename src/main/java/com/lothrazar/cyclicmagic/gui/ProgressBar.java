package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.base.GuiBaseContainer;
import net.minecraft.util.ResourceLocation;

public class ProgressBar {
  public static final int WIDTH = 156;
  public static final int HEIGHT = 7;
  public int xOffset;
  public int yOffset;
  public int fieldId;
  public int maxValue;
  public ResourceLocation asset = Const.Res.PROGRESS;
  private GuiBaseContainer parent;
  public ProgressBar(GuiBaseContainer p, int x, int y, int f, int max) {
    parent = p;
    this.xOffset = x;
    this.yOffset = y;
    this.fieldId = f;
    this.maxValue = max;
  }
  public int getProgressCurrent() {
    //parent and tile should never be null, just dont ever add a progress bar without a TE
    return parent.tile.getField(fieldId);
  }
  public ResourceLocation getProgressCtrAsset() {
    return Const.Res.PROGRESSCTR;
  }
  public ResourceLocation getProgressAsset() {
    return asset;
  }
}
