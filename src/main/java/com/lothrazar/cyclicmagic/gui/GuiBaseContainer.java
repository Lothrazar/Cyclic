package com.lothrazar.cyclicmagic.gui;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityBaseMachineInvo;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class GuiBaseContainer extends GuiContainer {
  public int FONTCOLOR = 4210752;
  public static final int WIDTH = 176;
  public static final int HEIGHT = 166;
  protected TileEntityBaseMachineInvo tile;
  public GuiBaseContainer(Container inventorySlotsIn, TileEntityBaseMachineInvo tile) {
    super(inventorySlotsIn);
    this.tile = tile;
  }
  public GuiBaseContainer(Container inventorySlotsIn) {
    super(inventorySlotsIn);
    this.tile = null;
  }
  public ResourceLocation getBackground() {
    return Const.Res.TABLEDEFAULT;//can override
  }
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
    this.fontRendererObj.drawString(display, x, y, 4210752);
  }
  @SideOnly(Side.CLIENT)
  @Override
  protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    if (tile != null) {
      String s = UtilChat.lang(tile.getName());
      this.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6);
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
    this.mc.getTextureManager().bindTexture(getBackground());
    int thisX = getMiddleX();
    int thisY = getMiddleY();
    int u = 0, v = 0;
    Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, this.xSize, this.ySize);
    // Gui.drawModalRectWithCustomSizedTexture(thisX, thisY, u, v, this.xSize, this.ySize, WIDTH, HEIGHT);
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
}
