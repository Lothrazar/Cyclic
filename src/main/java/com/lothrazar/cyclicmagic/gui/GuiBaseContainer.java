package com.lothrazar.cyclicmagic.gui;
import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclicmagic.ITooltipButton;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.Const.ScreenSize;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GuiBaseContainer extends GuiContainer {
  public final static int FONTCOLOR = 4210752;
  protected TileEntityBaseMachineInvo tile;
  protected Const.ScreenSize screenSize = ScreenSize.STANDARD;
  protected int fieldRedstoneBtn = -1;
  protected int fieldPreviewBtn = -1;
  protected int fieldFuel = -1;
  protected int fieldMaxFuel = -1;
  public ProgressBar progressBar = null;
  private GuiButtonToggleRedstone redstoneBtn = null;
  private GuiButtonTogglePreview btnPreview;
  public GuiBaseContainer(Container inventorySlotsIn, TileEntityBaseMachineInvo tile) {
    super(inventorySlotsIn);
    this.tile = tile;
  }
  public GuiBaseContainer(Container inventorySlotsIn) {
    super(inventorySlotsIn);
    this.tile = null;
  }
  protected void setScreenSize(Const.ScreenSize ss) {
    this.screenSize = ss;
    this.xSize = screenSize.width();
    this.ySize = screenSize.height();
  }
  protected void setFieldFuel(int ordinal) {
    this.fieldFuel = ordinal;
    this.fieldMaxFuel = ordinal + 1;
  }
  @Override
  public void initGui() {
    super.initGui();
    int x = this.guiLeft + Const.PAD / 2;
    int y = this.guiTop + Const.PAD / 2;
    if (this.fieldRedstoneBtn >= 0) {
      redstoneBtn = new GuiButtonToggleRedstone(1,
          x,
          y, this.tile.getPos());
      this.buttonList.add(redstoneBtn);
      y += Const.PAD / 2 + redstoneBtn.width;
    }
    if (this.fieldPreviewBtn > 0) {
      btnPreview = new GuiButtonTogglePreview(2,
          x,
          y, this.tile.getPos());
      this.buttonList.add(btnPreview);
    }
  }
  protected int fuelX = -30;//temp hax might be progress bar later?
  protected int fuely = 28;//fuel GUI???
  /**
   * ONLY CALL FROM drawGuiContainerForegroundLayer
   * 
   * @param x
   * @param y
   * @param f
   */
  protected void drawFieldAt(int x, int y, int f) {
    String display = "" + this.tile.getField(f);
    x = (display.length() > 1) ? x - 3 : x;
    this.drawString(display, x, y);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    drawNameText();
    drawFuelText();
    updateToggleButtonStates();
  }
  public void drawNameText() {
    if (tile != null) {
      String s = UtilChat.lang(tile.getName());
      this.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6);
    }
  }
  public void updateToggleButtonStates() {
    if (redstoneBtn != null) {
      redstoneBtn.setState(tile.getField(this.fieldRedstoneBtn));
    }
    if (btnPreview != null) {
      if (tile.getField(this.fieldPreviewBtn) == 1) {
        btnPreview.setStateOn();
      }
      else {
        btnPreview.setStateOff();
      }
    }
  }
  public void drawString(String s, int x, int y) {
    this.fontRendererObj.drawString(s, x, y, FONTCOLOR);
  }
  public int getMiddleY() {
    int yMiddle = (this.height - this.ySize) / 2;
    return yMiddle;
  }
  public int getMiddleX() {
    int xMiddle = (this.width - this.xSize) / 2;
    return xMiddle;
  }
  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    //    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);// abstract
    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    this.mc.getTextureManager().bindTexture(screenSize.texture());
    int thisX = getMiddleX();
    int thisY = getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v,
        screenSize.width(), screenSize.height(),
        screenSize.width(), screenSize.height());
    if (this.progressBar != null) {
      drawProgressBar();
    }
    if (this.fieldFuel > -1 && this.tile.getFuelCurrent() > 0) {
      drawFuelBar();
    }
  }
  public void drawFuelText() {
    if (this.fieldFuel > -1) {

      int percent = (int) ((float) tile.getField(this.fieldFuel) / (float) tile.getField(this.fieldMaxFuel) * 100);
      if (percent > 0) {
        float fontScale = 0.5F;
        GL11.glScalef(fontScale, fontScale, fontScale);
        this.drawString(percent + "", this.xSize * 2 + 20, fuely - 4);
      }
    }
  }
  public void drawFuelBar() {
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(Const.Res.FUEL_CTR);
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + screenSize.width() + 1,
        this.guiTop, u, v,
        24, 83,
        24, 83);
    this.mc.getTextureManager().bindTexture(Const.Res.FUEL_INNER);
    float percent = ((float) tile.getField(this.fieldFuel)) / ((float) tile.getField(this.fieldMaxFuel));
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + screenSize.width() + Const.PAD,
        this.guiTop + Const.PAD, u, v,
        10, (int) (67 * percent),
        10, 67);
  }
  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    super.drawScreen(mouseX, mouseY, partialTicks);
    ITooltipButton btn;
    for (int i = 0; i < buttonList.size(); i++) {
      if (buttonList.get(i).isMouseOver() && buttonList.get(i) instanceof ITooltipButton) {
        btn = (ITooltipButton) buttonList.get(i);
        if (btn.getTooltips() != null) {
          drawHoveringText(btn.getTooltips(), mouseX, mouseY, fontRendererObj);
        }
        break;// cant hover on 2 at once
      }
    }
  }
  public void drawProgressBar() {
    int u = 0, v = 0;
    this.mc.getTextureManager().bindTexture(progressBar.getProgressCtrAsset());
    Gui.drawModalRectWithCustomSizedTexture(
        this.guiLeft + progressBar.xOffset,
        this.guiTop + progressBar.yOffset, u, v,
        (int) ProgressBar.WIDTH, ProgressBar.HEIGHT,
        ProgressBar.WIDTH, ProgressBar.HEIGHT);
    if (progressBar.getProgressCurrent() > 0) {
      this.mc.getTextureManager().bindTexture(progressBar.getProgressAsset());
      float percent = ((float) progressBar.getProgressCurrent()) / ((float) progressBar.maxValue);
      Gui.drawModalRectWithCustomSizedTexture(
          this.guiLeft + progressBar.xOffset,
          this.guiTop + progressBar.yOffset,
          u, v,
          (int) (ProgressBar.WIDTH * percent),
          ProgressBar.HEIGHT, ProgressBar.WIDTH, ProgressBar.HEIGHT);
    }
  }
}
