package com.lothrazar.cyclic.gui;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class EnergyBar {

  private Screen parent;
  private int x = 154;
  private int y = 8;
  public int capacity;
  private int width = 16;
  private int height = 62;
  public int guiLeft;
  public int guiTop;
  public boolean visible = true;

  public EnergyBar(Screen parent, int cap) {
    this.parent = parent;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + getHeight();
  }

  public void draw(PoseStack ms, float energ) {
    if (!visible) {
      return;
    }
    int relX;
    int relY;
    //    parent.getMinecraft().getTextureManager().bind(TextureRegistry.ENERGY_CTR);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, TextureRegistry.ENERGY_BAR);
    relX = guiLeft + x;
    relY = guiTop + y;
    Screen.blit(ms, relX, relY, 16, 0, width, getHeight(), 32, getHeight());
    float pct = Math.min(energ / capacity, 1.0F);
    Screen.blit(ms, relX, relY, 0, 0, width, getHeight() - (int) (getHeight() * pct), 32, getHeight());
  }

  public void renderHoveredToolTip(PoseStack ms, int mouseX, int mouseY, int energ) {
    if (visible && this.isMouseover(mouseX, mouseY)) {
      String tt = energ + "/" + this.capacity;
      List<Component> list = new ArrayList<>();
      list.add(new TranslatableComponent(tt));
      parent.renderComponentTooltip(ms, list, mouseX, mouseY);
    }
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}
