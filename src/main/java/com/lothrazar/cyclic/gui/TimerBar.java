package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.data.Const;
import com.lothrazar.cyclic.registry.TextureRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class TimerBar {

  private AbstractContainerScreen<?> parent;
  private int x = 20;
  private int y = 98;
  public int capacity;
  public int width = 26;
  private int height = 14;
  public int guiLeft;
  public int guiTop;
  public boolean showText = true;
  public boolean visible = true;

  public TimerBar(AbstractContainerScreen<?> parent, int x, int y, int cap) {
    this.parent = parent;
    this.x = x;
    this.y = y;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + height;
  }

  public void draw(PoseStack ms, float timer) {
    if (!visible) {
      return;
    }
    parent.getMinecraft().getTextureManager().bind(TextureRegistry.PROGRESS);
    float pct = Math.min(timer / capacity, 1.0F);
    Screen.blit(ms, guiLeft + x, guiTop + y,
        0, 0,
        (int) (width * pct), height,
        width, height);
    if (showText) {
      Minecraft.getInstance().font.draw(ms, "[" + ((int) timer) + "]",
          guiLeft + x + 2,
          guiTop + y + 4, 4209792);
    }
  }

  public void renderHoveredToolTip(PoseStack ms, int mouseX, int mouseY, int curr) {
    if (this.isMouseover(mouseX, mouseY) && this.visible) {
      String display = "";
      int seconds = curr / Const.TICKS_PER_SEC;
      if (curr > Const.TICKS_PER_SEC * 120) {
        //if more than 120 secs which is two minutes
        int minutes = seconds / 60;
        int remainder = seconds % 60;
        display = minutes + "m " + remainder + "s";
      }
      else if (curr > Const.TICKS_PER_SEC * 20) {
        //if more than 20 seconds, show seconds not ticks 
        display = seconds + "s";
      }
      else {
        display = curr + "";
      }
      List<Component> list = new ArrayList<>();
      list.add(new TranslatableComponent(display));
      parent.renderComponentTooltip(ms, list, mouseX, mouseY);
    }
  }
}
