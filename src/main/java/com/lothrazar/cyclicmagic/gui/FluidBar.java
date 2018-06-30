package com.lothrazar.cyclicmagic.gui;

import com.lothrazar.cyclicmagic.core.gui.GuiBaseContainer;
import com.lothrazar.cyclicmagic.core.util.Const;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidBar {

  private GuiBaseContainer parent;
  private int x;
  private int y;
  private int capacity;
  private final int width = 36 / 2;
  private final int height = 124 / 2;

  public FluidBar(GuiBaseContainer p, int x, int y) {
    parent = p;
    this.setX(x);
    this.setY(y);
  }

  public void draw(int currentFluid, ResourceLocation fluid) {
    int u = 0, v = 0;
    parent.mc.getTextureManager().bindTexture(Const.Res.FLUID_BACKGROUND);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + getX(), parent.getGuiTop() + getY(), u, v,
        width, height,
        width, height);
    parent.mc.getTextureManager().bindTexture(fluid);
    float percent = (currentFluid / ((float) this.getCapacity()));
    int hpct = (int) ((height - 2) * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + getX() + 1, parent.getGuiTop() + getY() + 1 + height - hpct - 2,
        u, v,
        16, hpct,
        16, height);
  }

  public void draw(FluidStack fluid) {

    //bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    //  
    final int u = 0, v = 0, x = parent.getGuiLeft() + getX(), y = parent.getGuiTop() + getY();
    parent.mc.getTextureManager().bindTexture(Const.Res.FLUID_BACKGROUND);
    Gui.drawModalRectWithCustomSizedTexture(
       x, y, u, v,
        width, height,
        width, height);
    //NOW the fluid part
    if (fluid == null) {
      return;
    }
    float capacity = this.getCapacity();
    float amount = fluid.amount;
    float scale = amount / capacity;
    int fluidAmount = (int) (scale * height);
    
    //    int yVal = y + height - fluidAmount;
    //    int widthInner = width - 2;
    //    int heightInner = height - 2;
    //    int heightFluid = height - fluidAmount;
    // float percent = (((float) fluid.amount) / ((float) this.getCapacity()));
    //    int hpct = (int) ((height - 2) * percent);
    parent.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());

    drawFluid(x + 1, y + height - 2 - fluidAmount + 2, icon, 16, fluidAmount - 1);
  }

  /**
   * Thanks to Tiffiter123 on forums http://www.minecraftforge.net/forum/topic/39651-solved-194-render-fluid-in-gui/
   * 
   * @param x
   * @param y
   * @param icon
   * @param width
   * @param height
   */
  private void drawFluid(int x, int y, TextureAtlasSprite icon, int width, int height) {
    int size = width;

    int drawHeight = 0;
    int drawWidth = 0;
    for (int i = 0; i < width; i += size) {
      for (int j = 0; j < height; j += size) {
        drawWidth = Math.min(width - i, size);
        drawHeight = Math.min(height - j, size);
        parent.drawTexturedModalRect(x + i, y + j, icon, drawWidth, drawHeight);
      }
    }
  }
  public boolean isMouseover(int mouseX, int mouseY) {
    return parent.getGuiLeft() + getX() < mouseX && mouseX < parent.getGuiLeft() + getX() + width
        && parent.getGuiTop() + getY() < mouseY && mouseY < parent.getGuiTop() + getY() + height;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }
}
