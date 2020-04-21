package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.screen.Screen;

public class EnergyBar {

  private Screen parent;
  private int x = 154;
  private int y = 8;
  public int capacity;
  private int width = 16;
  private int height = 78;
  public int guiLeft;
  public int guiTop;

  public EnergyBar(Screen parent, int cap) {
    this.parent = parent;
    this.capacity = cap;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + width
        && guiTop + y < mouseY && mouseY < guiTop + y + height;
  }

  public void draw(float energ) {
    int relX;
    int relY;
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.ENERGY_CTR);
    relX = guiLeft + x;
    relY = guiTop + y;
    Screen.blit(relX, relY, 0, 0, 16, 66, width, height);
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.ENERGY_INNER);
    relX = relX + 1;
    relY = relY + 1;
    float pct = Math.min(energ / capacity, 1.0F);
    Screen.blit(relX, relY, 0, 0, 14, (int) (64 * pct), 14, 64);
  }

  public void renderHoveredToolTip(int mouseX, int mouseY, int energ) {
    if (this.isMouseover(mouseX, mouseY)) {
      parent.renderTooltip(energ + "/" + this.capacity, mouseX, mouseY);
    }
  }
}
