package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class TimerBar {

  private Screen parent;
  private int x = 20;
  private int y = 98;
  private int capacity;
  public int width = 26;
  private int height = 14;
  public int guiLeft;
  public int guiTop;

  public TimerBar(Screen parent, int x, int y, int cap) {
    this.parent = parent;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + height;
  }

  public void draw(MatrixStack ms, float energ) {
    //    Screen.blit(relX, relY, 0, 0, width, height, width, height);
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.PROGRESS);
    float pct = Math.min(energ / capacity, 1.0F);
    Screen.blit(ms, guiLeft + x, guiTop + y,
        0, 0,
        (int) (width * pct), height,
        width, height);
  }

  public void renderHoveredToolTip(MatrixStack ms, int mouseX, int mouseY, int curr) {
    if (this.isMouseover(mouseX, mouseY)) {
      int seconds = curr / 20;
      String tt = seconds + "";// + this.capacity;
      List<ITextComponent> list = new ArrayList<>();
      list.add(new TranslationTextComponent(tt));
      parent.func_243308_b(ms, list, mouseX, mouseY);
    }
  }
}
