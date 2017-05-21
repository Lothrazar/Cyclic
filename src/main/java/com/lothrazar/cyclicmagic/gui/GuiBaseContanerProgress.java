package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.client.gui.Gui;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class GuiBaseContanerProgress extends GuiBaseContainer {
  private static final int progressWidth = 156;
  private static final int progressH = 7;
  private static final int fuelWidth = 156;
  private static final int fuelH = 7;
  public GuiBaseContanerProgress(Container inventorySlotsIn, TileEntityBaseMachineInvo tile) {
    super(inventorySlotsIn, tile);
  }
  public abstract int getProgressX();
  public abstract int getProgressY();
  public abstract int getProgressCurrent();
  public abstract int getProgressMax();
  public boolean usesFuel() {
    return false;
  }
  public int getFuel() {
    return 0;
  }
  public ResourceLocation getProgressAsset() {
    return Const.Res.PROGRESS;
  }
  public ResourceLocation getFuelAsset() {
    return Const.Res.FUEL;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    drawProgressBar();//TODO: should we just pass TILE into this class from subs?
    if (usesFuel()) {
      drawFuelBar();
    }
  }
  public void drawFuelBar() {
    int u = 0, v = 0;
    int w = fuelWidth;
    int h = fuelH;
    int MAX = 2000;//fake number. currently its jus using furnace fuel meanin differnet every time for full item. to be worked on
    int x = this.guiLeft + 10;
    int y = this.guiTop + 60;
    this.mc.getTextureManager().bindTexture(Const.Res.FUELCTR);
    Gui.drawModalRectWithCustomSizedTexture(x,y, u, v, (int) w, h, w, h);
    if (getFuel() > 0) {
      this.mc.getTextureManager().bindTexture(getFuelAsset());
      float percent = ((float) getFuel()) / ((float) MAX);
      Gui.drawModalRectWithCustomSizedTexture(x,y, u, v, (int) (w * percent), h, w, h);
    }
  }
  public void drawProgressBar() {
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.PROGRESSCTR);
    Gui.drawModalRectWithCustomSizedTexture(getProgressX(), getProgressY(), u, v, (int) progressWidth, progressH, progressWidth, progressH);
    if (getProgressCurrent() > 0) {
      this.mc.getTextureManager().bindTexture(getProgressAsset());
      float percent = ((float) getProgressCurrent()) / ((float) getProgressMax());
      Gui.drawModalRectWithCustomSizedTexture(getProgressX(), getProgressY(), u, v, (int) (progressWidth * percent), progressH, progressWidth, progressH);
    }
  }
}
