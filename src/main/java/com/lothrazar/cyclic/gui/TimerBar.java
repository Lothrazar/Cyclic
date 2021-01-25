package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TimerBar {

  private ContainerScreen<?> parent;
  private int x = 20;
  private int y = 98;
  private int capacity;
  public int width = 26;
  private int height = 14;
  public int guiLeft;
  public int guiTop;
  public boolean showText = true;
  public boolean visible = true;

  public TimerBar(ContainerScreen<?> parent, int x, int y, int cap) {
    this.parent = parent;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + height;
  }

  public void draw(MatrixStack ms, float timer) {
    if (!visible) {
      return;
    }
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.PROGRESS);
    float pct = Math.min(timer / capacity, 1.0F);
    Screen.blit(ms, guiLeft + x, guiTop + y,
        0, 0,
        (int) (width * pct), height,
        width, height);
    if (showText) {
      Minecraft.getInstance().fontRenderer.drawString(ms, "[" + ((int) timer) + "]",
          guiLeft + x + 2,
          guiTop + y + 4, 4209792);
    }
  }

  public void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY, int curr) {
    if (this.isMouseover(mouseX, mouseY)) {
      int seconds = curr / Const.TICKS_PER_SEC;
      String tt = seconds + "";
      List<ITextComponent> list = new ArrayList<>();
      list.add(new TranslationTextComponent(tt));
      parent.func_243308_b(ms, list, mouseX, mouseY);
    }
  }
}
