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
  private final int w = 36 / 2;
  private final int h = 124 / 2;

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
        w, h,
        w, h);
    parent.mc.getTextureManager().bindTexture(fluid);
    float percent = (currentFluid / ((float) this.getCapacity()));
    int hpct = (int) ((h - 2) * percent);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + getX() + 1, parent.getGuiTop() + getY() + 1 + h - hpct - 2,
        u, v,
        16, hpct,
        16, h);
  }

  public void draw(FluidStack fluid) {

    //bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    //  
    int u = 0, v = 0;
    parent.mc.getTextureManager().bindTexture(Const.Res.FLUID_BACKGROUND);
    Gui.drawModalRectWithCustomSizedTexture(
        parent.getGuiLeft() + getX(), parent.getGuiTop() + getY(), u, v,
        w, h,
        w, h);
    //NOW the fluid part
    if (fluid == null) {
      return;
    }
    float percent = (((float) fluid.amount) / ((float) this.getCapacity()));
    int hpct = (int) ((h - 2) * percent);
    parent.mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
    TextureAtlasSprite tex = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(fluid.getFluid().getStill(fluid).toString());
    //     
    int size = 16;
    int start = parent.getGuiTop() + getY() + 1;
    int hgt = start + h - hpct - 2;
   
    //    for (int i = 0; i < hgt / size; i++) {
    //      
    parent.drawTexturedModalRect(parent.getGuiLeft() + getX() + 1, start + 2 * size, tex, size, size);
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
    return parent.getGuiLeft() + getX() < mouseX && mouseX < parent.getGuiLeft() + getX() + w
        && parent.getGuiTop() + getY() < mouseY && mouseY < parent.getGuiTop() + getY() + h;
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
