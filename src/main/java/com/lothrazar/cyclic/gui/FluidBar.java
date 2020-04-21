package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.cyclic.render.FluidRenderMap.FluidType;
import com.lothrazar.cyclic.util.UtilFluid;
import com.lothrazar.cyclic.util.UtilRender;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;

public class FluidBar {

  private Screen parent;
  private int x;
  private int y;
  private int capacity;
  private int width = 18;
  private int height = 62;
  public int guiLeft;
  public int guiTop;

  public FluidBar(Screen p, int x, int y, int cap) {
    parent = p;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public void draw(FluidStack fluid) {
    final int u = 0, v = 0, x = guiLeft + getX(), y = guiTop + getY();
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.FLUID_BACKGROUND);
    Screen.blit(
        x, y, u, v,
        width, height,
        width, height);
    //NOW the fluid part
    if (fluid == null || this.getCapacity() == 0 || fluid.getAmount() == 0) {
      return;
    }
    float capacity = this.getCapacity();
    float amount = fluid.getAmount();
    float scale = amount / capacity;
    int fluidAmount = (int) (scale * height);
    TextureAtlasSprite icon = UtilFluid.getBaseFluidTexture(fluid.getFluid(), FluidType.STILL);
    drawTiledSprite(x + 1, y + 1, height - 2, width - 2, fluidAmount - 2, icon);
  }

  protected void drawTiledSprite(int xPosition, int yPosition, int yOffset, int desiredWidth, int desiredHeight, TextureAtlasSprite sprite) {
    UtilRender.drawTiledSprite(xPosition, yPosition, yOffset, desiredWidth, desiredHeight, sprite, width - 2, width - 2, parent.getBlitOffset());
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + height;
  }

  public void renderHoveredToolTip(int mouseX, int mouseY, FluidStack current) {
    if (this.isMouseover(mouseX, mouseY) && current != null
        && !current.isEmpty()) {
      parent.renderTooltip(current.getAmount() + "/" + getCapacity() + " " + current.getDisplayName().getFormattedText(), mouseX, mouseY);
    }
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public int getCapacity() {
    return capacity;
  }
}
