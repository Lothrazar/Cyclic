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
    
    int yVal = y + height - fluidAmount;
    int widthInner = width - 2;
    int heightInner = height - 2;
    // float percent = (((float) fluid.amount) / ((float) this.getCapacity()));
    //    int hpct = (int) ((height - 2) * percent);
    parent.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    TextureAtlasSprite icon = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
    //     
    int size = 16;
    int start = parent.getGuiTop() + getY() + 1;
    /// int hgt = start + height - hpct - 2;
    System.out.println("====");
    int i = 0;
    int j = 0;
    int drawHeight = 0;
    int drawWidth = 0;
    for (i = 0; i < widthInner; i += size) {
      for (j = 0; j < heightInner; j += size) {
        drawWidth = Math.min(widthInner - i, size);
        drawHeight = Math.min(heightInner - j, size);
        System.out.println("i=" + i + "  j=" + j + "  drawWidth=" + drawWidth + "  drawHeight=" + drawHeight);
        parent.drawTexturedModalRect(x + i + 1, yVal + j + 1, icon, drawWidth, drawHeight);
      }
    }
    //    for (int i = 0; i < hgt / size; i++) {
    //      
    //  parent.drawTexturedModalRect(parent.getGuiLeft() + getX() + 1, start + 2 * size, tex, size, size);
    //    }
   // 

    //parent.drawTexturedModalRect(parent.getGuiLeft() + getX() + 1, hgt, tex, size, size);
    //    Gui.drawModalRectWithCustomSizedTexture(
    //        parent.getGuiLeft() + getX() + 1, parent.getGuiTop() + getY() + 1 + h - hpct - 2,
    //        u, v,
    //        16, hpct,
    //        16, h);
    ///////////// 
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
