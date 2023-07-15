package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.lothrazar.library.render.FluidRenderMap;
import com.lothrazar.library.render.FluidRenderMap.FluidFlow;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public class FluidBar {

  public String emtpyTooltip = "0";
  private Font font;
  private int x;
  private int y;
  private int capacity;
  private int width = 18;
  private int height = 62;
  public int guiLeft;
  public int guiTop;

  public FluidBar(Font p, int cap) {
    this(p, 132, 8, cap);
  }

  public FluidBar(Font p, int x, int y, int cap) {
    font = p;
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

  public void draw(GuiGraphics gg, FluidStack fluid) {
    final int u = 0, v = 0, x = guiLeft + getX(), y = guiTop + getY();
    //    parent.getMinecraft().getTextureManager().bind(TextureRegistry.FLUID_WIDGET);
    //    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    //    RenderSystem.setShaderTexture(0, TextureRegistry.FLUID_WIDGET);
    gg.blit(TextureRegistry.FLUID_WIDGET,
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
    TextureAtlasSprite sprite = FluidRenderMap.getFluidTexture(fluid, FluidFlow.STILL);
    if (fluid.getFluid() == Fluids.WATER) {
      //hack in the blue because water is grey and is filled in by the biome when in-world
      RenderSystem.setShaderColor(0, 0, 1, 1);
    }
    int xPosition = x + 1;
    int yPosition = y + 1;
    int maximum = height - 2;
    int desiredWidth = width - 2;
    int desiredHeight = fluidAmount - 2;
    // the .getBlitOffset() no longer exists.
    //good news we can drop vertexbuilder sprites and use gg blit this way
    // RenderUtils.drawTiledSprite(gg, xPosition,yPosition,yOffset, width - 2, fluidAmount - 2, sprite);
    gg.blit(xPosition, yPosition + (maximum - desiredHeight), 0, desiredWidth, desiredHeight, sprite);
    if (fluid.getFluid() == Fluids.WATER) {
      RenderSystem.setShaderColor(1, 1, 1, 1); //un-apply the water filter
    }
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x <= mouseX && mouseX <= guiLeft + x + width
        && guiTop + y <= mouseY && mouseY <= guiTop + y + height;
  }

  public void renderHoveredToolTip(GuiGraphics ms, int mouseX, int mouseY, FluidStack current) {
    if (this.isMouseover(mouseX, mouseY)) {
      this.renderTooltip(ms, mouseX, mouseY, current);
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

  public void renderTooltip(GuiGraphics gg, int mouseX, int mouseY, FluidStack current) {
    String tt = emtpyTooltip;
    if (current != null && !current.isEmpty()) {
      tt = current.getAmount() + "/" + getCapacity() + " " + current.getDisplayName().getString();
    }
    List<Component> list = new ArrayList<>();
    list.add(Component.translatable(tt));
    gg.renderComponentTooltip(font, list, mouseX, mouseY);
  }
}
