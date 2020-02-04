package com.lothrazar.cyclic.gui;

import com.lothrazar.cyclic.registry.TextureRegistry;
import net.minecraft.client.gui.screen.Screen;

public class EnergyBar {

  private Screen parent;
  public int max;
  public int guiLeft;
  public int guiTop;
  private int x = 154;
  private int y = 8;
  private int w = 16;
  private int h = 78;

  public EnergyBar(Screen parent) {
    this.parent = parent;
  }

  public boolean isMouseover(int mouseX, int mouseY) {
    return guiLeft + x < mouseX && mouseX < guiLeft + x + w
        && guiTop + y < mouseY && mouseY < guiTop + y + h;
  }

  public void renderEnergy(float energ) {
    int relX;
    int relY;
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.ENERGY_CTR);
    relX = guiLeft + x;
    relY = guiTop + y;
    Screen.blit(relX, relY, 0, 0, 16, 66, w, h);
    parent.getMinecraft().getTextureManager().bindTexture(TextureRegistry.ENERGY_INNER);
    relX = relX + 1;
    relY = relY + 1;
    //    float energ = container.getEnergy();
    float pct = Math.min(energ / max, 1.0F);
    Screen.blit(relX, relY, 0, 0, 14, (int) (64 * pct), 14, 64);
  }

  public void renderHoveredToolTip(int mouseX, int mouseY, int energ) {
    if (this.isMouseover(mouseX, mouseY)) {
      String tips = "" + energ + "/" + this.max;
      parent.renderTooltip(tips, mouseX, mouseY);
    }
  }
}
