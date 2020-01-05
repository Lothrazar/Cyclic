package com.lothrazar.cyclic.base;

import org.lwjgl.opengl.GL11;
import com.lothrazar.cyclic.CyclicRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiButtonExt;

public class ButtonTooltip extends GuiButtonExt {

  public static enum TextureEnum {

    REDSTONE_ON, REDSTONE_OFF;

    public int getX() {
      switch (this) {
        case REDSTONE_OFF://redstone cancel
          return 94;
        case REDSTONE_ON://lit redstone torch
          return 78;
        default:
        break;
      }
      return 0;
    }

    public int getY() {
      switch (this) {
        case REDSTONE_OFF:
          return 478;
        case REDSTONE_ON:
          return 478;
        default:
        break;
      }
      return 0;
    }
  }

  private TextureEnum textureId = TextureEnum.REDSTONE_OFF;
  private String tooltip;

  @Override
  public void renderButton(int mouseX, int mouseY, float partial) {
    super.renderButton(mouseX, mouseY, partial);
    textureId = TextureEnum.REDSTONE_ON;
    Minecraft minecraft = Minecraft.getInstance();
    minecraft.getTextureManager().bindTexture(CyclicRegistry.Textures.WIDGETS);
    GL11.glColor4f(1.0F, 1.0F, 1.0F, this.alpha);
    GL11.glEnable(GL11.GL_BLEND);
    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    if (textureId != null) {
      this.blit(this.x, this.y,
          textureId.getX(), textureId.getY(),
          width, height);
    }
    this.renderBg(minecraft, mouseX, mouseY);
  }

  public ButtonTooltip(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
    super(xPos, yPos, width, height, displayString, handler);
  }

  public String getTooltip() {
    return tooltip;
  }

  public void setTooltip(String tooltip) {
    this.tooltip = tooltip;
  }
}
